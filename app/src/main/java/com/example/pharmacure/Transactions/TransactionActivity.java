package com.example.pharmacure.Transactions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Model.CustomerModel;
import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Utils.Utility;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_Bill;
import com.factory.Firebase_factory_inventory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {

    View transaview;
    String uid;
    private DatabaseReference dbinventory, dbinvoice, dbcustomer, dbsummary;
    private FirebaseAuth mAuth;
    AutoCompleteTextView act, act2;
    AutoAdapterMedicine medicineadapter;
    AutoCustomerAdapter autoCustAdapter;
    BillAdapter billadapter;
    ProductAdapter productadapter;
    ArrayList<MedicineModel> models = new ArrayList<>();
    TextInputLayout date, patientname, doctorname, patientage;
    EditText edate, epatientname, edoctorname, epatientAge, due;
    TextView billIDtextview;
    private int mYear, mMonth, mDay;
    ArrayList<BillModel> billlistmodel = new ArrayList<>();
    ArrayList<CustomerModel> modelcustomers = new ArrayList<>();
    ListView lv1;
    //ListView lv2_productdetails;
    Button addbtn, register_customerbtn;
    static Button generatebill;
    LinearLayout linearLayout;

     static TextView totalamounttext;

     static double totamount;
    boolean set=false;

    String productName="";
    String productId="";
    String gstno="";
    String hsnno="";
    String companyname="";
    String expriydate="";
    String batchNo="";
    String packaging="";
    String quantity="";
    String mrp="";
    String rate="";
    String sctax="";
    String discount="";
    String hs_amount="";
    String base_amount="";
    String Sctax_amount="";
    String discount_amount="";
    String total_amount="";

    static TextView amount;

    static double billtotalamount=0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction);
        setTitle("Transaction");

        getSupportActionBar().hide();

        date = (TextInputLayout) findViewById(R.id.dateLayout_trans_activity);
        patientname = (TextInputLayout) findViewById(R.id.ptLayout);
        doctorname = (TextInputLayout) findViewById(R.id.docLayout);
        patientage = (TextInputLayout) findViewById(R.id.ageLayout);

          totalamounttext = (TextView)findViewById(R.id.totalamounttext_transaction);


        edate = (EditText) findViewById(R.id.dateText_transaction);
        epatientname = (EditText) findViewById(R.id.ptText);
        edoctorname = (EditText) findViewById(R.id.docText);
        epatientAge = (EditText) findViewById(R.id.ageText);
        due = (EditText) findViewById(R.id.amtdue);

        String generatedbillid=Firebase_factory_Bill.getBillID();

        billIDtextview=findViewById(R.id.billIdText);
        billIDtextview.setText(generatedbillid);


        edate.setInputType(InputType.TYPE_NULL);
        edate.setFocusable(false);
        edate.setOnClickListener(this);
        act = (AutoCompleteTextView) findViewById(R.id.act);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        edate.setText(format.format(c.getTime()));

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        dbinventory = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Inventory");
        dbinvoice = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Invoice");
        dbcustomer = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Customer");
        dbsummary = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Summary");

        dbinventory.keepSynced(true);
        dbinvoice.keepSynced(true);
        dbcustomer.keepSynced(true);
        dbsummary.keepSynced(true);

        addbtn = (Button) findViewById(R.id.btnadd);
        generatebill = (Button) findViewById(R.id.genbtn);

        linearLayout = (LinearLayout) findViewById(R.id.cust);

        generatebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    if (custValidate()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(TransactionActivity.this);
                        alert.setTitle("Confirm");
                        alert.setMessage("Generate Bill?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Firebase_factory_Bill.addsales_Bill(generatedbillid,billlistmodel,String.valueOf(billtotalamount),edate.getText().toString(),edoctorname.getText().toString(),epatientname.getText().toString(),epatientAge.getText().toString());
                                Toast.makeText(getApplicationContext(),"Sales Added ",Toast.LENGTH_SHORT).show();
                                finish();
                                // clear();
                            }
                        });
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        alert.show();
                    } else {
                        Toast.makeText(TransactionActivity.this, "Regular Customer Details not properly entered", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(TransactionActivity.this, "Fill Details Properly", Toast.LENGTH_SHORT).show();
                }
            }
        });



        dbinventory.child("productList").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                models.clear();
                int childrencount= (int) dataSnapshot.getChildrenCount();

                for (int i=1;i<=childrencount;i++){
                    productName=dataSnapshot.child(String.valueOf(i)).child("productName").getValue(String.class);
                    productId=dataSnapshot.child(String.valueOf(i)).child("productID").getValue(String.class);
                    gstno=dataSnapshot.child(String.valueOf(i)).child("GST NO").getValue(String.class);
                    hsnno=dataSnapshot.child(String.valueOf(i)).child("HSN_NO").getValue(String.class);
                    companyname=dataSnapshot.child(String.valueOf(i)).child("companyName").getValue(String.class);
                    expriydate=dataSnapshot.child(String.valueOf(i)).child("expriyDate").getValue(String.class);
                    batchNo=dataSnapshot.child(String.valueOf(i)).child("batchNo").getValue(String.class);
                    packaging=dataSnapshot.child(String.valueOf(i)).child("packaging").getValue(String.class);
                    quantity=dataSnapshot.child(String.valueOf(i)).child("quantity").getValue(String.class);
                    mrp=dataSnapshot.child(String.valueOf(i)).child("MRP").getValue(String.class);
                    rate=dataSnapshot.child(String.valueOf(i)).child("rate").getValue(String.class);
                    sctax=dataSnapshot.child(String.valueOf(i)).child("S+C tax%").getValue(String.class);
                    discount=dataSnapshot.child(String.valueOf(i)).child("discount%").getValue(String.class);
                    hs_amount=dataSnapshot.child(String.valueOf(i)).child("hs_amount").getValue(String.class);
                    base_amount=dataSnapshot.child(String.valueOf(i)).child("base_amount").getValue(String.class);
                    Sctax_amount=dataSnapshot.child(String.valueOf(i)).child("S+C tax_amount").getValue(String.class);
                    discount_amount=dataSnapshot.child(String.valueOf(i)).child("discount_amount").getValue(String.class);
                    total_amount=dataSnapshot.child(String.valueOf(i)).child("total_amount").getValue(String.class);

                    MedicineModel model=new MedicineModel(productName,batchNo,quantity,expriydate,mrp,rate,companyname,productId,hsnno,packaging,gstno,discount,sctax );
                    models.add(model);

                }



                act.setThreshold(1);
                medicineadapter = new AutoAdapterMedicine(TransactionActivity.this, R.layout.activity_transaction, R.id.item_name, models);
                act.setAdapter(medicineadapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        dbcustomer.child("customerList").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                modelcustomers.clear();
                int chldrencount= (int) dataSnapshot.getChildrenCount();

                for (int i=1;i<=chldrencount;i++){
                    CustomerModel cmodel=new CustomerModel(dataSnapshot.child(String.valueOf(i)).child("customerName").getValue(String.class),
                            dataSnapshot.child(String.valueOf(i)).child("mobileNo").getValue(String.class),
                            dataSnapshot.child(String.valueOf(i)).child("Date_Added").getValue(String.class),
                            dataSnapshot.child(String.valueOf(i)).child("balance").getValue(String.class),
                            dataSnapshot.child(String.valueOf(i)).child("customerAddress").getValue(String.class),
                            dataSnapshot.child(String.valueOf(i)).child("customerID").getValue(String.class));

                            modelcustomers.add(cmodel);
                    }



                act2 = (AutoCompleteTextView) findViewById(R.id.actcust);
                act2.setThreshold(1);
                autoCustAdapter = new AutoCustomerAdapter(TransactionActivity.this, R.layout.activity_transaction, R.id.item_name, modelcustomers);
                act2.setAdapter(autoCustAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        billlistmodel.clear();
        lv1 = (ListView) findViewById(R.id.list2);
        billadapter = new BillAdapter(TransactionActivity.this, R.layout.list_items, billlistmodel);
        lv1.setAdapter(billadapter);
        Utility.setListViewHeightBasedOnChildren(lv1);

        /*lv2_productdetails=findViewById(R.id.listproductdetails);
        productadapter=new ProductAdapter(TransactionActivity.this,R.layout.list_items_product,models);
        lv2_productdetails.setAdapter(productadapter);
        Utility.setListViewHeightBasedOnChildren_product(lv2_productdetails);*/


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addbtn();
            }
        });

    }

    // Functions

    public boolean validate() {
        int flag = 0;
        if (edate.getText().toString().trim().isEmpty()) {
            flag = 1;
            date.setError("Cannot be Empty");
        } else
            date.setErrorEnabled(false);
        if (billlistmodel.size() == 0) {
            flag = 1;
        }
        if (flag == 1)
            return false;
        else
            return true;
    }

    public boolean custValidate() {
        if (!autoCustAdapter.detcust()[0].equals("") && !due.getText().toString().equals("")
                && autoCustAdapter.detcust()[2].equals(act2.getText().toString())) {
            set = true;
            return true;

        } else if (autoCustAdapter.detcust()[0].equals("") && due.getText().toString().equals("")
                && act2.getText().toString().equals("")) {
            set = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                edate.setText(format.format(calendar.getTime()));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    protected void addbtn() {

        if (billlistmodel.size() < 8) {
             String item =medicineadapter.getProductName();
             String mrp = medicineadapter.getMrp();
             String batch = medicineadapter.getBatch();
             String expirydate = medicineadapter.getExpdate();
             String key = medicineadapter.getKey();
             String originalquantity = medicineadapter.getOqty();
             String packaging = medicineadapter.getPackaging();
             String gsto=medicineadapter.getGstNo();
             String rate=medicineadapter.getRate();

             //1 strip of 10


            String companyName=medicineadapter.getCompanyName();

            billtotalamount=0+billtotalamount;

             String match = item + " (" + medicineadapter.getProductID() + ")";

            if (!item.equalsIgnoreCase("") && !mrp.equalsIgnoreCase("") && match.equals(act.getText().toString())) {

                if (packaging.length()>10){
                    Toast.makeText(getApplicationContext(),"Tablet",Toast.LENGTH_SHORT).show();
                    LayoutInflater layoutInflater=LayoutInflater.from(TransactionActivity.this);
                    View promptview=layoutInflater.inflate(R.layout.input_tablet,null);
                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(TransactionActivity.this);
                    alertDialogBuilder.setView(promptview);

                    final EditText edittextqty_wholestrip=promptview.findViewById(R.id.edittext_whole_stripQty);
                    final EditText editText_looseQty=promptview.findViewById(R.id.edittextlosequantity);

                    alertDialogBuilder.setCancelable(false).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String wholseStripQty="";
                            wholseStripQty=edittextqty_wholestrip.getText().toString().trim();

                            String looseqty="0";
                            looseqty=editText_looseQty.getText().toString().trim();



                            //validating extfields of quantity and loose qauntity
                            if (wholseStripQty.isEmpty() || wholseStripQty.equals("0")){
                                if (looseqty.isEmpty() || looseqty.equals("0")){
                                    Toast.makeText(TransactionActivity.this, "Enter valid quantity", Toast.LENGTH_SHORT).show();
                                }
                            }

                            //condition if wholsestrip is 0 and only loose quantity is entered
                            if (wholseStripQty.isEmpty() || wholseStripQty.equals("0")){
                                if (Integer.parseInt(looseqty)>0){
                                    if (Integer.parseInt(looseqty)<Integer.parseInt(packaging.substring(11))){
                                        double calcamount=0.0;
                                        double looseprice=(Double.valueOf(looseqty)/Double.valueOf(packaging.substring(11))*Double.valueOf(mrp));
                                        calcamount=calcamount+looseprice;
                                        calcamount=Utility.round(calcamount,2);
                                        BillModel list=new BillModel(item,rate,"0",batch,expirydate,key,originalquantity,packaging,gsto,looseqty,mrp,String.valueOf(calcamount),companyName);
                                        billlistmodel.add(list);
                                        billadapter.notifyDataSetChanged();
                                        act.setText("");
                                        generatebill.setText("GENERATE BILL ");
                                        Utility.setListViewHeightBasedOnChildren(lv1);
                                        dialog.dismiss();
                                        billtotalamount= billtotalamount+calcamount;
                                        //billtotalamount=Utility.round(billtotalamount,2);
                                        totalamounttext.setText(String.valueOf(billtotalamount));


                                    }else {
                                        Toast.makeText(TransactionActivity.this, "loose quantity should be less than "+packaging.substring(11), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }



                            if (!wholseStripQty.equals("") && !wholseStripQty.equals("0")){
                                if (Integer.parseInt(wholseStripQty)<=Integer.parseInt(originalquantity)){
                                    if (looseqty.equals("")){
                                        double calcamount=0.0;
                                         calcamount=Double.valueOf(wholseStripQty)*Double.valueOf(mrp);
                                        BillModel list=new BillModel(item,rate,wholseStripQty,batch,expirydate,key,originalquantity,packaging,gsto,"0",mrp,String.valueOf(calcamount),companyName);
                                        billlistmodel.add(list);
                                        billadapter.notifyDataSetChanged();
                                        act.setText("");
                                        generatebill.setText("GENERATE BILL ");
                                        Utility.setListViewHeightBasedOnChildren(lv1);
                                        dialog.dismiss();
                                        billtotalamount=Double.valueOf(wholseStripQty)*Double.valueOf(mrp)+billtotalamount;
                                        totalamounttext.setText(String.valueOf(billtotalamount));



                                    }else {
                                        double calcamount=0.0;
                                        double looseprice=(Double.valueOf(looseqty)/Double.valueOf(packaging.substring(10))*Double.valueOf(mrp));
                                        calcamount=(Double.valueOf(wholseStripQty)*Double.valueOf(mrp)+looseprice);

                                        BillModel list=new BillModel(item,rate,wholseStripQty,batch,expirydate,key,originalquantity,packaging,gsto,looseqty,mrp,String.valueOf(calcamount),companyName);
                                        billlistmodel.add(list);
                                        billadapter.notifyDataSetChanged();
                                        act.setText("");
                                        generatebill.setText("GENERATE BILL ");
                                        Utility.setListViewHeightBasedOnChildren(lv1);
                                        dialog.dismiss();


                                        billtotalamount=Double.valueOf(wholseStripQty)*Double.valueOf(mrp)+billtotalamount+looseprice;
                                        totalamounttext.setText(String.valueOf(billtotalamount));

                                    }
                                }

                            } else {
                                Toast.makeText(TransactionActivity.this, "Enter valid quantity", Toast.LENGTH_SHORT).show();
                            }





                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    // create an alert dialog
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    alert.show();




                }else {
                    Toast.makeText(getApplicationContext(),"other ",Toast.LENGTH_SHORT).show();
                    LayoutInflater layoutInflater = LayoutInflater.from(TransactionActivity.this);
                    View promptView = layoutInflater.inflate(R.layout.input_dia, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TransactionActivity.this);
                    alertDialogBuilder.setView(promptView);
                    final EditText editText = (EditText) promptView.findViewById(R.id.edittext_intputqty_others);
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                             String qt = "";
                            qt = editText.getText().toString().trim();
                            if (!qt.equals("") && !qt.equals("0")) {

                                if (Integer.parseInt(qt) <= Integer.parseInt(originalquantity)) {


                                /*double d = (Double.valueOf(qt) / Double.valueOf(packaging) * Double.valueOf(mrp));
                                d = roundOff(d);
                                String amount = String.valueOf(d);
                                totamount = totamount + Double.valueOf(amount);
                                totamount = roundOff(totamount);*/

                                    //BillModel list = new BillModel(item, mrp, qt, amount, batch, expirydate, key, originalquantity, packaging);
                                    double calcamount=0.0;
                                    calcamount=Double.valueOf(qt)*Double.valueOf(mrp);
                                    BillModel list=new BillModel(item,rate,qt,batch,expirydate,key,originalquantity,packaging,gsto,"0",mrp,String.valueOf(calcamount),companyName);
                                    billlistmodel.add(list);

                                    billadapter.notifyDataSetChanged();
                                    act.setText("");
                                    generatebill.setText("GENERATE BILL ");

                                    Utility.setListViewHeightBasedOnChildren(lv1);
                                    //Utility.setListViewHeightBasedOnChildren_product(lv2_productdetails);

                                    //totalamounttext.setText(String.valueOf(Utility.calculatetotalamount_bill(billlistmodel)));



                                    dialog.dismiss();
                                    billtotalamount=Double.valueOf(qt)*Double.valueOf(mrp)+billtotalamount;
                                    totalamounttext.setText(String.valueOf(billtotalamount));

                                } else {

                                    Toast.makeText(TransactionActivity.this, "Quantity is not in stock", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(TransactionActivity.this, "Enter valid quantity", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    // create an alert dialog
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    alert.show();

                }

            } else {
                Toast.makeText(TransactionActivity.this, "Select Item", Toast.LENGTH_SHORT).show();
            }
        }

        else {
            Toast.makeText(TransactionActivity.this, "No further items can be added", Toast.LENGTH_SHORT).show();
        }

    }

    public void clear() {
        edate.setText("");
        epatientname.setText("");
        edoctorname.setText("");
        epatientAge.setText("");
        totalamounttext.setText("");
        totamount = 0.0;
        billlistmodel.clear();
        act2.setText("");
        due.setText("");
        billadapter.notifyDataSetChanged();
    }

    public void updateQty(String key, String qty, String orgqty) {
        int oqt = Integer.parseInt(orgqty);
        int qt = Integer.parseInt(qty);
        oqt = oqt - qt;
        dbinventory.child("productList").child(key).child("quantity").setValue(String.valueOf(oqt));
        medicineadapter.notifyDataSetChanged();
    }

    public void updateCust(String key, String amt, String orgamt) {

        double org = roundOff(Double.valueOf(orgamt));
        double am = roundOff(Double.valueOf(amt));
        double samt = totamount - am;
        org = org + samt;
        final String k = key;
        final double tot = totamount;
        final double balance = org;
        final double paid = am;
        dbcustomer.child("customerList").child(key).child("balance").setValue(String.valueOf(org));
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        dbcustomer.child("customerList").child(key).child("date").setValue(formattedDate);

        dbcustomer.child("customerList").child(key).child("log").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                dbcustomer.child("customerList").child(k).child("log").setValue(value + "[" + format.format(calendar.getTime())
                        + "] Transaction. Total:" + tot + " Paid:" + paid + " Balance:" + balance + "\n");
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    public double roundOff(double d) {
        d = Math.round(d * 100.0);
        d = d / 100.0;
        return d;
    }

}


