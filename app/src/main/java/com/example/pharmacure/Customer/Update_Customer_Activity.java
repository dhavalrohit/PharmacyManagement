package com.example.pharmacure.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.pharmacure.Utils.DecimalDigitsInputFilter;
import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Update_Customer_Activity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout iName, iaddr, iphone, ibalance, idate;
    EditText eName, eaddr, ephone, ebalance, edate;
    private int mYear, mMonth, mDay;
    private DatabaseReference du;
    String s;
    private FirebaseAuth mAuth;
    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_customer);

        setTitle("Update Customer");
        s = getIntent().getStringExtra("Keys");

        iName = (TextInputLayout) findViewById(R.id.nameLayout);
        iaddr = (TextInputLayout) findViewById(R.id.addrLayout);
        iphone = (TextInputLayout) findViewById(R.id.phoneLayout);
        ibalance = (TextInputLayout) findViewById(R.id.balanceLayout);
        idate = (TextInputLayout) findViewById(R.id.dateLayout);

        eName = (EditText) findViewById(R.id.nameText);
        eaddr = (EditText) findViewById(R.id.addrText);
        ephone = (EditText) findViewById(R.id.phoneText);
        ebalance = (EditText) findViewById(R.id.balanceText);
        edate = (EditText) findViewById(R.id.dateText);

        edate.setInputType(InputType.TYPE_NULL);
        edate.setFocusable(false);
        edate.setOnClickListener(this);

        ebalance.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });

        mAuth = Firebase_factory.getFirebaseAuth_Instance();
        uid = Firebase_factory.getfbUserId();
        du = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Customer").child("Customers")
                .child(s);
        //du.keepSynced(true);
        du.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                };
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);

                eName.setText(map.get("customerName"));
                eaddr.setText(map.get("address"));
                ephone.setText(map.get("mobileno"));
                ebalance.setText(map.get("amount"));
                edate.setText(map.get("date"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

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
        if (id == R.id.del) {

            du.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(Update_Customer_Activity.this);
                    alert.setTitle("Alert!!");
                    alert.setMessage("Are you sure to delete this customer?");
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
                public void onCancelled(DatabaseError error) {
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
 }

    public void updateInfo(View view) {
        String sName = eName.getText().toString();
        String saddr = eaddr.getText().toString();
        String sphone = ephone.getText().toString();
        final String sbalance = ebalance.getText().toString();
        String sdate = edate.getText().toString();

        if (validate()) {

            du.child("customerName").setValue(sName);
            du.child("mobileno").setValue(sphone);
            du.child("date").setValue(sdate);
            du.child("amount").setValue(sbalance);
            du.child("address").setValue(saddr);

            du.child("log").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = (String) dataSnapshot.getValue();
                    // do your stuff here with value
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    du.child("log").setValue(value + "[" + format.format(calendar.getTime()) + "] "
                            + "Account updated. Balance :" + sbalance + "\n");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            // log
            add_log("Customer ");

            finish();
        }
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

    public void clear(View v) {
        m_clear();
    }

    public void m_clear() {
        eName.setText("");
        eaddr.setText("");
        ephone.setText("");
        ebalance.setText("");
        edate.setText("");
    }

}