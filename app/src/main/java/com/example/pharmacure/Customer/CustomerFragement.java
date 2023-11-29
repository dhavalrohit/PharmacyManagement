package com.example.pharmacure.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.Model.CustomerModel;
import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.Inflater;


public class CustomerFragement extends Fragment implements SearchView.OnQueryTextListener {

    View view;
    String uid;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    MyAdapterCustomer adapter;
    private RecyclerView rv;
    ArrayList<CustomerModel> models = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    boolean order = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_fragement, container, false);
        getActivity().setTitle("Customers");
        MainActivity.flag = 1;
        setHasOptionsMenu(true);

        //initialize recycleView
        rv = view.findViewById(R.id.rv_customers);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        mAuth = Firebase_factory.getFirebaseAuth_Instance();
        uid = Firebase_factory.getfbUserId();
        db = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Customer");
        db.keepSynced(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), New_Customer_Activity.class);
                startActivity(i);
            }
        });
        MainActivity.floatingbtn.show();

        adapter = new MyAdapterCustomer(getContext(), retrive());
        rv.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_updateinfo).setVisible(false);
        menu.findItem(R.id.order_ename).setVisible(false);
        menu.findItem(R.id.order_qty).setVisible(false);
        menu.findItem(R.id.order_exp).setVisible(false);

        menu.findItem(R.id.order_due).setVisible(true);
        inflater.inflate(R.menu.search_bar, menu);

        final MenuItem searchitem = menu.findItem(R.id.search);
        final SearchView searchview = (SearchView) MenuItemCompat.getActionView(searchitem);
        searchview.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchitem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.findItem(R.id.order_due).setVisible(false);
                return true;

            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                menu.findItem(R.id.order_due).setVisible(true);
                return true;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.order_due) {
            if (!item.isChecked()) {
                if (models.size() > 0) {
                    Collections.sort(models, new Comparator<CustomerModel>() {
                        @Override
                        public int compare(CustomerModel o1, CustomerModel o2) {
                            return Double.compare(Double.parseDouble(o2.getAmount()),
                                    Double.parseDouble(o1.getAmount()));

                        }
                    });
                }
                order = true;
                item.setChecked(true);
                adapter.notifyDataSetChanged();
            } else {
                if (models.size() > 0) {
                    Collections.sort(models, new Comparator<CustomerModel>() {
                        @Override
                        public int compare(final CustomerModel object1, final CustomerModel object2) {
                            return object1.getCustomerName().compareTo(object2.getCustomerName());
                        }
                    });

                }
                order = false;
                item.setChecked(false);
                adapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void FetchData(DataSnapshot snapshot) {
        models.clear();
        for (DataSnapshot ds : snapshot.getChildren()) {
            CustomerModel model = ds.getValue(CustomerModel.class);
            models.add(model);
        }
        if (order) {
            if (models.size() > 0) {
                Collections.sort(models, new Comparator<CustomerModel>() {
                    @Override
                    public int compare(CustomerModel o1, CustomerModel o2) {
                        return Double.compare(Double.parseDouble(o1.getAmount()), Double.parseDouble(o1.getAmount()));
                    }
                });

            }
        }
        else {
            if (models.size()>0){
                Collections.sort(models, new Comparator<CustomerModel>() {
                    @Override
                    public int compare(CustomerModel o1, CustomerModel o2) {
                        return o1.getCustomerName().compareTo(o2.getCustomerName());
                    }
                });
            }
        }
        adapter.notifyDataSetChanged();

    }

    public ArrayList<CustomerModel> retrive(){
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FetchData(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FetchData(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (models.size()==1){
                    models.clear();
                    adapter.notifyDataSetChanged();
                }else {
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
        return models;

    }
}




