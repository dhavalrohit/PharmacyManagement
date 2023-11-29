package com.example.pharmacure.Summary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Sample_Bill_Activity extends AppCompatActivity {

    TextView ph1, ph2, storename, storeaddr, transid, dlnum, date, gst, patienttname, age, doctorname, totamt, name;
    private DatabaseReference du;
    private FirebaseAuth mAuth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.invoice);
        getSupportActionBar().hide();

        ph1 = (TextView) findViewById(R.id.ph1);
        ph2 = (TextView) findViewById(R.id.ph2);
        storename = (TextView) findViewById(R.id.storename);
        storeaddr = (TextView) findViewById(R.id.storeaddr);
        transid = (TextView) findViewById(R.id.transid);
        dlnum = (TextView) findViewById(R.id.dlnum);
        date = (TextView) findViewById(R.id.date);
        gst = (TextView) findViewById(R.id.gst);
        patienttname = (TextView) findViewById(R.id.ptname);
        age = (TextView) findViewById(R.id.age);
        doctorname = (TextView) findViewById(R.id.docname);
        totamt = (TextView) findViewById(R.id.totamt);
        name = (TextView) findViewById(R.id.name);

        mAuth = Firebase_factory.getFirebaseAuth_Instance();
        uid = Firebase_factory.getfbUserId();
        du = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("info");
        du.keepSynced(true);

        du.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                storename.setText(snapshot.child("storename").getValue(String.class));
                dlnum.setText(snapshot.child("tin").getValue(String.class));
                storeaddr.setText(snapshot.child("addr").getValue(String.class));
                ph1.setText("Ph: " + snapshot.child("ph1").getValue(String.class));
                ph2.setText("Ph: " + snapshot.child("ph2").getValue(String.class));
                gst.setText(snapshot.child("gst").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}