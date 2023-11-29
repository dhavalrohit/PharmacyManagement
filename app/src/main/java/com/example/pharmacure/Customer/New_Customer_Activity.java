package com.example.pharmacure.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pharmacure.Model.CustomerModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Utils.DecimalDigitsInputFilter;
import com.factory.Firebase_factory;
import com.factory.Firebase_factory_customer;
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

public class New_Customer_Activity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout iName, iaddr, iphone, ibalance, idate;
    EditText excustomerID, eName, eaddr, ephone, ebalance, edate;
    private DatabaseReference du;

    private FirebaseAuth mAuth;
    String uid;
    private int mYear, mMonth, mDay;

    Button addcust_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_customer);
        setTitle("Add Customer");
        iName = (TextInputLayout) findViewById(R.id.CustomernameLayout_add_customer);
        iaddr = (TextInputLayout) findViewById(R.id.addrLayout_add_customer);
        iphone = (TextInputLayout) findViewById(R.id.phoneLayout_add_customer);
        ibalance = (TextInputLayout) findViewById(R.id.balanceLayout_add_customer);
        idate = (TextInputLayout) findViewById(R.id.dateLayout_add_customer);

        excustomerID=findViewById(R.id.customerID_Text_add_customer);
        eName = (EditText) findViewById(R.id.nameText_add_customer);
        eaddr = (EditText) findViewById(R.id.addrText_add_Customer);
        ephone = (EditText) findViewById(R.id.phoneText_add_Customer);
        ebalance = (EditText) findViewById(R.id.balanceText_add_Customer);
        edate = (EditText) findViewById(R.id.dateText_add_Customer);

        addcust_btn=findViewById(R.id.btn_add_Add_newCustomer);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        edate.setText(format.format(c.getTime()));

        ebalance.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });

        edate.setInputType(InputType.TYPE_NULL);
        edate.setFocusable(false);
        edate.setOnClickListener(this);

        String customerid=Firebase_factory_customer.getcustomerid();
        excustomerID.setText(customerid);
        excustomerID.setEnabled(false);


        addcust_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_Customer();
                finish();
            }
        });





    }


    public void add_Customer(){
        String scustomerid=excustomerID.getText().toString();
        String sName = eName.getText().toString();
        String saddr = eaddr.getText().toString();
        String sphone = ephone.getText().toString();
        String sbalance = ebalance.getText().toString();
        String sdate = edate.getText().toString();

        if (validate()){
            Firebase_factory_customer.addCustomer(scustomerid,sName,saddr,sphone,sbalance,sdate);
            Toast.makeText(this, "Customer "+sName+" is saved", Toast.LENGTH_SHORT).show();



        }
        else {
            Toast.makeText(this, "Sorry.. Try Again", Toast.LENGTH_SHORT).show();
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

    public void add_log(String message){

        DatabaseReference dblog=Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Log");


        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm");
        String todaysdate= format.format(date);
        String time=timeformat.format(date);


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

    public void clear(){
        eName.setText("");
        eaddr.setText("");
        ephone.setText("");
        ebalance.setText("");
        edate.setText("");


    }

    public boolean validate() {
        int flag = 0;
        if (eName.getText().toString().trim().isEmpty()) {
            flag = 1;
            iName.setError("Cannot be Empty");
        } else
            iName.setErrorEnabled(false);

        if (ephone.getText().toString().trim().isEmpty()) {
            flag = 1;
            iphone.setError("Cannot be Empty");
        } else
            iphone.setErrorEnabled(false);
        if (edate.getText().toString().trim().isEmpty()) {
            flag = 1;
            idate.setError("Cannot be Empty");
        } else
            idate.setErrorEnabled(false);
        if (ebalance.getText().toString().trim().isEmpty()) {
            flag = 1;
            ibalance.setError("Cannot be Empty");
        } else
            ibalance.setErrorEnabled(false);

        if (flag == 1)
            return false;
        else
            return true;
    }

}