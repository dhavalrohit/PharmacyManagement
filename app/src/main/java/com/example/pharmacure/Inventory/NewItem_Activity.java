package com.example.pharmacure.Inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pharmacure.Utils.DecimalDigitsInputFilter;
import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Utils.Utility;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_inventory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NewItem_Activity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout productName, batchNo, qty, expdate, hsnno,mrp, rate, companyName, productId, packaging,gstNo;
    Spinner disc,cstax,producttype_spinner,unittype_spinner;
    EditText exproductname, exbatchNo, exqty, exexpdate, exhsnno,exmrp, exrate,
            excompanyName, exproductID,  expackaging,exgstNo,exhsamount,exbaseamount,
            excstax,exdiscount,exscamounttext,exdiscountAmountText,extotalAmountText,exproductType,exunittype;

    private DatabaseReference db;

    private FirebaseAuth mAuth;
    String uid;
    private int mYear, mMonth, mDay;

    String generatedproductId="";

    Button clearbtn,addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);

        productName = findViewById(R.id.ProductnameLayout);
        batchNo = findViewById(R.id.batchLayout_add_info);
        qty = findViewById(R.id.quantityLayout);
        expdate = findViewById(R.id.expLayout);
        mrp = findViewById(R.id.mrpLayout);
        rate = findViewById(R.id.rateLayout);
        productId = findViewById(R.id.codeLayout);
        packaging = findViewById(R.id.packagingLayout);
        hsnno=findViewById(R.id.hsnoLayout);
        gstNo=findViewById(R.id.gstLayout);

       // rate.setClickable(true);//sets textinputlayout clickable


        disc=findViewById(R.id.discount_perc_spinner);
        disc.setFocusable(true);
        disc.setFocusableInTouchMode(true);
        disc.requestFocus();

        cstax=findViewById(R.id.cstax_perc_spinner);
        cstax.setFocusable(true);
        cstax.setFocusableInTouchMode(true);
        cstax.requestFocus();

        producttype_spinner=findViewById(R.id.product_type_spinner);
        producttype_spinner.setFocusable(true);
        //producttype_spinner.setFocusableInTouchMode(true);
        producttype_spinner.requestFocus();

        unittype_spinner=findViewById(R.id.unit_type_spinner);
        unittype_spinner.setFocusable(true);
        //unittype_spinner.setFocusableInTouchMode(true);
        unittype_spinner.requestFocus();

        String[] discountperc=new String[]{"0","5","10","12","15"};
        String[] cstaxperc=new String[]{"0","5","10","12","15"};
        String[] producttype=new String[]{"Tablet","Other"};
        String[] unittype=new String[]{"mg","kg","ml","IU"};

        ArrayAdapter<String> discountadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, discountperc);
        disc.setAdapter(discountadapter);

        ArrayAdapter<String> cstaxadapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,cstaxperc);
        cstax.setAdapter(cstaxadapter);

        ArrayAdapter<String> unittypeadapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,unittype);
        unittype_spinner.setAdapter(unittypeadapter);

        ArrayAdapter<String> producttypeAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,producttype);
        producttype_spinner.setAdapter(producttypeAdapter);

        exproductname = (EditText) findViewById(R.id.ProductnameText);
        exbatchNo = (EditText) findViewById(R.id.batchText);
        exqty =(EditText)  findViewById(R.id.quantityText);
        exexpdate = (EditText) findViewById(R.id.expText);
        exmrp = (EditText) findViewById(R.id.mrpText);
        exrate=(EditText) findViewById(R.id.rateText);
        excompanyName = (EditText) findViewById(R.id.companyNameText);
        exproductID=(EditText) findViewById(R.id.productIDText);
        expackaging=(EditText) findViewById(R.id.packagingText);
        exhsnno=(EditText) findViewById(R.id.hsnNoText);
        exgstNo=(EditText) findViewById(R.id.gstNoText);
        exhsamount=(EditText) findViewById(R.id.hsamountText);
        exbaseamount=(EditText) findViewById(R.id.baseamountText);
        excstax=(EditText) findViewById(R.id.cstaxtext);
        exdiscount=(EditText) findViewById(R.id.discounttext);
        exscamounttext=(EditText) findViewById(R.id.sctaxamountText);
        exdiscountAmountText=(EditText) findViewById(R.id.discountamountText);
        extotalAmountText=(EditText) findViewById(R.id.totalamountText);
        exproductType=(EditText) findViewById(R.id.producttypetext);
        exunittype=(EditText) findViewById(R.id.unittypetext);



        clearbtn=findViewById(R.id.btn_clear);
        addbtn=findViewById(R.id.btn_add);

        exmrp.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });
        exrate.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });

        exexpdate.setInputType(InputType.TYPE_NULL);
        exexpdate.setFocusable(false);
        exexpdate.setOnClickListener(this);

        generatedproductId=Firebase_factory_inventory.getproductid();
        System.out.println("generated product id:"+generatedproductId);

        exproductID.setText(generatedproductId);

        //exrate.setEnabled(false);//makes rate textfield Non-Editable
        exproductID.setEnabled(false);//makes productID Non-Editable
        exbaseamount.setEnabled(false);//makes base amount Non-Editable
        exscamounttext.setEnabled(false);//makes tax amount Non-Editable
        exdiscountAmountText.setEnabled(false);//makes discount amount Non-Editable
        extotalAmountText.setEnabled(false);//makes total amount text Non-Editable
        excstax.setEnabled(false);
        exdiscount.setEnabled(false);
        exproductType.setEnabled(false);




        /* if (exqty.getText().toString().isEmpty()) {
                   System.out.println("clicked");
                   Toast.makeText(NewItem_Activity.this, "Please enter quantity first", Toast.LENGTH_SHORT).show();

               }*/

        if (exqty != null && exrate != null) {
            exrate.setEnabled(false); // Initially disable rate input

            exqty.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    String quantityString = exqty.getText().toString();
                    if (quantityString.isEmpty() || Integer.parseInt(quantityString) == 0) {
                        Toast.makeText(getApplicationContext(), "Enter quantity first", Toast.LENGTH_SHORT).show();
                        exqty.requestFocus(); // Request focus back to quantity field
                    } else {
                        exrate.setEnabled(true); // Enable rate input if quantity is entered
                    }
                }
            });

            exrate.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    String quantityString = exqty.getText().toString();
                    if (quantityString.isEmpty() || Integer.parseInt(quantityString) == 0) {
                        Toast.makeText(getApplicationContext(), "Enter quantity first", Toast.LENGTH_SHORT).show();
                        exqty.requestFocus(); // Request focus to quantity field
                    }
                }
            });
        }




        exqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                 if (!exqty.getText().toString().isEmpty()) {
                    if (!exbaseamount.getText().toString().isEmpty()) {
                        int quantity = Integer.parseInt(String.valueOf(exqty.getText()));
                        double rate = Double.parseDouble(String.valueOf(exrate.getText()));
                        double hsamount = Double.parseDouble(String.valueOf(exhsamount.getText()));

                        if (hsamount > 0) {
                            if (quantity > 0) {
                                if (rate > 0) {

                                    double baseamount = Utility.calculate_base_amount(rate, quantity, hsamount);
                                    exbaseamount.setText(Double.toString(Utility.round(baseamount, 2)));



                                }
                            }

                        } else if (exhsamount.getText().toString().isEmpty()) {

                            if (quantity > 0) {
                                if (rate > 0) {

                                    double baseamount = Utility.calculate_base_amount(rate, quantity, 0);
                                    exbaseamount.setText(Double.toString(Utility.round(baseamount, 2)));
                                }
                            }

                        } else if (hsamount == 0) {
                            if (quantity > 0) {
                                if (rate > 0) {
                                    double baseamount = Utility.calculate_base_amount(rate, quantity, 0);
                                    exbaseamount.setText(Double.toString(Utility.round(baseamount, 2)));

                                }
                            }


                        }

                    }
                }
            }
        });

        /*System.out.println("Clicked");
                if (exqty.getText().toString().isEmpty()){
                    System.out.println("clicked");
                    Toast.makeText(getBaseContext(), "Please enter quantity first", Toast.LENGTH_SHORT).show();
                */




        //calculate base amount and autofill base amount in textfield
        exhsamount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }
                else {
                    int quantity=Integer.parseInt(String.valueOf(exqty.getText()));
                    double rate=Double.parseDouble(String.valueOf(exrate.getText()));
                    double hsamount=Double.parseDouble(String.valueOf(exhsamount.getText()));

                    if (hsamount>0){
                        if (quantity>0){
                            if (rate>0){

                                double baseamount=Utility.calculate_base_amount(rate,quantity,hsamount);
                                exbaseamount.setText(Double.toString(Utility.round(baseamount,2)));
                            }
                        }

                    } else if (exhsamount.getText().toString().isEmpty()) {

                        if (quantity>0){
                            if (rate>0){

                                double baseamount=Utility.calculate_base_amount(rate,quantity,0);
                                exbaseamount.setText(Double.toString(Utility.round(baseamount,2)));
                            }
                        }

                    } else if (hsamount==0) {
                        if (quantity>0){
                            if (rate>0){
                                double baseamount=Utility.calculate_base_amount(rate,quantity,0);
                                exbaseamount.setText(Double.toString(Utility.round(baseamount,2)));

                            }
                        }


                    } else {
                        Toast.makeText(getBaseContext(), "Please enter correct values.", Toast.LENGTH_SHORT).show();
                    }




                }
            }
        });

        //get selected tax percentage value and autofill in textfield
        cstax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item=parent.getItemAtPosition(position);

                String selecteditem=item.toString();
                excstax.setText(selecteditem);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get selected discount percentage value and autofill in textfield
        disc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item=parent.getItemAtPosition(position);

                String selecteditem=item.toString();
                exdiscount.setText(selecteditem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            producttype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item=parent.getItemAtPosition(position);

                String selecteditem=item.toString();
                exproductType.setText(selecteditem);
                if (exproductType.getText().toString().equalsIgnoreCase("tablet")){
                    expackaging.setInputType(InputType.TYPE_CLASS_NUMBER);
                    expackaging.setHint("Enter No of Tablets Per Strip");
                    unittype_spinner.setEnabled(false);
                    exunittype.setEnabled(false);
                }else {
                    expackaging.setInputType(InputType.TYPE_CLASS_TEXT);
                    unittype_spinner.setEnabled(true);
                    expackaging.setHint("Qty/Pack");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        unittype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item=parent.getItemAtPosition(position);

                String selecteditem=item.toString();
                exunittype.setText(selecteditem);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //calculates and  set tax amount from base amount and autofill in textfield
       cstax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }else {

                    int taxrate=Integer.parseInt(String.valueOf(excstax.getText()));
                    System.out.println(taxrate);
                    double baseamount=Double.valueOf(String.valueOf(exbaseamount.getText()));
                    System.out.println(baseamount);

                    double taxamount=Utility.calculatetax(baseamount,taxrate);
                    exscamounttext.setText(Double.toString(Utility.round(taxamount,2)));

                }

            }
        });



        //calculates and  set discount amount from base amount and autofill in textfield
       disc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){


                }else {

                    int discountpercentage=Integer.parseInt(String.valueOf(exdiscount.getText()));
                    double baseamount=Double.valueOf(String.valueOf(exbaseamount.getText()));
                    double discountamount=Utility.calculate_discount(baseamount,discountpercentage);
                    exdiscountAmountText.setText(Double.toString(Utility.round(discountamount,2)));

                    double cstaxamount=Double.valueOf(String.valueOf(exscamounttext.getText()));
                    double totalamount=Utility.calculate_total_amount(baseamount,cstaxamount,discountamount);
                    extotalAmountText.setText(Double.toString(Utility.round(totalamount,2)));

                }
            }
        });





        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addinfo();

                finish();
            }
        });

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }


    public void addinfo(){



        String sproductname=exproductname.getText().toString();
        String scompanyname=excompanyName.getText().toString();
        String srate=exrate.getText().toString();

        String spackagingtablet="1 Strip of "+expackaging.getText().toString();
        String spackagingother=expackaging.getText().toString()+exunittype.getText().toString();

        String sexpdate=exexpdate.getText().toString();
        String squantity=exqty.getText().toString();
        String sbatchno=exbatchNo.getText().toString();
        String sproductID=exproductID.getText().toString();
        String smrp=exmrp.getText().toString();
        String shsnno=exhsnno.getText().toString();
        String sgstno=exgstNo.getText().toString();
        String sdiscountperc=disc.getSelectedItem().toString();
        String scstaxperc=cstax.getSelectedItem().toString();
        String shsamount=exhsamount.getText().toString();
        String sbaseamount=exbaseamount.getText().toString();
        String sdiscountamount=exdiscountAmountText.getText().toString();
        String ssctaxamount=exscamounttext.getText().toString();
        String stotalamount=extotalAmountText.getText().toString();


        if(validate()){
            if (exproductType.getText().toString().equalsIgnoreCase("tablet")){
                Firebase_factory_inventory.addProduct(sproductname,sproductID,shsnno,scompanyname,smrp,srate,sexpdate,spackagingtablet,squantity,sbatchno,sgstno,sdiscountperc,scstaxperc,shsamount,sbaseamount,sdiscountamount,ssctaxamount,stotalamount);
                Toast.makeText(this, sproductname+" saved", Toast.LENGTH_SHORT).show();
            }else {
                Firebase_factory_inventory.addProduct(sproductname,sproductID,shsnno,scompanyname,smrp,srate,sexpdate,spackagingother,squantity,sbatchno,sgstno,sdiscountperc,scstaxperc,shsamount,sbaseamount,sdiscountamount,ssctaxamount,stotalamount);
                Toast.makeText(this, sproductname+" saved", Toast.LENGTH_SHORT).show();

            }

        }
    }





    public boolean validate() {
        int flag = 0;
        if (exproductname.getText().toString().trim().isEmpty()) {
            flag = 1;
            productName.setError("Cannot be Empty");
        } else
            productName.setErrorEnabled(false);

        if (exqty.getText().toString().trim().isEmpty()) {
            flag = 1;
            qty.setError("Cannot be Empty");
        } else
            qty.setErrorEnabled(false);

        if (exbatchNo.getText().toString().trim().isEmpty()) {
            flag = 1;
            batchNo.setError("Cannot be Empty");
        } else
            batchNo.setErrorEnabled(false);

        if (exexpdate.getText().toString().trim().isEmpty()) {
            flag = 1;
            expdate.setError("Cannot be Empty");
        } else
            expdate.setErrorEnabled(false);

        if (exmrp.getText().toString().trim().isEmpty()) {
            flag = 1;
            mrp.setError("Cannot be Empty");
        } else
            mrp.setErrorEnabled(false);

        if (exrate.getText().toString().trim().isEmpty()) {
            flag = 1;
            exrate.setError("Cannot be Empty");
        } else
            rate.setErrorEnabled(false);

        if (excompanyName.getText().toString().trim().isEmpty()) {
            flag = 1;
            companyName.setError("Cannot be Empty");
        } else
            rate.setErrorEnabled(false);

        if (exproductID.getText().toString().trim().isEmpty()) {
            flag = 1;
            productId.setError("Cannot be Empty");
        } else
            productId.setErrorEnabled(false);

        if (exgstNo.getText().toString().trim().isEmpty()) {
            flag = 1;
            gstNo.setError("Cannot be Empty");
        } else
            gstNo.setErrorEnabled(false);


        if (expackaging.getText().toString().trim().isEmpty()) {
            flag = 1;
            packaging.setError("Cannot be Empty");
        } else
            packaging.setErrorEnabled(false);

        return flag != 1;
    }

    public void clear(){
        exproductname.setText("");
        expackaging.setText("");
        exmrp.setText("");
        exrate.setText("");
        excompanyName.setText("");
        exproductID.setText("");
        exbatchNo.setText("");
        exexpdate.setText("");
        exqty.setText("");
        exhsnno.setText("");

    }




    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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
                SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
                exexpdate.setText(format.format(calendar.getTime()));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }

}