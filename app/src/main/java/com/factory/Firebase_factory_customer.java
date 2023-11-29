package com.factory;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Firebase_factory_customer {

    static long lastCustomerID = 0;
    static String  customerID="";

    public static void addCustomer(String customerID,String name,String address,String mobileno,String balance,String dateadded){
        String generatedCustomerID=getcustomerid();
        String uid=Firebase_factory.getfbUserId();
        DatabaseReference db=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Customer").child("customerList").child(generatedCustomerID);
        db.keepSynced(true);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    db.child("customerID").setValue(customerID);
                    db.child("customerName").setValue(name);
                    db.child("customerAddress").setValue(address);
                    db.child("mobileNo").setValue(mobileno);
                    db.child("balance").setValue(balance);
                    db.child("Date_Added").setValue(dateadded);
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
        String message=name+" Added Successfully";

        //adding Log
        DatabaseReference dblog=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Log");
        dblog.keepSynced(true);
        dblog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dblog.child("log "+todaysdate+" "+time).setValue(message.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static String getcustomerid(){
        String uid = Firebase_factory.getfbUserId();

        DatabaseReference customerListRef = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Customer").child("customerList");
        customerListRef.keepSynced(true);

        customerListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    lastCustomerID=snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        customerID=String.valueOf(lastCustomerID+1);
        System.out.println(customerID);
        return customerID;

    }
}
