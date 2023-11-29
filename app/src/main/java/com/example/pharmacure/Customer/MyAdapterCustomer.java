package com.example.pharmacure.Customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacure.Model.CustomerModel;
import com.example.pharmacure.Utils.DecimalDigitsInputFilter;
import com.example.pharmacure.R;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MyAdapterCustomer extends RecyclerView.Adapter<MyViewHolderCustomer> implements Filterable {

    Context con;

    String uid;
    ArrayList<CustomerModel> models, filterList;
    DatabaseReference db;
    private FirebaseAuth mAuth;
    CustomFilterCustomer filter;

    public MyAdapterCustomer(Context con,ArrayList<CustomerModel> models) {
        this.con = con;
        this.models = models;
        this.filterList = models;

    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilterCustomer(filterList, this);
        }
        return filter;
    }

    @NonNull
    @Override
    public MyViewHolderCustomer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(con).inflate(R.layout.customercard, parent, false);
        return new MyViewHolderCustomer(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCustomer holder, @SuppressLint("RecyclerView") int position) {

        holder.item.setText(models.get(holder.getAdapterPosition()).getCustomerName());
        holder.date.setText(models.get(holder.getAdapterPosition()).getDate());
        holder.money.setText(models.get(holder.getAdapterPosition()).getAmount());
        holder.addr.setText(models.get(holder.getAdapterPosition()).getAddress());
        holder.phone.setText(models.get(holder.getAdapterPosition()).getMobileno());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.gone.getVisibility() == View.VISIBLE) {
                    holder.gone.setVisibility(View.GONE);
                } else {
                    holder.gone.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(con, Update_Customer_Activity.class);
                i.putExtra("Keys", models.get(position).getCustomerID());
                con.startActivity(i);
            }
        });

        holder.LogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(con, Customer_Log_Activity.class);
                i.putExtra("k", models.get(position).getCustomerID());
                con.startActivity(i);

            }
        });

        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String item = models.get(holder.getAdapterPosition()).getCustomerName();
                mAuth = Firebase_factory.getFirebaseAuth_Instance();
                uid = Firebase_factory.getfbUserId();
                db = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Customer")
                        .child("Customers").child(models.get(holder.getAdapterPosition()).getCustomerID());
                db.keepSynced(true);

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(con);
                        alert.setTitle("Alert!!");
                        alert.setMessage("Are you sure to delete this customer?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                snapshot.getRef().removeValue();
                                dialog.dismiss();
                                models.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, models.size());
                                add_log("Customer Deleted");

                                Toast.makeText(con, item + " has been removed succesfully.", Toast.LENGTH_SHORT).show();

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

            }
        });


        holder.updateLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String item = models.get(position).getCustomerName();
                mAuth = Firebase_factory.getFirebaseAuth_Instance();
                uid = Firebase_factory.getfbUserId();
                db = Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Customer")
                        .child("Customers").child(models.get(position).getCustomerID());
                db.keepSynced(true);
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(con);
                        final EditText edittext = new EditText(con);
                        edittext.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });

                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        final Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);

                        edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        edittext.setMaxLines(1);
                        edittext.setHint("Enter amount");
                        // alert.setMessage("Enter amount :");
                        // alert.setTitle("Enter amount :");

                        alert.setView(edittext);

                        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // What ever you want to do with the value
                                final Editable value = edittext.getText();
                                if (!value.toString().trim().isEmpty()) {
                                    BigDecimal a = new BigDecimal(value.toString());
                                    BigDecimal due = BigDecimal.valueOf(Double.parseDouble(map.get("amount"))).add(a);

                                    db.child("amount").setValue(due.toString());

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c.getTime());
                                    db.child("edate").setValue(formattedDate);

                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                    db.child("log").setValue(map.get("log") + "[" + format.format(calendar.getTime())
                                            + "] (" + value.toString() + ") Balance:" + due.toString() + "\n");

                                    // log
                                    add_log(map.get("customerName")
                                            + " Customer Balance updated:"
                                            + value.toString() );

                                    notifyDataSetChanged();
                                }

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                                dialog.dismiss();
                            }
                        });
                        AlertDialog b = alert.create();
                        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        b.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }

                });

            }
        });



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
    @Override
    public int getItemCount() {
   return models.size();
    }
}
