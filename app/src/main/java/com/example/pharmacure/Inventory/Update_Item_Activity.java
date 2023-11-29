package com.example.pharmacure.Inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pharmacure.Utils.DecimalDigitsInputFilter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Update_Item_Activity extends AppCompatActivity implements View.OnClickListener {


    TextInputLayout productName, batchNo, qty, expdate, hsnno, mrp, rate, companyName, productId, packaging, gstNo;
    Spinner disc, cstax;
    EditText exproductname, exbatchNo, exqty, exexpdate, exhsnno, exmrp, exrate, excompanyName, exproductID, expackaging,
            exgstNo ,exhsamount,exbaseamount,
            excstax,exdiscount,exscamounttext,exdiscountAmountText,extotalAmountText;;

    private int mYear, mMonth, mDay;

    String selectedproductID;


    Button updatebtn, clearbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_item);
        setTitle("Update Item");

        updatebtn = findViewById(R.id.updateproduct_btn_update);
        clearbtn = findViewById(R.id.updateproduct_btn_clear);

        selectedproductID = getIntent().getStringExtra("Keys");
        productName = (TextInputLayout) findViewById(R.id.updateproduct_ProductnameLayout);
        batchNo = (TextInputLayout) findViewById(R.id.updateproduct_batchLayout);
        qty = (TextInputLayout) findViewById(R.id.updateproduct_qtyLayout);
        expdate = (TextInputLayout) findViewById(R.id.updateproduct_expLayout);
        mrp = (TextInputLayout) findViewById(R.id.updateproduct_mrpLayout);
        rate = (TextInputLayout) findViewById(R.id.updateproduct_rateLayout);
        companyName = (TextInputLayout) findViewById(R.id.updateproduct_companyNameLayout);
        productId = (TextInputLayout) findViewById(R.id.updateproduct_ProductIDLayout);
        gstNo = (TextInputLayout) findViewById(R.id.updateproduct_gstLayout);
        hsnno = (TextInputLayout) findViewById(R.id.updateproduct_hsnoLayout);
        packaging = (TextInputLayout) findViewById(R.id.updateproduct_packagingLayout);

        exproductname = (EditText) findViewById(R.id.updateproduct_ProductnameText);
        exbatchNo = (EditText) findViewById(R.id.updateproduct_batchText);
        exqty = (EditText) findViewById(R.id.updateproduct_qtyText);
        exexpdate = (EditText) findViewById(R.id.updateproduct_expText);
        exmrp = (EditText) findViewById(R.id.updateproduct_mrpText);
        exrate = (EditText) findViewById(R.id.updateproduct_rateText);
        excompanyName = (EditText) findViewById(R.id.updateproduct_companyNameText);
        exproductID = (EditText) findViewById(R.id.updateproduct_ProductIDText);
        exgstNo = (EditText) findViewById(R.id.updateproduct_gstNoText);
        exhsnno = (EditText) findViewById(R.id.updateproduct_hsnNoText);
        expackaging = (EditText) findViewById(R.id.updateproduct_packagingText);
        exhsamount=(EditText) findViewById(R.id.updateproduct_hsamountText);
        exbaseamount=(EditText) findViewById(R.id.updateproduct_baseamountText);
        excstax=(EditText) findViewById(R.id.updateproduct_cstaxtext);
        exdiscount=(EditText) findViewById(R.id.updateproduct_discounttext);
        exscamounttext=(EditText) findViewById(R.id.updateproduct_sctaxamountText);
        exdiscountAmountText=(EditText) findViewById(R.id.updateproduct_discountamountText);
        extotalAmountText=(EditText) findViewById(R.id.updateproduct_totalamountText);


        exmrp.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        exrate.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        disc=findViewById(R.id.updateproduct_discount_perc_spinner);
        disc.setFocusable(true);
        disc.setFocusableInTouchMode(true);
        disc.requestFocus();

        cstax=findViewById(R.id.updateproduct_cstax_perc_spinner);
        cstax.setFocusable(true);
        cstax.setFocusableInTouchMode(true);
        cstax.requestFocus();

        String[] discountperc = new String[]{"0", "5", "10", "12", "15"};
        String[] cstaxperc = new String[]{"0", "5", "10", "12", "15"};

        ArrayAdapter<String> discountadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, discountperc);
        disc.setAdapter(discountadapter);

        ArrayAdapter<String> cstaxadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cstaxperc);
        cstax.setAdapter(cstaxadapter);


        exexpdate.setInputType(InputType.TYPE_NULL);
        exexpdate.setFocusable(false);
        exexpdate.setOnClickListener(this);

        exrate.setEnabled(false);//makes rate textfield Non-Editable
        exproductID.setEnabled(false);//makes productID Non-Editable
        exbaseamount.setEnabled(false);//makes base amount Non-Editable
        exscamounttext.setEnabled(false);//makes tax amount Non-Editable
        exdiscountAmountText.setEnabled(false);//makes discount amount Non-Editable
        extotalAmountText.setEnabled(false);//makes total amount text Non-Editable




        exrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exqty.getText().toString().isEmpty()) {
                    System.out.println("clicked");
                    Toast.makeText(getBaseContext(), "Please enter quantity first", Toast.LENGTH_SHORT).show();

                }
            }
        });



        exqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!exqty.getText().toString().isEmpty()){
                    exrate.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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




        Firebase_factory_inventory.fetch_fill_values(selectedproductID,exproductname,
                exproductID,exhsnno,excompanyName,exmrp,exrate,exexpdate,expackaging,exqty,
                exbatchNo,exgstNo,exhsamount,excstax,exdiscount,exbaseamount,exscamounttext,
                exdiscountAmountText,extotalAmountText);


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateproduct();

                finish();
            }
        });

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        exproductID.setEnabled(false);//makes productID Non-Editable

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.del_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
       /* if (id == R.id.del) {

            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(Update_Item_Activity.this);
                    alert.setTitle("Alert!!");
                    alert.setMessage("Are you sure to delete this item?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dataSnapshot.getRef().removeValue();
                            dialog.dismiss();
                            finish();

                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


            });
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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

    public void updateproduct(){
        String sproductname=exproductname.getText().toString();
        String scompanyname=excompanyName.getText().toString();
        String srate=exrate.getText().toString();
        String spackaging=expackaging.getText().toString();
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


        Firebase_factory_inventory.updatedata(selectedproductID,sproductname,sproductID,shsnno,scompanyname,smrp,srate,sexpdate,spackaging,squantity,sbatchno,sgstno,sdiscountperc,scstaxperc,shsamount,sbaseamount,ssctaxamount,sdiscountamount,stotalamount);



    }

    public void clear() {
        exproductname.setText("");
        expackaging.setText("");
        exmrp.setText("");
        exrate.setText("");
        exhsnno.setText("");
        excompanyName.setText("");
        exgstNo.setText("");
        exbatchNo.setText("");
        exexpdate.setText("");
        exqty.setText("");

    }

}
