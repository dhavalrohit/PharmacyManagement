package com.example.pharmacure.Doctor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.Model.DoctorModel;
import com.example.pharmacure.Model.TransactionModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Transactions.newTransaction.TransactionActivity;
import com.example.pharmacure.Transactions.transHistory.MyAdapterTransactions;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Iterator;


public class doctorFragement extends Fragment {
    View addedDocotrView;
    String uid;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    DoctorAdapter adapter;
    private RecyclerView rv;
    ArrayList<DoctorModel> models = new ArrayList<>();
    private LinearLayoutManager layoutManager;


     String doctorname="";
     String regno="";
     String locality="";
     String spec="";
     String mobileno="";
     String doctorid="";

ArrayList<DoctorModel> doctorslist=new ArrayList<>();

    Firebase_factory_doctor fbdoctor=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addedDocotrView= inflater.inflate(R.layout.fragement_doctor, container, false);
        getActivity().setTitle("Doctor");
        MainActivity.flag=1;
        setHasOptionsMenu(true);
        rv=addedDocotrView.findViewById(R.id.rv_doctorfragement);
        rv.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rv.setLayoutManager(layoutManager);
        rv.setVisibility(View.GONE);


         fbdoctor=new Firebase_factory_doctor();

        mAuth= Firebase_factory.getFirebaseAuth_Instance();
        uid=Firebase_factory.getfbUserId();
        db= Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Doctors");



        //db.keepSynced(true);


        return addedDocotrView;
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), Doctor_Activity.class);
                startActivity(i);
            }
        });
        MainActivity.floatingbtn.show();
        adapter = new DoctorAdapter(getContext(),retrieve());
        rv.setAdapter(adapter);
        Log.d("size",retrieve().size()+"");

    }

    public void fetchData(DataSnapshot snapshot){
        models.clear();
      /*  for (DataSnapshot ds:snapshot.getChildren()){
            String docID=ds.child("doctorID").getValue(String.class);
            String dname=ds.child("doctorName").getValue(String.class);
            String dmobileno=ds.child("mobileno").getValue(String.class);
            String locality=ds.child("locality").getValue(String.class);
            String spec=ds.child("specialisatopn").getValue(String.class);
            String regno=ds.child("regno").getValue(String.class);


            DoctorModel model=new DoctorModel();
            model.setDoctorName(dname);
            model.setLocality(locality);
            model.setDoctorID(docID);
            model.setRegno(regno);
            model.setSpecialisation(spec);
            model.setMobileno(dmobileno);
            models.add(model);

        }*/

        for (DataSnapshot ds:snapshot.getChildren()){
            DoctorModel model=ds.getValue(DoctorModel.class);
            models.add(model);
        }

        rv.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

    }


    // READ BY HOOKING ONTO DATABASE OPERATION CALLBACKS
    public ArrayList<DoctorModel> retrieve() {
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


    /*public void recievedata(DataSnapshot snapshot) {
        Iterator it = snapshot.getChildren().iterator();


        while (it.hasNext()) {

            doctorid=((String) ((DataSnapshot) it.next()).getValue());
            doctorname=((String) ((DataSnapshot) it.next()).getValue());
            locality=((String) ((DataSnapshot) it.next()).getValue());
            mobileno=((String) ((DataSnapshot) it.next()).getValue());
            regno=((String) ((DataSnapshot) it.next()).getValue());
            spec=((String) ((DataSnapshot) it.next()).getValue());

            DoctorModel model=new DoctorModel(doctorname,locality,regno,mobileno,spec,doctorid);
            doctorslist.add(model);

        }


    }


    public ArrayList<DoctorModel> retrivedoctors(){
        String uid=Firebase_factory.getfbUserId();
        DatabaseReference dbd=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Doctors")
                .child("doctorsList").child(1+"");
        dbd.keepSynced(true);

        dbd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                recievedata(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                recievedata(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return doctorslist;
    }*/




}