package com.example.pharmacure.Bill.taxBill;

import static java.lang.Thread.sleep;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Transactions.newTransaction.TransactionActivity;
import com.example.pharmacure.Transactions.transHistory.MyAdapterTransactions;
import com.example.pharmacure.Utils.Utility;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_Bill;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class taxBill_Activity extends AppCompatActivity implements DatatransferInterface{

    TextView shopname, shopaddress, patientname, docname, billtype, area,
            gstno, billno, datetimetxt, billdetail, mobileno,totalquantity
            ,discvaluetxt;

    NestedScrollView nestedpdview;
    LinearLayout llpdfmain;
    DatabaseReference dbinfo, di;
    String uid;
     String selectedbillID = "";
     static String rate="";
    ListView taxbilllist;

    taxbillAdapter adapter;
    double finaldiscvalue=0.0;

     ArrayList<String> quantityal=new ArrayList<>();
    static  String quantity="";
    int totalquantoty=0;
    String discperc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_taxbill);
        getSupportActionBar().hide();

        shopname=findViewById(R.id.shopnametaxbill);
        shopaddress=findViewById(R.id.shopaddtaxbill);
        mobileno=findViewById(R.id.mobnotaxbill);
        billtype=findViewById(R.id.billtypetaxbill);
        area=findViewById(R.id.areataxbill);
        gstno=findViewById(R.id.gstnotaxbill);
        billno=findViewById(R.id.billtypetaxbill);
        datetimetxt=findViewById(R.id.datetimetaxbill);
        billdetail=findViewById(R.id.billdetailtaxbill);
        patientname=findViewById(R.id.ptnametaxbill);
        docname=findViewById(R.id.docnametaxbill);
        totalquantity=findViewById(R.id.totalqty);
        discvaluetxt=findViewById(R.id.discvalue);

        uid = Firebase_factory.getfbUserId();

        taxbilllist = findViewById(R.id.list_billtaxbill);
        discperc= getIntent().getStringExtra("discper");
        Toast.makeText(getApplicationContext(), MyAdapterTransactions.discper,Toast.LENGTH_SHORT).show();
        Log.d("recieved disc perc",discperc+"");
        selectedbillID = getIntent().getStringExtra("billID");
        if (selectedbillID.isEmpty()) {
            selectedbillID = TransactionActivity.generatedbillid;
        }

        Firebase_factory_Bill billfb = new Firebase_factory_Bill();
        List<Map<String,String>> maps=new ArrayList<>();
        ArrayList<BillModel> billModels=new ArrayList<>();

        adapter = new taxbillAdapter(taxBill_Activity.this, R.layout.listitems_tax_bill, billModels,this);
        taxbilllist.setAdapter(adapter);


        String billId = TransactionActivity.generatedbillid;
        Log.d("Array Size", String.valueOf(billfb.retrivechild_billItems(selectedbillID + "").size()));

        String uid = Firebase_factory.getfbUserId();


        DatabaseReference dbs = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales")
                .child("salesBill")
                .child("billList").child(selectedbillID + "").child("billItemsList");
        dbs.keepSynced(true);


        dbs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {


                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                    };
                    Map<String, String> map = ds.getValue(genericTypeIndicator);

                    String productname = map.get("productName");
                    String companyname =map.get("companyName");
                    String gst =map.get("gst%");
                     String rate =map.get("rate");
                     taxBill_Activity.rate=rate;

                    String mrp =map.get("mrp");
                    String looseqty =map.get("looseQuantity");
                    String expiry =map.get("expiryDate");
                    String totalamount =map.get("totalamount");
                    String batchno =map.get("batchNo");
                    String packaging =map.get("packaging");
                    String quantity =map.get("quantity");
                    quantityal.add(quantity);
                    String hsncode=map.get("hsncode");
                    String discperc=map.get("discperc");

                    BillModel model=new BillModel(productname,rate,quantity,batchno,expiry,packaging,gst,looseqty,mrp,totalamount,companyname,hsncode,discperc);
                    billModels.add(model);
                    //adapter.add(model);
                    //adapter.addAll(billModels);
                    adapter.notifyDataSetChanged();


                    //maps.add(productname,companyname,gst,rate,mrp,looseqty,expiry,totalamount,batchno,packaging,quantity);

                    //Log.d("batch",batchno);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("final size", maps.size() + "");






        dbinfo= Firebase_factory.getdatabaseRef().child("Users").child(uid).child("info");
        di=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales")
                .child("salesBill")
                .child("billList").child(selectedbillID);

        dbinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shopname.setText(snapshot.child("storename").getValue(String.class));
                shopaddress.setText(snapshot.child("addr").getValue(String.class));
                mobileno.setText(snapshot.child("ph1").getValue(String.class)+","+snapshot.child("ph2").getValue(String.class));
                gstno.setText(snapshot.child("gst").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        di.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billno.setText("Bill ID/No:"+snapshot.child("bill_ID").getValue(String.class
                ));
                docname.setText(snapshot.child("doctorName").getValue(String.class));
                datetimetxt.setText("Date/time"+snapshot.child("Date").getValue(String.class));
                patientname.setText(snapshot.child("patientName").getValue(String.class)+"(Age:"+snapshot.child("patientAge").getValue(String.class)+")");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.notifyDataSetChanged();

        for (int i = 0; i < quantityal.size(); i++) {
            totalquantoty=totalquantoty+ Integer.parseInt(quantityal.get(i));
        }
        totalquantity.setText(String.valueOf(totalquantoty));

        for (int i = 0; i < billModels.size(); i++) {
            totalquantoty=totalquantoty+Integer.parseInt(billModels.get(i).getQuantity());
        }
        totalquantity.setText(String.valueOf(totalquantoty));

    }


    @Override
    public void onsetvalues(double discvalue) {
        this.finaldiscvalue=discvalue;

        discvaluetxt.setText(String.valueOf(Utility.round(finaldiscvalue,2)));

    }
}


