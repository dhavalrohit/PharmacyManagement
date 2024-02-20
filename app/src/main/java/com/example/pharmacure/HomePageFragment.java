package com.example.pharmacure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.factory.Firebase_factory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class HomePageFragment extends Fragment {
    View homeview;

    TextView tx;
    DatabaseReference db;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        homeview=inflater.inflate(R.layout.homepage,container,false);
        getActivity().setTitle("Home");

         tx= (TextView) homeview.findViewById(R.id.homepagehometext);

        ImageView btntrans=(ImageView) homeview.findViewById(R.id.btntrans_homeicon);
        ImageView btninventory=(ImageView) homeview.findViewById(R.id.btninventory_homeicon);
        ImageView btntranshistory= (ImageView)homeview.findViewById(R.id.btntranshistory_homeicon);
        ImageView btncust= (ImageView)homeview.findViewById(R.id.btncust_homeicon);



        String uid=Firebase_factory.getfbUserId();

         db= Firebase_factory.getdatabaseRef().child("Users").child(uid);
        return homeview;
    }

    @Override
    public void onResume() {
        super.onResume();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String storename=snapshot.child("info").child("storename").getValue(String.class);
                System.out.println(storename);
                tx.setText(storename);
                String name=snapshot.child("info").child("name").getValue(String.class);
                System.out.println(name);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getBaseContext(), "Firebase Error",Toast.LENGTH_SHORT);
            }
        });
        MainActivity.floatingbtn.hide();
    }

}