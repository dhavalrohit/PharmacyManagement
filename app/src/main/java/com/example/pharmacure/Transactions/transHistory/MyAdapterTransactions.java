package com.example.pharmacure.Transactions.transHistory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacure.Bill.salesBill.Bill_Activity;
import com.example.pharmacure.Bill.taxBill.taxBill_Activity;
import com.example.pharmacure.R;
import com.example.pharmacure.Model.TransactionModel;
import com.example.pharmacure.sample.sampleactivity;
import com.factory.Firebase_factory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapterTransactions extends RecyclerView.Adapter<MyViewHolderTrans> implements Filterable {

    Context con;

    String uid;
    ArrayList<TransactionModel> models, filterList;
    DatabaseReference db,dbcount;
    private FirebaseAuth mAuth;
    CustomFilterTransaction filter;
    public static String discper="";

    public MyAdapterTransactions(Context con, ArrayList<TransactionModel> models){
        this.con=con;
        this.models=models;
        this.filterList=models;
    }


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilterTransaction(this, filterList);
        }
        return filter;
    }

    @NonNull
    @Override
    public MyViewHolderTrans onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(con).inflate(R.layout.transcard,parent,false);

        return new MyViewHolderTrans(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderTrans holder, @SuppressLint("RecyclerView") final int position) {




        holder.transaction.setText("Bill ID:"+models.get(holder.getAdapterPosition()).getBill_ID());
        holder.date.setText(models.get(holder.getAdapterPosition()).getDate());
        holder.amount.setText(models.get(holder.getAdapterPosition()).getTotal_bill_amount());
        holder.customerName.setText(models.get(holder.getAdapterPosition()).getPatientName());

        holder.totalitems.setText((models.get(holder.getAdapterPosition()).getBillItemsList().size()-1)+"");

        mAuth= Firebase_factory.getFirebaseAuth_Instance();
        uid=Firebase_factory.getfbUserId();
        db= Firebase_factory.getdatabaseRef().child("Users").child(uid).child("Sales").child("salesBill")
                .child("billList")
                .child(models.get(position).getBill_ID());





        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*    Intent i=new Intent(con, sampleactivity.class);
                con.startActivity(i);*/


                Intent i = new Intent(con, taxBill_Activity.class);

                discper="5";
                i.putExtra("billID", models.get(holder.getAdapterPosition()).getBill_ID());
                con.startActivity(i);
            }

            /*@Override
            public void onClick(View v) {
                Intent i = new Intent(con, Bill_Activity.class);
                i.putExtra("billID", models.get(holder.getAdapterPosition()).getBill_ID());
                con.startActivity(i);
            }*/
        });

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String transID=models.get(holder.getAdapterPosition()).getBill_ID();
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AlertDialog.Builder alert=new AlertDialog.Builder(con);
                        alert.setTitle("Alert!!");
                        alert.setMessage("Are you sure to delete this transaction?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                     snapshot.getRef().removeValue();
                                     dialog.dismiss();
                                     models.remove(holder.getAdapterPosition());
                                     notifyItemRemoved(holder.getAdapterPosition());
                                     notifyItemRangeChanged(holder.getAdapterPosition(),models.size());
                                     add_log("Transaction Deleted");
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                Toast.makeText(con, transID + " has been removed succesfully.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
         return models.size();
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

}
