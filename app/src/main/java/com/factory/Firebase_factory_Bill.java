package com.factory;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pharmacure.Model.BillModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Firebase_factory_Bill {

    static long lastBillID=0;
    static String salesbillID="";

    static  int noofitems=0;

    static String productname="";
    static String rate="";
    static String quantity="";
    static String companyname="";
    static String batchno="";
    static String expiry="";
    static String packaging="";
    static String mrp="";
    static  String looseqty="";
    static String gst="";
    static String totalamount="";
    static String discperc="";
    static String hsncode="";
    BillModel model;




     ArrayList<BillModel> billitemlist=new ArrayList<>();



    public static void addsales_Bill(String billId, ArrayList<BillModel> billitemslist,String totalamount,String date,String doctorname,String patientname,String patientage){
        String uid=Firebase_factory.getfbUserId();
        //String generatedsalesBillID=Firebase_factory_Bill.getBillID();
        System.out.println("Bill List Model Size:"+billitemslist.size());
        DatabaseReference addsalesref = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales").child("salesBill").child("billList").child(billId);

        addsalesref.keepSynced(true);

        HashMap<String, Object> billdata=new HashMap<>();
        billdata.put("bill_ID",billId);
        billdata.put("Date", date);
        billdata.put("patientName", patientname);
        billdata.put("patientAge", patientage);
        billdata.put("doctorName", doctorname);
        billdata.put("total_bill_amount",totalamount);
        addsalesref.setValue(billdata);


        for (int i = 0; i < billitemslist.size(); i++) {
            BillModel model=billitemslist.get(i);
            HashMap<String,Object> billitemdata=new HashMap<>();
            billitemdata.put("productName",model.getProductName());
            billitemdata.put("companyName",model.getCompanyName());
            billitemdata.put("batchNo",model.getBatch());
            billitemdata.put("expiryDate",model.getExpirydate());
            billitemdata.put("packaging",model.getPackaging());
            billitemdata.put("mrp",model.getMrp());
            billitemdata.put("quantity",model.getQuantity());
            billitemdata.put("looseQuantity",model.getLooseqty());
            billitemdata.put("rate",model.getRate());
            billitemdata.put("gst%",model.getGst());
            billitemdata.put("hsncode",model.getHsnocode());
            billitemdata.put("discperc",model.getDiscperc());
            billitemdata.put("totalamount",model.getTotalAmount());

            String itemNo=String.valueOf(i+1);
            DatabaseReference billItemref=addsalesref.child("billItemsList").child(itemNo);
            billItemref.setValue(billitemdata).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("Firebase","Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase","Failure "+e.getMessage());
                }
            });




        }


    }


    public static String getBillID(){
        String uid = Firebase_factory.getfbUserId();

        DatabaseReference salesBillref = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales").child("salesBill").child("billList");
        //salesBillref.getDatabase().setPersistenceEnabled(true);
        salesBillref.keepSynced(true);
        salesBillref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    lastBillID=snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            salesbillID=String.valueOf(lastBillID+1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return salesbillID;

    }




    public ArrayList<BillModel> retrivechild_billItems(String billid){
        String uid=Firebase_factory.getfbUserId();



        DatabaseReference   dbs=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales")
                .child("salesBill")
                .child("billList").child(billid).child("billItemsList");
        dbs.keepSynced(true);

        dbs.addChildEventListener(new ChildEventListener() {
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

        return billitemlist;
    }

    public  void recievedata(DataSnapshot snapshot){
        Iterator it=snapshot.getChildren().iterator();


        while (it.hasNext()){

            batchno=((String) ((DataSnapshot) it.next()).getValue());
            companyname=((String) ((DataSnapshot) it.next()).getValue());
            discperc=((String) ((DataSnapshot) it.next()).getValue());
            expiry=((String) ((DataSnapshot) it.next()).getValue());
            gst=((String) ((DataSnapshot) it.next()).getValue());
            hsncode=((String) ((DataSnapshot) it.next()).getValue());
            looseqty=((String) ((DataSnapshot) it.next()).getValue());
            mrp=((String) ((DataSnapshot) it.next()).getValue());
            packaging=((String) ((DataSnapshot) it.next()).getValue());
            productname=((String) ((DataSnapshot) it.next()).getValue());
            quantity=((String) ((DataSnapshot) it.next()).getValue());
            rate=((String) ((DataSnapshot) it.next()).getValue());
            totalamount=((String) ((DataSnapshot) it.next()).getValue());

            BillModel model=new BillModel(productname,rate,quantity,batchno,expiry,packaging,gst,looseqty,mrp,totalamount,companyname,hsncode,discperc);
            billitemlist.add(model);



        }
    }
}
