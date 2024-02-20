package com.example.pharmacure.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Customer_Log_Activity extends AppCompatActivity {
    String uid;
    DatabaseReference db;
    private FirebaseAuth mAuth;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_log);
        Intent i = getIntent();
        String k = i.getStringExtra("k");
        t = (TextView) findViewById(R.id.textView);
        mAuth= Firebase_factory.getFirebaseAuth_Instance();
        uid=Firebase_factory.getfbUserId();
        db = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Customer").child("Customers")
                .child(k);

        //db.keepSynced(true);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                };
                final Map<String, String> map = snapshot.getValue(genericTypeIndicator);
                setTitle(map.get("customerName"));

                t.setText(map.get("log"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}