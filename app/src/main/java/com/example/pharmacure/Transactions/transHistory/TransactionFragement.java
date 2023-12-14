package com.example.pharmacure.Transactions.transHistory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.window.BackEvent;

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.R;
import com.example.pharmacure.Model.TransactionModel;
import com.example.pharmacure.Transactions.newTransaction.TransactionActivity;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class TransactionFragement extends Fragment implements SearchView.OnQueryTextListener {

    View transactionview;
    String uid;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    MyAdapterTransactions adapter;
    private RecyclerView rv;
    ArrayList<TransactionModel> models = new ArrayList<>();
    private LinearLayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        transactionview= inflater.inflate(R.layout.fragment_transaction_fragement, container, false);
        getActivity().setTitle("Transactions");
        MainActivity.flag=1;
        setHasOptionsMenu(true);
        rv=transactionview.findViewById(R.id.rv_transactionfragement);
        rv.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rv.setLayoutManager(layoutManager);
        rv.setVisibility(View.GONE);

        mAuth= Firebase_factory.getFirebaseAuth_Instance();
        uid=Firebase_factory.getfbUserId();
        db= Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales").child("salesBill");

        db.keepSynced(true);


        return transactionview;
    }




    @Override
    public void onResume() {
        super.onResume();
        MainActivity.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), TransactionActivity.class);
                startActivity(i);
            }
        });
        MainActivity.floatingbtn.show();
        adapter = new MyAdapterTransactions(getContext(), retrieve());
        rv.setAdapter(adapter);
        Log.d("transationlistSize",retrieve().size()+"");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {

        menu.findItem(R.id.order_ename).setVisible(false);
        menu.findItem(R.id.order_qty).setVisible(false);
        menu.findItem(R.id.order_exp).setVisible(false);
        menu.findItem(R.id.order_due).setVisible(false);
        inflater.inflate(R.menu.search_bar, menu);
        final MenuItem searchitem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchitem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // menu.findItem(R.id.order_transdate).setVisible(true);
                menu.findItem(R.id.action_logout).setVisible(true);
                menu.findItem(R.id.action_updateinfo).setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // menu.findItem(R.id.order_transdate).setVisible(false);
                menu.findItem(R.id.action_logout).setVisible(false);
                menu.findItem(R.id.action_updateinfo).setVisible(false);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void fetchData(DataSnapshot snapshot){
        models.clear();
        for (DataSnapshot ds:snapshot.getChildren()){

            TransactionModel model=ds.getValue(TransactionModel.class);
            models.add(model);

        }
        rv.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

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

    // READ BY HOOKING ONTO DATABASE OPERATION CALLBACKS
    public ArrayList<TransactionModel> retrieve() {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (models.size() == 1) {
                    models.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return models;
    }

}