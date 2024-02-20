package com.example.pharmacure.Summary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log_Activity extends AppCompatActivity {

    String uid;
    DatabaseReference db;
    private FirebaseAuth mAuth;
    TextView tx;
    ValueEventListener viewlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        tx = (TextView) findViewById(R.id.textView_log);
        mAuth = Firebase_factory.getFirebaseAuth_Instance();
        uid = Firebase_factory.getfbUserId();
        db = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Log");
        //db.keepSynced(true);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date date=new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm");
                String todaysdate= format.format(date);
                String time=timeformat.format(date);
                String logentry="log "+todaysdate+" "+time;
                tx.setText(snapshot.child(logentry).getValue(String.class));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(viewlistener);

    }
}