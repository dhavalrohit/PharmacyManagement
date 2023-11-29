package com.factory;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Transactions.TransactionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Firebase_factory_Bill {

    static long lastBillID=0;
    static String salesbillID="";

    static  int noofitems=0;



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
            return salesbillID;

    }
}
