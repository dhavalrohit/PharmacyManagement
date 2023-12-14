package com.example.pharmacure.Bill.salesBill;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Transactions.newTransaction.TransactionActivity;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_Bill;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Bill_Activity extends AppCompatActivity {
    TextView ph1, ph2, storename, storeaddr, billID, dlnum, date, gstNo, patientname, patientage, doctorname, totalamountt, name;
    private DatabaseReference db;
    private DatabaseReference di;
    private DatabaseReference dbs;
    private FirebaseAuth mAuth;

    ListView listviewbill;
    String uid;

    String selectedbillID="";
    BillPrintAdapter adapter;

    String productname="";
    String rate="";
    String quantity="";
    String companyname="";
    String batchno="";
    String expiry="";
    String packaging="";
    String mrp="";
    String looseqty="";
    String gst="";
    String totalamount="";
    BillModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.invoice);
        getSupportActionBar().hide();
        selectedbillID=getIntent().getStringExtra("billID");
        if (selectedbillID.isEmpty()){
            selectedbillID= TransactionActivity.generatedbillid;
        }

        Firebase_factory_Bill billfb=new Firebase_factory_Bill();

        ph1 = (TextView) findViewById(R.id.ph1);
        ph2 = (TextView) findViewById(R.id.ph2);
        storename = (TextView) findViewById(R.id.storename);
        storeaddr = (TextView) findViewById(R.id.storeaddr);
        billID = (TextView) findViewById(R.id.transid);
        dlnum = (TextView) findViewById(R.id.dlnum);
        date = (TextView) findViewById(R.id.date);
        gstNo = (TextView) findViewById(R.id.gst);
        patientname = (TextView) findViewById(R.id.ptname);
        patientage = (TextView) findViewById(R.id.age);
        doctorname = (TextView) findViewById(R.id.docname);
        totalamountt = (TextView) findViewById(R.id.totamt);

        listviewbill=findViewById(R.id.list_bill);


        String billId=TransactionActivity.generatedbillid;
        adapter=new BillPrintAdapter(Bill_Activity.this,R.layout.listitems_bill,billfb.retrivechild_billItems(selectedbillID+""));
        Log.d("Array Size", String.valueOf(billfb.retrivechild_billItems(selectedbillID+"").size()));
        listviewbill.setAdapter(adapter);

        uid=Firebase_factory.getfbUserId();

        dbs=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales")
                .child("salesBill")
                .child("billList").child(selectedbillID).child("billItemsList");
        dbs.keepSynced(true);
        db=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("info");

        di=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales")
                        .child("salesBill")
                                .child("billList").child(selectedbillID);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storename.setText(snapshot.child("storename").getValue(String.class));
                storeaddr.setText(snapshot.child("addr").getValue(String.class));
                ph1.setText(snapshot.child("ph1").getValue(String.class));
                ph2.setText(snapshot.child("ph2").getValue(String.class));
                dlnum.setText(snapshot.child("tin").getValue(String.class));
                gstNo.setText(snapshot.child("gst").getValue(String.class));

             }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

   /*    dbs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){


                        productname=ds.child("productName").getValue(String.class);
                         rate=ds.child("rate").getValue(String.class);
                         quantity=ds.child("quantity").getValue(String.class);
                         companyname=ds.child("companyName").getValue(String.class);
                         batchno=ds.child("batchNo").getValue(String.class);
                         expiry=ds.child("expiryDate").getValue(String.class);
                         packaging=ds.child("packaging").getValue(String.class);
                         mrp=ds.child("mrp").getValue(String.class);
                         looseqty=ds.child("looseQuantity").getValue(String.class);
                         gst=ds.child("gst%").getValue(String.class);
                         totalamount=ds.child("totalamount").getValue(String.class);

                        model=new BillModel(productname,rate,quantity,batchno,expiry,packaging,gst,looseqty,mrp,totalamount,companyname);

                        billitemslist.add(model);

                        adapter.add(model);



                        //adapter.notifyDataSetChanged();

                    }



                    //adapter.clear();
                   // adapter.addAll(billitemslist);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


     /*  dbs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //billitemslist.clear();
                billitemslist.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    String productname=ds.child("productName").getValue(String.class);
                    String rate=ds.child("rate").getValue(String.class);
                    String quantity=ds.child("quantity").getValue(String.class);
                    String companyName=ds.child("companyName").getValue(String.class);
                    String batchNo=ds.child("batchNo").getValue(String.class);
                    String expiryDate=ds.child("expiryDate").getValue(String.class);
                    String packaging=ds.child("packaging").getValue(String.class);
                    String mrp=ds.child("mrp").getValue(String.class);
                    String looseQuantity=ds.child("looseQuantity").getValue(String.class);
                    String gst=ds.child("gst%").getValue(String.class);
                    String totalamount=ds.child("totalamount").getValue(String.class);



                    BillModel model=new BillModel(productname,rate,quantity+" Strip",batchNo,expiryDate,"packaging:"+packaging,gst,looseQuantity+" Loose",mrp,totalamount,companyName);

                    billitemslist.add(model);
                    adapter.notifyDataSetChanged();

                }

                Toast.makeText(getApplicationContext(),"Size:"+billitemslist.size(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        di.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billID.setText(snapshot.child("bill_ID").getValue(String.class
                ));
                doctorname.setText(snapshot.child("doctorName").getValue(String.class));
                date.setText(snapshot.child("Date").getValue(String.class));
                patientname.setText(snapshot.child("patientName").getValue(String.class));
                patientage.setText(snapshot.child("patientAge").getValue(String.class));
                totalamountt.setText(snapshot.child("total_bill_amount").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listviewbill.setAdapter(adapter);


    }


}