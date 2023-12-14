package com.factory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pharmacure.Model.DoctorModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Firebase_factory_doctor {

    static String doctorname="";
    static String regno="";
    static String locality="";
    static String spec="";
    static String mobileno="";
    static String doctorid="";

    static long lastdoctorid=0;

    ArrayList<DoctorModel> doctorslist=new ArrayList<>();

    public void recievedata(DataSnapshot snapshot) {
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
    }


    public static void addDoctor(String docname,String docid,String doclocality,String docmobileno,String docregno,String docspec){
        String uid=Firebase_factory.getfbUserId();
        String generateddocid=getdoctorid();
        DatabaseReference dbd=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Doctors").child("doctorsList").child(generateddocid);
        dbd.keepSynced(true);

        dbd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbd.child("doctorID").setValue(generateddocid);
                dbd.child("doctorName").setValue(docname);
                dbd.child("locality").setValue(doclocality);
                dbd.child("mobileno").setValue(docmobileno);
                dbd.child("regno").setValue(docregno);
                dbd.child("specialisation").setValue(docspec);

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
        String message=docname+" Added Successfully";

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

    public static String getdoctorid(){
        String uid = Firebase_factory.getfbUserId();
        DatabaseReference doctorsListref = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Doctors").child("doctorsList");
        doctorsListref.keepSynced(true);

        doctorsListref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    lastdoctorid=snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        doctorid=String.valueOf(lastdoctorid+1);
        System.out.println(doctorid);
        return doctorid;

    }
}
