package com.example.pharmacure.Inventory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_inventory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ItemsFragement extends Fragment implements SearchView.OnQueryTextListener {
 View itemview;
 private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    MyAdapterMedicine adapter;
    ArrayList<MedicineModel> models = new ArrayList<>();
    boolean menuqty = false, menuexp = false;
    MenuItem orderN, orderQ, orderE;
    static String generatedproductid;

    String uid;
    private DatabaseReference db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        itemview = inflater.inflate(R.layout.activity_items, container, false);
        getActivity().setTitle("Items");
        MainActivity.flag = 1;
        setHasOptionsMenu(true);

        // INITIALIZE RV
        recyclerView = (RecyclerView) itemview.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        generatedproductid=MainActivity.productid;
        generatedproductid= Firebase_factory_inventory.getproductid();
        mAuth = Firebase_factory.getFirebaseAuth_Instance();
        uid = Firebase_factory.getfbUserId();
        db = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Inventory");
        //db.keepSynced(true);

        return itemview;

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewItem_Activity.class);
                startActivity(i);
            }
        });
        MainActivity.floatingbtn.show();
        // ADAPTER
        adapter = new MyAdapterMedicine(getContext(), retrive());

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {

        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_updateinfo).setVisible(false);
        menu.findItem(R.id.order_due).setVisible(false);
        menu.findItem(R.id.order_ename).setVisible(true);
        menu.findItem(R.id.order_exp).setVisible(true);
        menu.findItem(R.id.order_qty).setVisible(true);
        inflater.inflate(R.menu.search_bar, menu);
        final MenuItem searchitem = menu.findItem(R.id.search);
        orderQ = menu.findItem(R.id.order_qty);
        orderE = menu.findItem(R.id.order_exp);
        orderN = menu.findItem(R.id.order_ename);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchitem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                menu.findItem(R.id.order_ename).setVisible(true);
                menu.findItem(R.id.order_exp).setVisible(true);
                menu.findItem(R.id.order_qty).setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.findItem(R.id.order_ename).setVisible(false);
                menu.findItem(R.id.order_exp).setVisible(false);
                menu.findItem(R.id.order_qty).setVisible(false);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.order_exp) {

            if (models.size() > 0) {
                Collections.sort(models, new Comparator<MedicineModel>() {
                    DateFormat f = new SimpleDateFormat("MM-yyyy");

                    @Override
                    public int compare(final MedicineModel object1, final MedicineModel object2) {
                        try {
                            return f.parse(object1.getExpiryDate()).compareTo(f.parse(object2.getExpiryDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
            }
            menuqty = false;
            menuexp = true;
            orderE.setChecked(true);
            orderN.setChecked(false);
            orderQ.setChecked(false);
            adapter.notifyDataSetChanged();
            return true;
        }

        if (id == R.id.order_qty) {

            if (models.size() > 0) {
                Collections.sort(models, new Comparator<MedicineModel>() {
                    @Override
                    public int compare(MedicineModel p1, MedicineModel p2) {
                        return Integer.parseInt(p1.getQuantity()) - Integer.parseInt(p2.getQuantity());
                    }
                });
            }
            menuqty = true;
            menuexp = false;
            orderE.setChecked(false);
            orderN.setChecked(false);
            orderQ.setChecked(true);
            adapter.notifyDataSetChanged();
            return true;
        }

        if (id == R.id.order_ename) {

            if (models.size() > 0) {
                Collections.sort(models, new Comparator<MedicineModel>() {
                    @Override
                    public int compare(final MedicineModel object1, final MedicineModel object2) {
                        return object1.getProductName().compareTo(object2.getProductName());
                    }
                });
            }
            menuqty = false;
            menuexp = false;
            orderE.setChecked(false);
            orderN.setChecked(true);
            orderQ.setChecked(false);
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    private void fetchData(DataSnapshot dataSnapshot) {
        models.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            MedicineModel model = ds.getValue(MedicineModel.class);
            models.add(model);
        }

        if (menuqty) {
            if (models.size() > 0) {
                Collections.sort(models, new Comparator<MedicineModel>() {
                    @Override
                    public int compare(MedicineModel p1, MedicineModel p2) {
                        return Integer.parseInt(p1.getQuantity()) - Integer.parseInt(p2.getQuantity());
                    }
                });
            }
        } else if (menuexp) {
            if (models.size() > 0) {
                Collections.sort(models, new Comparator<MedicineModel>() {
                    DateFormat f = new SimpleDateFormat("MM-yyyy");

                    @Override
                    public int compare(final MedicineModel object1, final MedicineModel object2) {
                        try {
                            return f.parse(object1.getExpiryDate()).compareTo(f.parse(object2.getExpiryDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
            }
        } else {
            if (models.size() > 0) {
                Collections.sort(models, new Comparator<MedicineModel>() {
                    @Override
                    public int compare(final MedicineModel object1, final MedicineModel object2) {
                        return object1.getProductName().compareTo(object2.getProductName());
                    }
                });
            }
        }
        adapter.notifyDataSetChanged();
    }
    public ArrayList<MedicineModel> retrive(){
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                fetchData(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                fetchData(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (models.size() == 1) {
                    models.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  models;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;

    }


}