package com.example.pharmacure.Bill.taxBill;

import static java.lang.Thread.sleep;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.itextpdf.*;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Transactions.newTransaction.TransactionActivity;
import com.example.pharmacure.Transactions.transHistory.MyAdapterTransactions;
import com.example.pharmacure.Utils.PDfDocumentAdapter;
import com.example.pharmacure.Utils.Utility;
import com.example.pharmacure.Utils.printing.PDFDocumentAdapter;
import com.example.pharmacure.Utils.printing.PrintJobMonitorService;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_Bill;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.sql.CommonDataSource;


public class taxBill_Activity extends AppCompatActivity implements DatatransferInterface {
    private ProgressDialog progressDialog;
    private PdfGenerator.XmlToPDFLifecycleObserver xmlToPDFLifecycleObserver;
    TextView shopname, shopaddress, patientname, docname, billtype, area,
            gstno, billno, datetimetxt, billdetail, mobileno,
            totalquantity, discvaluetxt, totaltaxablevalueview, totalsgstvalueview,
            totalcgstvalueview, totalbillamountview;

    LinearLayout billdatalayout,footerlayout,firstheaderlayout,secondheaderlayout;
    Button generatepdfbtn, backbtn,printpdf,nextpage,previouspage;

    DatatransferInterface datatransferInterface;
    DatabaseReference dbinfo, di;
    String uid;
    private PrintManager mgr=null;
    private int originalorientation;
    int totalsecond=0;
    String selectedbillID = "";
    static String rate = "";
    ListView taxbilllist;
    taxbillAdapter generaladapter,firstpageadapter,lastpageadapter,middlepageadapter;
    double finaldiscvalue = 0.0;
    double totaltaxablevalue = 0.0;
    double totalsgstvalue = 0.0;
    double totalcgstvalue = 0.0;
    double totalbillamount = 0.0;
    int totalitems=0;

    ArrayList<String> quantityal = new ArrayList<>();

    int totalquantoty = 0;
    String discperc = "";
    int totalmiddlepages=0;


    int fromindex=0;
    private Thread predecessor;


    DatabaseReference dbs;

    ArrayList<BillModel> firstpageitems,remaningitems;
    ArrayList<BillModel> totalbillitems=new ArrayList<>();
    ArrayList<ArrayList<BillModel>> middlepageitems=new ArrayList<>();
    ArrayList<BillModel> lastpageitems=new ArrayList<>();


    taxBill_Activity activity;

    int pagecount=0;
    ProgressBar pb;
    ProgressDialog pgd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_taxbill);
        getSupportActionBar().hide();

        datatransferInterface=this;
        shopname = findViewById(R.id.shopnametaxbill);
        shopaddress = findViewById(R.id.shopaddtaxbill);
        mobileno = findViewById(R.id.mobnotaxbill);
        billtype = findViewById(R.id.billtypetaxbill);
        area = findViewById(R.id.areataxbill);
        gstno = findViewById(R.id.gstnotaxbill);
        billno = findViewById(R.id.billnoidtaxbill);
        datetimetxt = findViewById(R.id.datetimetaxbill);
        billdetail = findViewById(R.id.billdetailtaxbill);
        patientname = findViewById(R.id.ptnametaxbill);
        docname = findViewById(R.id.docnametaxbill);
        totalquantity = findViewById(R.id.totalqty);
        discvaluetxt = findViewById(R.id.discvalue);
        totaltaxablevalueview = findViewById(R.id.totaltaxablevalue);
        totalcgstvalueview = findViewById(R.id.totalcgstvaluetext);
        totalsgstvalueview = findViewById(R.id.totalsgstvaluetext);
        totalbillamountview = findViewById(R.id.totalbillamountview);
        nextpage=findViewById(R.id.nextpagebtn);
        previouspage=findViewById(R.id.previouspagebtn);

        footerlayout=findViewById(R.id.billfooter);
        billdatalayout=findViewById(R.id.billdatalayout);
        firstheaderlayout=findViewById(R.id.firstheader);
        secondheaderlayout=findViewById(R.id.secondheader);

        generatepdfbtn = findViewById(R.id.generatepdfbtn);
        backbtn = findViewById(R.id.backbtn);
        printpdf=findViewById(R.id.printbtn);

         pgd=new ProgressDialog(this);
        pgd.setTitle("Loading");
        pgd.setMessage("Wait while loading...");
        pgd.setCancelable(false); // disable dismiss by tapping outside of the dialog



        uid = Firebase_factory.getfbUserId();

        activity=new taxBill_Activity();

        taxbilllist = findViewById(R.id.list_billtaxbill);
        discperc = getIntent().getStringExtra("discper");
        //Toast.makeText(getApplicationContext(), MyAdapterTransactions.discper, Toast.LENGTH_SHORT).show();
        Log.d("recieved disc perc", discperc + "");
        selectedbillID = getIntent().getStringExtra("billID");
        if (selectedbillID.isEmpty()) {
            selectedbillID = TransactionActivity.generatedbillid;
        }

        Firebase_factory_Bill billfb = new Firebase_factory_Bill();
        List<Map<String, String>> maps = new ArrayList<>();
        ArrayList<BillModel> billModels = new ArrayList<>();

        String billId = TransactionActivity.generatedbillid;
        Log.d("Array Size", String.valueOf(billfb.retrivechild_billItems(selectedbillID + "").size()));

        String uid = Firebase_factory.getfbUserId();

        mgr=(PrintManager)getSystemService(PRINT_SERVICE);

        dbs = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales")
                .child("salesBill")
                .child("billList").child(selectedbillID + "").child("billItemsList");
        //dbs.keepSynced(true);

        dbs.addValueEventListener(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(@NonNull DataSnapshot snapshot) {
                totalitems= (int) snapshot.getChildrenCount();
                Log.d("totalbillitems",totalitems+"");

                if (totalitems>10){
                    display_multipagebill(snapshot);
                }

                if (totalitems>5 && totalitems<=10) {
                    displaytwopage_bill(snapshot);
                }
                if (totalitems<=5){
                    display_singlepagebill(snapshot);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //rough2
        //showbillitemsData(totalitems);

        dbinfo = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("info");
        di = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales")
                .child("salesBill")
                .child("billList").child(selectedbillID);

        dbinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shopname.setText(snapshot.child("storename").getValue(String.class));
                shopaddress.setText(snapshot.child("addr").getValue(String.class));
                mobileno.setText(snapshot.child("ph1").getValue(String.class) + "," + snapshot.child("ph2").getValue(String.class));
                gstno.setText("GST NO:" + snapshot.child("gst").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        di.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billno.setText("Bill ID/No:" + snapshot.child("bill_ID").getValue(String.class
                ));
                docname.setText(snapshot.child("doctorName").getValue(String.class));
                datetimetxt.setText("Date/time" + snapshot.child("Date").getValue(String.class));
                patientname.setText(snapshot.child("patientName").getValue(String.class) + "(Age:" + snapshot.child("patientAge").getValue(String.class) + ")");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        xmlToPDFLifecycleObserver = new PdfGenerator.XmlToPDFLifecycleObserver(this);
        getLifecycle().addObserver(xmlToPDFLifecycleObserver);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        generatepdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Generating PDF",Toast.LENGTH_SHORT).show();
                pgd.show();

                if (totalitems>5) {

                    create_multipagepdf();
                }
                else {

                    //createpdfm2();
                    createPDFFromLayout();
                }

                pgd.dismiss();

 }
        });

        printpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Loding..",Toast.LENGTH_SHORT).show();
                //createpdfm1();
                //printpdf();
                if (totalitems<=5){

                create_print_pdf_singlepage();
                }else {

                    create_print_pdf_multipage();
                }

            }
        });

        //rough1


        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalitems>5 && totalitems<=10){
                    Show_lastpagedata();
                    nextpage.setVisibility(View.GONE);
                    previouspage.setVisibility(View.VISIBLE);

                }
                else {
                    if (pagecount<totalmiddlepages){
                        Show_nextpagedata(pagecount);
                        pagecount=pagecount+1;
                        nextpage.setVisibility(View.VISIBLE);
                        previouspage.setVisibility(View.VISIBLE);
                    }
                    else {
                        //firstheaderlayout.setVisibility(View.GONE);
                        //secondheaderlayout.setVisibility(View.GONE);
                        footerlayout.setVisibility(View.VISIBLE);

                        lastpageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,lastpageitems,datatransferInterface,fromindex,totalitems);
                        taxbilllist.setAdapter(lastpageadapter);
                        //lastpageadapter.setCount(lastpageitems.size());
                        //updatetotalquantity(firstpageadapter.gettotalquantity());
                        lastpageadapter.notifyDataSetChanged();
                        //Toast.makeText(getApplicationContext(),"last page adapter used",Toast.LENGTH_SHORT).show();
                        //updatetotalquantity(lastpageadapter.gettotalquantity());

                        nextpage.setVisibility(View.GONE);
                        previouspage.setVisibility(View.VISIBLE);
                    }
                }
      }
        });

        previouspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalitems>5 && totalitems<=10) {
                    show_previospagedata();
                    previouspage.setVisibility(View.GONE);
                    nextpage.setVisibility(View.VISIBLE);
                }
                else {


                        if(pagecount==0){
                            firstheaderlayout.setVisibility(View.VISIBLE);
                            secondheaderlayout.setVisibility(View.VISIBLE);
                            footerlayout.setVisibility(View.GONE);
                            previouspage.setVisibility(View.GONE);
                            firstpageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,firstpageitems,datatransferInterface);
                            taxbilllist.setAdapter(firstpageadapter);
                            //firstpageadapter.setCount(firstpageitems.size());
                            //updatetotalquantity(firstpageadapter.gettotalquantity());
                            //Toast.makeText(getApplicationContext(),"first page adapter used previous method",Toast.LENGTH_SHORT).show();
                            firstpageadapter.notifyDataSetChanged();
                            // updatetotalquantity(firstpageadapter.gettotalquantity());

                        }else {
                            pagecount=pagecount-1;
                            Show_nextpagedata(pagecount);
                            nextpage.setVisibility(View.VISIBLE);
                            previouspage.setVisibility(View.VISIBLE);

                        }



                    }
                if (pagecount>totalmiddlepages)
                {
                        firstheaderlayout.setVisibility(View.GONE);
                        secondheaderlayout.setVisibility(View.GONE);
                        footerlayout.setVisibility(View.VISIBLE);

                        lastpageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,lastpageitems,datatransferInterface,fromindex,totalitems);
                        taxbilllist.setAdapter(lastpageadapter);
                        //lastpageadapter.setCount(lastpageitems.size());
                        //updatetotalquantity(firstpageadapter.gettotalquantity());
                        lastpageadapter.notifyDataSetChanged();
                       // Toast.makeText(getApplicationContext(),"last page adapter used",Toast.LENGTH_SHORT).show();
                        //updatetotalquantity(lastpageadapter.gettotalquantity());

                        nextpage.setVisibility(View.GONE);
                        previouspage.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public synchronized void display_multipagebill(DataSnapshot snapshot){
        footerlayout.setVisibility(View.GONE);
        nextpage.setVisibility(View.VISIBLE);
        previouspage.setVisibility(View.GONE);
        nextpage.setVisibility(View.VISIBLE);

        LinearLayout layout = (LinearLayout) findViewById(R.id.billdatalayout);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();

        lp.height = 580;

        Log.d("data set method", "inside if condition");

        for (DataSnapshot ds:snapshot.getChildren()){
            //params.height=230;
            GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
            };
            Map<String, String> map = ds.getValue(genericTypeIndicator);

            String productname = map.get("productName");
            String companyname = map.get("companyName");
            String gst = map.get("gst%");
            String rate = map.get("rate");
            taxBill_Activity.rate = rate;
            String mrp = map.get("mrp");
            String looseqty = map.get("looseQuantity");
            String expiry = map.get("expiryDate");
            String totalamount = map.get("totalamount");
            String batchno = map.get("batchNo");
            String packaging = map.get("packaging");
            String quantity = map.get("quantity");
            quantityal.add(quantity);
            String hsncode = map.get("hsncode");
            String discperc = map.get("discperc");

            BillModel model = new BillModel(productname, rate, quantity, batchno, expiry, packaging, gst, looseqty, mrp, totalamount, companyname, hsncode, discperc);
            totalbillitems.add(model);
        }
        double itemsperpage=5.00;
        double totalpagescalc=(totalitems/itemsperpage)+(totalitems*(itemsperpage/100));
        int totalpages=Integer.valueOf((int) totalpagescalc);
        int firstpageitemscount=5;
        totalmiddlepages=totalpages-2;

        int totalmiddlepageitemscount=5*totalmiddlepages;
        fromindex=5;
        firstpageitems=new ArrayList<>(totalbillitems.subList(0,firstpageitemscount));

        for (int i=0;i<totalmiddlepages;i++){
            middlepageitems.add(new ArrayList<>(totalbillitems.subList(fromindex,fromindex+5)));
            fromindex=fromindex+5;
        }

        lastpageitems=new ArrayList<>(totalbillitems.subList(fromindex,totalitems));

        firstpageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,firstpageitems,datatransferInterface);

        taxbilllist.setAdapter(firstpageadapter);
        //Toast.makeText(getApplicationContext(),"first page adapter used",Toast.LENGTH_SHORT).show();
        //firstpageadapter.setcount(firstpageitems.size());
        firstpageadapter.notifyDataSetChanged();

    }
    public synchronized void displaytwopage_bill(DataSnapshot snapshot){
        footerlayout.setVisibility(View.GONE);
        nextpage.setVisibility(View.VISIBLE);
        previouspage.setVisibility(View.GONE);

        LinearLayout layout = (LinearLayout) findViewById(R.id.billdatalayout);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();

        lp.height = 580;

        Log.d("data set method", "inside if condition");

        for (DataSnapshot ds:snapshot.getChildren()){
            //params.height=230;
            GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
            };
            Map<String, String> map = ds.getValue(genericTypeIndicator);

            String productname = map.get("productName");
            String companyname = map.get("companyName");
            String gst = map.get("gst%");
            String rate = map.get("rate");
            taxBill_Activity.rate = rate;
            String mrp = map.get("mrp");
            String looseqty = map.get("looseQuantity");
            String expiry = map.get("expiryDate");
            String totalamount = map.get("totalamount");
            String batchno = map.get("batchNo");
            String packaging = map.get("packaging");
            String quantity = map.get("quantity");
            quantityal.add(quantity);
            String hsncode = map.get("hsncode");
            String discperc = map.get("discperc");

            BillModel model = new BillModel(productname, rate, quantity, batchno, expiry, packaging, gst, looseqty, mrp, totalamount, companyname, hsncode, discperc);
            totalbillitems.add(model);

        }

        firstpageitems=new ArrayList<>(totalbillitems.subList(0,5));
        lastpageitems=new ArrayList<>(totalbillitems.subList(5,totalitems));

        firstpageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,totalbillitems,datatransferInterface);

        taxbilllist.setAdapter(firstpageadapter);
        //Toast.makeText(getApplicationContext(),"first page adapter used",Toast.LENGTH_SHORT).show();
        //firstpageadapter.setcount(firstpageitems.size());
        firstpageadapter.notifyDataSetChanged();



    }
    public synchronized void display_singlepagebill(DataSnapshot snapshot){
        nextpage.setVisibility(View.GONE);
        previouspage.setVisibility(View.GONE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.billdatalayout);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();

        lp.height = 580;

        for (DataSnapshot ds:snapshot.getChildren()){

            //params.height=230;
            GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
            };
            Map<String, String> map = ds.getValue(genericTypeIndicator);

            String productname = map.get("productName");
            String companyname = map.get("companyName");
            String gst = map.get("gst%");
            String rate = map.get("rate");
            taxBill_Activity.rate = rate;

            String mrp = map.get("mrp");
            String looseqty = map.get("looseQuantity");
            String expiry = map.get("expiryDate");
            String totalamount = map.get("totalamount");
            String batchno = map.get("batchNo");
            String packaging = map.get("packaging");
            String quantity = map.get("quantity");
            quantityal.add(quantity);
            String hsncode = map.get("hsncode");
            String discperc = map.get("discperc");
            BillModel model = new BillModel(productname, rate, quantity, batchno, expiry, packaging, gst, looseqty, mrp, totalamount, companyname, hsncode, discperc);
            totalbillitems.add(model);

        }
        generaladapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,totalbillitems,datatransferInterface);
        taxbilllist.setAdapter(generaladapter);
        //generaladapter.addAll(totalbillitems);
        //generaladapter.setCount(totalbillitems.size());

        //Toast.makeText(getApplicationContext(),"general adapter used",Toast.LENGTH_SHORT).show();
        generaladapter.notifyDataSetChanged();


    }

    public synchronized void showmiddlepagedata(){
        if (pagecount < totalmiddlepages) {
            middlepageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,middlepageitems.get(pagecount),this,firstpageitems.size(),middlepageitems.get(pagecount).size()+5);
            taxbilllist.setAdapter(middlepageadapter);
            //lastpageadapter.setCount(lastpageitems.size());
            //updatetotalquantity(firstpageadapter.gettotalquantity());
            middlepageadapter.notifyDataSetChanged();
            //Toast.makeText(getApplicationContext(),"middle page adapter used",Toast.LENGTH_SHORT).show();
            //updatetotalquantity(lastpageadapter.gettotalquantity());


            pagecount = pagecount + 1;
            nextpage.setVisibility(View.VISIBLE);
            previouspage.setVisibility(View.VISIBLE);
        } else {
            //firstheaderlayout.setVisibility(View.GONE);
            //secondheaderlayout.setVisibility(View.GONE);
            footerlayout.setVisibility(View.VISIBLE);

            lastpageadapter = new taxbillAdapter(taxBill_Activity.this, R.layout.listitems_tax_bill, lastpageitems, datatransferInterface, fromindex, totalitems);
            taxbilllist.setAdapter(lastpageadapter);

            //lastpageadapter.setCount(lastpageitems.size());
            //updatetotalquantity(firstpageadapter.gettotalquantity());
            lastpageadapter.notifyDataSetChanged();
           // Toast.makeText(getApplicationContext(), "last page adapter used", Toast.LENGTH_SHORT).show();
            //updatetotalquantity(lastpageadapter.gettotalquantity());

            nextpage.setVisibility(View.GONE);
            previouspage.setVisibility(View.VISIBLE);


        }
    }

    public synchronized void Show_lastpagedata(){
        //firstheaderlayout.setVisibility(View.GONE);
        //secondheaderlayout.setVisibility(View.GONE);
        footerlayout.setVisibility(View.VISIBLE);

        lastpageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,lastpageitems,this,firstpageitems.size(),lastpageitems.size());
        taxbilllist.setAdapter(lastpageadapter);
        //lastpageadapter.setCount(lastpageitems.size());
        //updatetotalquantity(firstpageadapter.gettotalquantity());
        lastpageadapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(),"last page adapter used",Toast.LENGTH_SHORT).show();
        //updatetotalquantity(lastpageadapter.gettotalquantity());


    }



    public synchronized void Show_nextpagedata(int totoalmiddlepages){
        //firstheaderlayout.setVisibility(View.GONE);
        //secondheaderlayout.setVisibility(View.GONE);

        middlepageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,middlepageitems.get(pagecount),this,firstpageitems.size(),middlepageitems.get(pagecount).size()+5);
        taxbilllist.setAdapter(middlepageadapter);
        //lastpageadapter.setCount(lastpageitems.size());
        //updatetotalquantity(firstpageadapter.gettotalquantity());
        middlepageadapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(),"middle page adapter used",Toast.LENGTH_SHORT).show();
        //updatetotalquantity(lastpageadapter.gettotalquantity());


    }




    public synchronized void show_previospagedata(){
        firstheaderlayout.setVisibility(View.VISIBLE);
        secondheaderlayout.setVisibility(View.VISIBLE);
        footerlayout.setVisibility(View.GONE);

        firstpageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,firstpageitems,this);
        taxbilllist.setAdapter(firstpageadapter);
        //firstpageadapter.setCount(firstpageitems.size());
        //updatetotalquantity(firstpageadapter.gettotalquantity());
        //Toast.makeText(getApplicationContext(),"first page adapter used previous method",Toast.LENGTH_SHORT).show();
        firstpageadapter.notifyDataSetChanged();
        // updatetotalquantity(firstpageadapter.gettotalquantity());
    }

    public synchronized void show_previospagedata(int totalmiddlepages){
        firstheaderlayout.setVisibility(View.VISIBLE);
        secondheaderlayout.setVisibility(View.VISIBLE);
        footerlayout.setVisibility(View.GONE);

        middlepageadapter=new taxbillAdapter(taxBill_Activity.this,R.layout.listitems_tax_bill,middlepageitems.get(pagecount),this,firstpageitems.size(),middlepageitems.get(pagecount).size()+5);
        taxbilllist.setAdapter(middlepageadapter);
        //firstpageadapter.setCount(firstpageitems.size());
        //updatetotalquantity(firstpageadapter.gettotalquantity());
        middlepageadapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(),"middle page adapter used",Toast.LENGTH_SHORT).show();
        // updatetotalquantity(firstpageadapter.gettotalquantity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void printPDFV2(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Document";
        PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
            @Override
            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                InputStream input = null;
                OutputStream output = null;

                try {
                    input = getContentResolver().openInputStream(fileUri);
                    output = new FileOutputStream(destination.getFileDescriptor());

                    byte[] buf = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buf)) > 0) {
                        output.write(buf, 0, bytesRead);
                    }

                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (input != null) {
                            input.close();
                        }
                        if (output != null) {
                            output.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }

                PrintDocumentInfo info = new PrintDocumentInfo.Builder("file_name.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .build();

                callback.onLayoutFinished(info, true);
            }
        };

        if (printManager != null) {
            PrintJob printJob = printManager.print(jobName, printAdapter, null);

            if (printJob == null) {
                // Printing failed
            }
        }
    }


    public void printpdf(String filePath) {

        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            //PrintDocumentAdapter printDocumentAdapter = new PDfDocumentAdapter(taxBill_Activity.this, filePath);
            PrintDocumentAdapter printDocumentAdapter = new PDfDocumentAdapter(taxBill_Activity.this, filePath);

            PrintAttributes printAttributes=new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4.asLandscape()).build();

            printManager.print("Document", printDocumentAdapter, printAttributes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }}
    //rough4




    public void create_print_pdf_multipage(){

        if (totalitems > 10) {
            showLoadingPopup();

            View view = findViewById(R.id.layouttopdfmain);

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "generated_pdf.pdf");

            PdfWriter writer = null;
            try {
                writer = new PdfWriter(new FileOutputStream(file));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }



            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

            Document document = new Document(pdfDocument);

            document.setLeftMargin(10);
            document.setRightMargin(80);

            //Executor executor=Executors.newSingleThreadExecutor();
            ScheduledExecutorService executorService=Executors.newSingleThreadScheduledExecutor();
            Runnable createfirstpaget1=new Runnable() {
                @Override
                public void run() {

                    create_itext_pdf(view, document);//firstpage
                    totalsecond=totalsecond+1;

                }
            };

            Runnable movetofirstpage=new Runnable() {
                @Override
                public void run() {

                    previouspage.post(new Runnable() {
                        @Override
                        public void run() {
                            previouspage.performClick();
                        }
                    });
                }
            };

            /*Runnable movetofirstpage=new Runnable() {
                @Override
                public void run() {
                    show_previospagedata();
                    }
                };*/


            Runnable movetonextpaget2=new Runnable() {
                @Override
                public void run() {

                    nextpage.post(new Runnable() {
                        @Override
                        public void run() {

                            nextpage.performClick();
                            totalsecond=totalsecond+1;
                        }
                    });

                }
            };

            Runnable createmiddlepages=new Runnable() {
                @Override
                public void run() {

                    View view = findViewById(R.id.layouttopdfmain);
                    create_itext_pdf(view, document);
                    totalsecond=totalsecond+1;
                }

            };
            Runnable mergecreatepdf=new Runnable() {
                @Override
                public void run() {
                    document.close();

                    hideLoadingPopup();




                    printPDFV2(file);

                }
            };
            if (pagecount==0){

                executorService.schedule(createfirstpaget1,0,TimeUnit.SECONDS);
            }
            else {

                for (int i = 0; i < pagecount; i++) {
                    executorService.schedule(movetofirstpage,i,TimeUnit.SECONDS);
                    pagecount=pagecount-1;
                    totalsecond=totalsecond+1;

                }

                executorService.schedule(createfirstpaget1,totalsecond,TimeUnit.SECONDS);

            }


            for (int i = 0; i < totalmiddlepages+1; i++) {
                totalsecond=totalsecond+1;
                executorService.schedule(movetonextpaget2,totalsecond+i,TimeUnit.SECONDS);
                totalsecond=totalsecond+1;
                executorService.schedule(createmiddlepages,totalsecond+i,TimeUnit.SECONDS);
                totalsecond=totalsecond+1;
            }
            executorService.schedule(mergecreatepdf,totalmiddlepages+totalsecond+1,TimeUnit.SECONDS);




        }

        if (totalitems > 5 && totalitems <= 10) {


            showLoadingPopup();


            View view = findViewById(R.id.layouttopdfmain);
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "generated_pdf.pdf");
            PdfWriter writer = null;
            try {
                writer = new PdfWriter(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }



            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

            Document document = new Document(pdfDocument);

            document.setLeftMargin(10);
            document.setRightMargin(80);


            //PdfDocument pdfDocument = new PdfDocument();
            //View view = findViewById(R.id.layouttopdfmain);

            //createpageforview(pdfDocument, view);

            create_itext_pdf(view,document);


            Show_lastpagedata();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    create_itext_pdf(view,document);
                    document.close();

                    hideLoadingPopup();


                    printpdf(file.getAbsolutePath());
                 }
            }, 500);
        }


    }
    public void createpageforview(PdfDocument document, View view) {
        // Define margins in pixels from dp
        int sideMarginInDp = 0; // Side margins (left and right)
        int topMarginInDp = 5; // Top margin
        int bottomMarginInDp = 80; // Bottom margin

        float scale = getResources().getDisplayMetrics().density;

        int sideMarginInPx = (int) (sideMarginInDp * scale + 0.5f); // Convert dp to pixels
        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f); // Convert dp to pixels
        int bottomMarginInPx = (int) (bottomMarginInDp * scale + 0.5f); // Convert dp to pixels

        // Get the PDF page dimensions
        int pageWidth = 842; // Set your desired page width in pixels (for example, A4 size)
        int pageHeight = 595; // Set your desired page height in pixels (for example, A4 size)

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        // Calculate the available width and height for content after considering margins
        int contentWidth = pageWidth - (2 * sideMarginInPx);
        int contentHeight = pageHeight - (topMarginInPx + bottomMarginInPx);

        // Create a bitmap with fixed dimensions
        Bitmap bitmap = Bitmap.createBitmap(contentWidth, contentHeight, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(bitmap);

        view.draw(bitmapCanvas);

        // Calculate the scale to fit the fixed-size bitmap onto the PDF canvas
        float scaleX = (float) contentWidth / bitmap.getWidth();
        float scaleY = (float) contentHeight / bitmap.getHeight();
        float scaleToFit = Math.min(scaleX, scaleY) * 0.9f;

        // Calculate the scaled width and height of the bitmap
        int scaledWidth = (int) (bitmap.getWidth() * scaleToFit);
        int scaledHeight = (int) (bitmap.getHeight() * scaleToFit);

        // Calculate the centering offsets
        int xOffset = 20;
        int yOffset = ((contentHeight - scaledHeight) / 2) + topMarginInPx; // Account for top margin

        // Translate the canvas to apply margins and center the content
        canvas.translate(sideMarginInPx + xOffset, yOffset);

        // Scale the content to fit within the available space
        canvas.scale(scaleToFit, scaleToFit);

        // Draw the bitmap onto the PDF canvas at the centered position
        canvas.drawBitmap(bitmap, 0, 0, null);

        document.finishPage(page);
    }


    public synchronized void create_multipagepdf() {
        int currentpage = 0;
        int middlepagecount = 0;

        if (totalitems > 10) {
            showLoadingPopup();

            View view = findViewById(R.id.layouttopdfmain);
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "generated_pdf.pdf");
            PdfWriter writer = null;
            try {
                writer = new PdfWriter(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }



            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

            Document document = new Document(pdfDocument);

            document.setLeftMargin(10);
            document.setRightMargin(80);

            //Executor executor=Executors.newSingleThreadExecutor();
            ScheduledExecutorService executorService=Executors.newSingleThreadScheduledExecutor();
             Runnable createfirstpaget1=new Runnable() {
                @Override
                public void run() {
                    pgd.show();
                        create_itext_pdf(view, document);//firstpage
                        totalsecond=totalsecond+1;

                }
            };

           Runnable movetofirstpage=new Runnable() {
               @Override
               public void run() {
                   pgd.show();
                   previouspage.post(new Runnable() {
                       @Override
                       public void run() {
                           previouspage.performClick();
                       }
                   });
               }
           };

            /*Runnable movetofirstpage=new Runnable() {
                @Override
                public void run() {
                    show_previospagedata();
                    }
                };*/


            Runnable movetonextpaget2=new Runnable() {
                @Override
                public void run() {

                    nextpage.post(new Runnable() {
                        @Override
                        public void run() {

                            nextpage.performClick();
                            totalsecond=totalsecond+1;
                         }
                    });

                }
            };

            Runnable createmiddlepages=new Runnable() {
                @Override
                public void run() {

                    View view = findViewById(R.id.layouttopdfmain);
                    create_itext_pdf(view, document);
                    totalsecond=totalsecond+1;
                }

            };
            Runnable mergecreatepdf=new Runnable() {
                @Override
                public void run() {
                    document.close();

                    hideLoadingPopup();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(taxBill_Activity.this, getApplicationContext().getPackageName() + ".provider", file);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);


                }
            };
            if (pagecount==0){

                executorService.schedule(createfirstpaget1,0,TimeUnit.SECONDS);
            }
            else {

                for (int i = 0; i < pagecount; i++) {
                    executorService.schedule(movetofirstpage,i+1,TimeUnit.SECONDS);
                    pagecount=pagecount-1;
                    totalsecond=totalsecond+1;

                }

                executorService.schedule(createfirstpaget1,totalsecond,TimeUnit.SECONDS);

            }


            for (int i = 0; i < totalmiddlepages+1; i++) {
                totalsecond=totalsecond+1;
                executorService.schedule(movetonextpaget2,totalsecond+i,TimeUnit.SECONDS);
                totalsecond=totalsecond+1;
                executorService.schedule(createmiddlepages,totalsecond+i,TimeUnit.SECONDS);
                totalsecond=totalsecond+1;
            }
            executorService.schedule(mergecreatepdf,totalmiddlepages+totalsecond+1,TimeUnit.SECONDS);


        }

        if (totalitems > 5 && totalitems <= 10) {

            create_two_pagepdf();
            }
        }

        public void create_two_pagepdf(){
            showLoadingPopup();


            View view = findViewById(R.id.layouttopdfmain);
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "generated_pdf.pdf");
            PdfWriter writer = null;
            try {
                writer = new PdfWriter(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }



            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

            Document document = new Document(pdfDocument);

            document.setLeftMargin(10);
            document.setRightMargin(40);
            document.setTopMargin(10);
            document.setBottomMargin(40);


            //PdfDocument pdfDocument = new PdfDocument();
            //View view = findViewById(R.id.layouttopdfmain);

            //createpageforview(pdfDocument, view);

            create_itext_pdf(view,document);


            Show_lastpagedata();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    create_itext_pdf(view,document);
                    document.close();

                    hideLoadingPopup();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(taxBill_Activity.this, getApplicationContext().getPackageName() + ".provider", file);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            }, 500);

        }
    public void create_itext_pdf( View view,Document document){
        // Inflate the layout


        // Set layout params (optional)
        //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Create a bitmap of the view
        Bitmap bitmap = loadBitmapFromView(view);

        float factor = 0.8f;

        // Create a PDF using iText
        try {

            // Assuming A4 size dimensions (595 x 842 units)
            float pageSizeWidth = 595;
            float pageSizeHeight = 842;

            // Calculate 80% of the page size
            float scaledWidth = pageSizeWidth * 0.8f;
            float scaledHeight = pageSizeHeight * 0.8f;

            ImageData imageData = ImageDataFactory.create(bitmapToByteArray(bitmap));

            Image pdfImage = new Image(imageData);
            pdfImage.setMarginBottom(40);
            pdfImage.setMarginRight(80);
            pdfImage.setMarginLeft(10);
            pdfImage.setMarginTop(10);



            //document.add(pdfImage);
            document.add(pdfImage);

                        //document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void createPDFFromLayout() {
        // Inflate the layout
        View view = findViewById(R.id.layouttopdfmain);

        // Set layout params (optional)
        //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Create a bitmap of the view
        Bitmap bitmap = loadBitmapFromView(view);

        // Create a PDF using iText
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "generated_pdf.pdf");
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new Document(pdfDocument);

            ImageData imageData = ImageDataFactory.create(bitmapToByteArray(bitmap));
            Image pdfImage = new Image(imageData);

            document.add(pdfImage);
            document.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  Bitmap loadBitmapFromView(View view) {


        // Define margins in pixels from dp
        int sideMarginInDp = 0; // Side margins (left and right)
        int topMarginInDp = 5; // Top margin
        int bottomMarginInDp = 80; // Bottom margin

        float scale = getResources().getDisplayMetrics().density;

        int sideMarginInPx = (int) (sideMarginInDp * scale + 0.5f); // Convert dp to pixels
        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f); // Convert dp to pixels
        int bottomMarginInPx = (int) (bottomMarginInDp * scale + 0.5f); // Convert dp to pixels

        // Get the PDF page dimensions
        int pageWidth = 842; // Set your desired page width in pixels (for example, A4 size)
        int pageHeight = 595; // Set your desired page height in pixels (for example, A4 size)

        // Calculate the available width and height for content after considering margins
        int contentWidth = pageWidth - (2 * sideMarginInPx);
        int contentHeight = pageHeight - (topMarginInPx + bottomMarginInPx);


        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);



        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(android.graphics.Color.WHITE);
        }
        view.draw(canvas);
        return bitmap;
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap createBitmapFromView(View view) {
        // Define margins in pixels from dp
        int sideMarginInDp = 0; // Side margins (left and right)
        int topMarginInDp = 5; // Top margin
        int bottomMarginInDp = 80; // Bottom margin

        float scale = view.getResources().getDisplayMetrics().density;

        int sideMarginInPx = (int) (sideMarginInDp * scale + 0.5f); // Convert dp to pixels
        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f); // Convert dp to pixels
        int bottomMarginInPx = (int) (bottomMarginInDp * scale + 0.5f); // Convert dp to pixels

        // Get the PDF page dimensions
        int pageWidth = 842; // Set your desired page width in pixels (for example, A4 size)
        int pageHeight = 595; // Set your desired page height in pixels (for example, A4 size)

        // Create a Bitmap with specified dimensions
        Bitmap bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Calculate the available width and height for content after considering margins
        int contentWidth = pageWidth - (2 * sideMarginInPx);
        int contentHeight = pageHeight - (topMarginInPx + bottomMarginInPx);

        // Calculate the scale to fit the view within the available content area
        float scaleX = (float) contentWidth / view.getWidth();
        float scaleY = (float) contentHeight / view.getHeight();
        float scaleToFit = Math.min(scaleX, scaleY) * 0.9f;

        // Calculate the scaled width and height of the view
        int scaledWidth = (int) (view.getWidth() * scaleToFit);
        int scaledHeight = (int) (view.getHeight() * scaleToFit);

        // Calculate the centering offsets
        int xOffset = 20;
        int yOffset = ((contentHeight - scaledHeight) / 2) + topMarginInPx; // Account for top margin

        // Translate the canvas to apply margins and center the content
        canvas.translate(sideMarginInPx + xOffset, yOffset);

        // Scale the content to fit within the available space
        canvas.scale(scaleToFit, scaleToFit);

        // Draw the view content onto the canvas at the centered position
        view.draw(canvas);

        return bitmap;
    }

    /*public synchronized void createpageforview(PdfDocument document,View view){

        // Define margins in pixels from dp
        int sideMarginInDp = 0; // Side margins (left and right)
        int topMarginInDp = 5; // Top margin
        int bottomMarginInDp = 80; // Bottom margin

        float scale = getResources().getDisplayMetrics().density;

        int sideMarginInPx = (int) (sideMarginInDp * scale + 0.5f); // Convert dp to pixels
        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f); // Convert dp to pixels
        int bottomMarginInPx = (int) (bottomMarginInDp * scale + 0.5f); // Convert dp to pixels

        // Get the PDF page dimensions
        int pageWidth = 842; // Set your desired page width in pixels (for example, A4 size)
        int pageHeight = 595; // Set your desired page height in pixels (for example, A4 size)


        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        // Calculate the available width and height for content after considering margins
        int contentWidth = pageWidth - (2 * sideMarginInPx);
        int contentHeight = pageHeight - (topMarginInPx + bottomMarginInPx);

        // Calculate the scale to fit the view within the available content area
        float scaleX = (float) contentWidth / view.getWidth();
        float scaleY = (float) contentHeight / view.getHeight();
        float scaleToFit = Math.min(scaleX, scaleY)*0.9f;

        // Calculate the scaled width and height of the view
        int scaledWidth = (int) (view.getWidth() * scaleToFit);
        int scaledHeight = (int) (view.getHeight() * scaleToFit);

        // Calculate the centering offsets
        int xOffset = 20;
        int yOffset = ((contentHeight - scaledHeight) / 2) + topMarginInPx; // Account for top margin

        // Translate the canvas to apply margins and center the content
        canvas.translate(sideMarginInPx + xOffset, yOffset);

        // Scale the content to fit within the available space
        canvas.scale(scaleToFit, scaleToFit);

        // Draw the view content onto the canvas at the centered position
        view.draw(canvas);

        document.finishPage(page);

    }*/
    public void createpdfm2(){

        View view = findViewById(R.id.layouttopdfmain);

        // Define margins in pixels from dp
        int sideMarginInDp = 0; // Side margins (left and right)
        int topMarginInDp = 5; // Top margin
        int bottomMarginInDp = 80; // Bottom margin

        float scale = getResources().getDisplayMetrics().density;

        int sideMarginInPx = (int) (sideMarginInDp * scale + 0.5f); // Convert dp to pixels
        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f); // Convert dp to pixels
        int bottomMarginInPx = (int) (bottomMarginInDp * scale + 0.5f); // Convert dp to pixels

        // Get the PDF page dimensions
        int pageWidth = 842; // Set your desired page width in pixels (for example, A4 size)
        int pageHeight = 595; // Set your desired page height in pixels (for example, A4 size)

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        // Calculate the available width and height for content after considering margins
        int contentWidth = pageWidth - (2 * sideMarginInPx);
        int contentHeight = pageHeight - (topMarginInPx + bottomMarginInPx);

        // Calculate the scale to fit the view within the available content area
        float scaleX = (float) contentWidth / view.getWidth();
        float scaleY = (float) contentHeight / view.getHeight();
        float scaleToFit = Math.min(scaleX, scaleY)*0.9f;

        // Calculate the scaled width and height of the view
        int scaledWidth = (int) (view.getWidth() * scaleToFit);
        int scaledHeight = (int) (view.getHeight() * scaleToFit);

        // Calculate the centering offsets
        int xOffset = 20;
        int yOffset = ((contentHeight - scaledHeight) / 2) + topMarginInPx; // Account for top margin

        // Translate the canvas to apply margins and center the content
        canvas.translate(sideMarginInPx + xOffset, yOffset);

        // Scale the content to fit within the available space
        canvas.scale(scaleToFit, scaleToFit);

        // Draw the view content onto the canvas at the centered position
        view.draw(canvas);

        pdfDocument.finishPage(page);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "generated_pdf.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

    }

    //rough3

    public void create_print_pdf_singlepage(){
        View view = findViewById(R.id.layouttopdfmain);
        originalorientation = getResources().getConfiguration().orientation;

        // Define margins in pixels from dp
        int sideMarginInDp = 0; // Side margins (left and right)
        int topMarginInDp = 5; // Top margin
        int bottomMarginInDp = 80; // Bottom margin

        float scale = getResources().getDisplayMetrics().density;

        int sideMarginInPx = (int) (sideMarginInDp * scale + 0.5f); // Convert dp to pixels
        int topMarginInPx = (int) (topMarginInDp * scale + 0.5f); // Convert dp to pixels
        int bottomMarginInPx = (int) (bottomMarginInDp * scale + 0.5f); // Convert dp to pixels

        // Get the PDF page dimensions
        int pageWidth = 842; // Set your desired page width in pixels (for example, A4 size)
        int pageHeight = 595; // Set your desired page height in pixels (for example, A4 size)

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        // Calculate the available width and height for content after considering margins
        int contentWidth = pageWidth - (2 * sideMarginInPx);
        int contentHeight = pageHeight - (topMarginInPx + bottomMarginInPx);

        // Calculate the scale to fit the view within the available content area
        float scaleX = (float) contentWidth / view.getWidth();
        float scaleY = (float) contentHeight / view.getHeight();
        float scaleToFit = Math.min(scaleX, scaleY)*0.9f;

        // Calculate the scaled width and height of the view
        int scaledWidth = (int) (view.getWidth() * scaleToFit);
        int scaledHeight = (int) (view.getHeight() * scaleToFit);

        // Calculate the centering offsets
        int xOffset = 20;
        int yOffset = ((contentHeight - scaledHeight) / 2) + topMarginInPx; // Account for top margin

        // Translate the canvas to apply margins and center the content
        canvas.translate(sideMarginInPx + xOffset, yOffset);

        // Scale the content to fit within the available space
        canvas.scale(scaleToFit, scaleToFit);

        // Draw the view content onto the canvas at the centered position
        view.draw(canvas);

        pdfDocument.finishPage(page);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "generated_pdf_print.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();



        printpdf(file.getAbsolutePath());
        //print(file.getAbsolutePath(),adapter,new PrintAttributes.Builder().build());

    }

    //rough5


    public float convertDpToFloat(Context context, int dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        return dpValue * density;
    }

    int countdiscountvalue=0;
    boolean settotaldiscount_methodexecuted=false;
    @Override
    public void setdiscountvalues(double discvalue) {
        if (countdiscountvalue < totalitems && settotaldiscount_methodexecuted == false) {
            finaldiscvalue = finaldiscvalue + discvalue;
            countdiscountvalue = countdiscountvalue + 1;
            discvaluetxt.setText(String.valueOf(Utility.round(finaldiscvalue, 2)));
            if (countdiscountvalue >= totalitems) {
                settotaldiscount_methodexecuted = true;
            }
        }
    }

    int countq=0;
    boolean settotalqty_methodexecuted=false;

    @Override
    public  void settotalqty(int qty) {

        if (countq<totalitems && settotalqty_methodexecuted==false){
            totalquantoty=totalquantoty+qty;
            countq=countq+1;
            totalquantity.setText(String.valueOf(totalquantoty));
            if (countq>=totalitems){
                settotalqty_methodexecuted=true;
            }

        }
    }

    int counttaxvalue=0;
    boolean settotaltaxvalue_executed=false;
    @Override
    public void settotal_taxablevalue(double total) {

        if (counttaxvalue<totalitems && settotaltaxvalue_executed==false){
            totaltaxablevalue=totaltaxablevalue+total;
            counttaxvalue=counttaxvalue+1;
            totaltaxablevalueview.setText(String.valueOf(Utility.round(totaltaxablevalue,2)));
            if (counttaxvalue>=totalitems){
                settotaltaxvalue_executed=true;
            }

        }
    }

    int countcgstvalue=0;
    boolean settoalcgstvalue_executed=false;
    @Override
    public void settotalcgstvalue(double cgstvalue) {

        if (countcgstvalue<totalitems && settoalcgstvalue_executed==false){
            totalcgstvalue=totalcgstvalue+cgstvalue;
            countcgstvalue=countcgstvalue+1;
            totalcgstvalueview.setText(String.valueOf(Utility.round(totalcgstvalue,2)));
            if (countcgstvalue>=totalitems){
                settoalcgstvalue_executed=true;
            }

        }
    }

    int countsgstvalue=0;
    boolean settoalsgstvalue_executed=false;

    @Override
    public void settotalsgstvalue(double sgstvalue) {
        if (countsgstvalue<totalitems && settoalsgstvalue_executed==false){
            totalsgstvalue=totalsgstvalue+sgstvalue;
            countsgstvalue=countsgstvalue+1;
            totalsgstvalueview.setText(String.valueOf(Utility.round(totalsgstvalue,2)));
            if (countsgstvalue>=totalitems){
                settoalsgstvalue_executed=true;
            }

        }

    }

    int counttotalbillvalue=0;
    boolean settoalbillamount_executed=false;
    @Override
    public void settotalbillamount(double total) {
        if (counttotalbillvalue<totalitems && settoalbillamount_executed==false){
            totalbillamount=total+totalbillamount;
            counttotalbillvalue=counttotalbillvalue+1;
            totalbillamountview.setText(String.valueOf(Utility.round(totalbillamount,2)));
            if (counttotalbillvalue>=totalitems){
                settoalbillamount_executed=true;
            }

        }

    }

    @Override
    public void setsgstrate(String rate) {

    }

    @Override
    public void setcgstrate(String rate) {

    }

    @Override
    public double getdiscountvalues() {

        return 0;
    }

    @Override
    public int gettotalqty() {

        return 0;
    }

    @Override
    public double gettotal_taxable_value() {

        return 0;
    }

    @Override
    public double gettotalcgstvalue() {
        return 0;
    }

    @Override
    public double gettotalsgstvalue() {
        return 0;
    }

    @Override
    public double gettotalbillamount() {
        return 0;
    }

    public double getTotalbillamount() {
        return totalbillamount;
    }

    int transfertotalitems=0;

    public int getTransfertotalitems() {
        return transfertotalitems;
    }

    // Method to show the loading popup
    private void showLoadingPopup() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Generating PDF...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    // Method to hide the loading popup
    private void hideLoadingPopup() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



}


