package com.example.pharmacure.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pharmacure.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Updateinfo_activity extends AppCompatActivity {

    TextInputLayout txname, txstorename, txtin, txaddr, txph1, txph2, txmore, txgst;
    private EditText name,sname,gst,addr,tin,phone1,phonewhatsapp,addinfo;
    FirebaseAuth mauth;
    String uid;

    Button updatebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);
        name=findViewById(R.id.input_name);
        sname=findViewById(R.id.input_store_name);
        gst=findViewById(R.id.input_gst);
        addr=findViewById(R.id.input_addr);
        tin=findViewById(R.id.input_tin);
        phone1=findViewById(R.id.input_ph1);
        phonewhatsapp=findViewById(R.id.input_ph2);
        addinfo=findViewById(R.id.input_more);

        updatebtn=findViewById(R.id.updatebtn);


        getdetails();
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    update_details();
                    Toast.makeText(getBaseContext(),"Information Updated Successfully",Toast.LENGTH_SHORT);

                    finish();
            }
        });




        
    }

    public void update_details(){

        name=findViewById(R.id.input_name);
        sname=findViewById(R.id.input_store_name);
        gst=findViewById(R.id.input_gst);
        addr=findViewById(R.id.input_addr);
        tin=findViewById(R.id.input_tin);
        phone1=findViewById(R.id.input_ph1);
        phonewhatsapp=findViewById(R.id.input_ph2);
        addinfo=findViewById(R.id.input_more);


        mauth=FirebaseAuth.getInstance();
         uid=mauth.getCurrentUser().getUid();
         DatabaseReference dblog=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Log");
         DatabaseReference dbinfo=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("info");
         dbinfo.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 dbinfo.child("storename").setValue(sname.getText().toString());
                 dbinfo.child("name").setValue(name.getText().toString());
                 dbinfo.child("addr").setValue(addr.getText().toString());
                 dbinfo.child("gst").setValue(gst.getText().toString());
                 dbinfo.child("more").setValue(addinfo.getText().toString());
                 dbinfo.child("tin").setValue(tin.getText().toString());
                 dbinfo.child("ph1").setValue(phone1.getText().toString());
                 dbinfo.child("ph2").setValue(phonewhatsapp.getText().toString());



             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });



        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm");
        String todaysdate= format.format(date);
        String time=timeformat.format(date);
        String message="Shop Information Updated";

         dblog.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 dblog.child("log "+todaysdate+" "+time).setValue(message.toString());
                 Toast.makeText(getBaseContext(),"Log Updated Successfully",Toast.LENGTH_SHORT);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });


    }

    public void getdetails(){
         mauth=FirebaseAuth.getInstance();
         uid=mauth.getCurrentUser().getUid();

        name=findViewById(R.id.input_name);
        sname=findViewById(R.id.input_store_name);
        gst=findViewById(R.id.input_gst);
        addr=findViewById(R.id.input_addr);
        tin=findViewById(R.id.input_tin);
        phone1=findViewById(R.id.input_ph1);
        phonewhatsapp=findViewById(R.id.input_ph2);
        addinfo=findViewById(R.id.input_more);


        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("info");
        db.keepSynced(true);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                sname.setText(snapshot.child("storename").getValue(String.class));
                addr.setText(snapshot.child("addr").getValue(String.class));
                gst.setText(snapshot.child("gst").getValue(String.class));
                phone1.setText(snapshot.child("ph1").getValue(String.class));
                phonewhatsapp.setText(snapshot.child("ph2").getValue(String.class));
                addinfo.setText(snapshot.child("more").getValue(String.class));
                tin.setText(snapshot.child("tin").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean validate() {
        int flag = 0;
        if (name.getText().toString().trim().isEmpty()) {
            flag = 1;
            name.setError("Cannot be Empty");
        } else
            txname.setErrorEnabled(false);
        if (sname.getText().toString().trim().isEmpty()) {
            flag = 1;
            txstorename.setError("Cannot be Empty");
        } else
            txstorename.setErrorEnabled(false);
        if (tin.getText().toString().trim().isEmpty()) {
            flag = 1;
            tin.setError("Cannot be Empty");
        } else
            txtin.setErrorEnabled(false);
        if (addr.getText().toString().trim().isEmpty()) {
            flag = 1;
            txaddr.setError("Cannot be Empty");
        } else
            txaddr.setErrorEnabled(false);
        if (phone1.getText().toString().trim().isEmpty()) {
            flag = 1;
            txph1.setError("Cannot be Empty");
        } else
            txph1.setErrorEnabled(false);
        if (phonewhatsapp.getText().toString().trim().isEmpty()) {
            flag = 1;
            txph2.setError("Cannot be Empty");
        } else
            txph2.setErrorEnabled(false);
        if (addinfo.getText().toString().trim().isEmpty()) {
            addinfo.setText("-");
        }
        if (gst.getText().toString().trim().isEmpty()) {
            flag = 1;
            txgst.setError("Cannot be Empty");
        } else
            txgst.setErrorEnabled(false);
        if (flag == 1)
            return false;
        else
            return true;
    }

}