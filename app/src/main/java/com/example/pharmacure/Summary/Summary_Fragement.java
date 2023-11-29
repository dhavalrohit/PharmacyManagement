package com.example.pharmacure.Summary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pharmacure.MainActivity;
import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Summary_Fragement extends Fragment {

    View view;
    Button sampleinvoice, logrecords;
    private DatabaseReference du;
    String s;
    private FirebaseAuth mAuth;
    TextView tx;
    String uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    view=inflater.inflate(R.layout.fragment_summary__fragement,container,false);
    getActivity().setTitle("summary");
        MainActivity.flag=1;

        sampleinvoice=view.findViewById(R.id.sample);
        logrecords=view.findViewById(R.id.log);
        tx=view.findViewById(R.id.title_summary);

        mAuth = Firebase_factory.getFirebaseAuth_Instance();
        uid =Firebase_factory.getfbUserId();
        du = Firebase_factory.getdatabaseRef().child("Users").child(uid);
        du.keepSynced(true);
          return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.floatingbtn.hide();

        sampleinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Sample_Bill_Activity.class);
                startActivity(i);

            }
        });

        logrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),Log_Activity.class);
                startActivity(i);
            }
        });

        du.child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                String storename=snapshot.child("storename").getValue(String.class);
                tx.setText(storename);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        du.child("Summary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("amount")){
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("ddMMyy");
                    SimpleDateFormat fm = new SimpleDateFormat("MM");

                    String date = (String) snapshot.child("date").getValue();
                    String month = (String) snapshot.child("month").getValue();

                    if (!format.format(c.getTime()).equals(date)) {
                        du.child("date").setValue(format.format(c.getTime()));
                        du.child("monthyCount").setValue("0");
                        du.child("todaysAmount").setValue("0.0");
                        ((TextView) (view.findViewById(R.id.todayamt))).setText("0.0");
                        ((TextView) (view.findViewById(R.id.todaycount))).setText("0");
                    } else if (!fm.format(c.getTime()).equals(month)) {
                        du.child("month").setValue(fm.format(c.getTime()));
                        du.child("monthlycount").setValue("0");
                        du.child("monthlyamount").setValue("0.0");
                        ((TextView) (view.findViewById(R.id.mcount))).setText("0");
                        ((TextView) (view.findViewById(R.id.mamt))).setText("0.0");
                    } else {
                        ((TextView) (view.findViewById(R.id.todayamt))).setText(snapshot.child("amount").getValue(String.class));
                        ((TextView) (view.findViewById(R.id.todaycount))).setText(snapshot.child("count").getValue(String.class));
                        ((TextView) (view.findViewById(R.id.mcount))).setText(snapshot.child("monthlyCount").getValue(String.class));
                        ((TextView) (view.findViewById(R.id.mamt))).setText(snapshot.child("monthlyAmount").getValue(String.class));
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}