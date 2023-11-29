package com.example.pharmacure.Inventory;

import android.annotation.SuppressLint;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapterMedicine extends RecyclerView.Adapter<MyViewHolderMedicine>  implements Filterable {

    Context context;

    String uid;
    ArrayList<MedicineModel> models, filterList;
    DatabaseReference db;
    private FirebaseAuth mAuth;
    MedicineFilter filter;

    public MyAdapterMedicine(Context context, ArrayList<MedicineModel> models) {
        this.context = context;
        this.models = models;
        this.filterList = models;

    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public MyViewHolderMedicine onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolderMedicine(v);

    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderMedicine holder, @SuppressLint("RecyclerView") final int position) {

        holder.productName.setText(models.get(holder.getAdapterPosition()).getProductName());
        holder.btc.setText(models.get(holder.getAdapterPosition()).getBatchNo());
        holder.exp.setText(models.get(holder.getAdapterPosition()).getExpiryDate());
        holder.mrp.setText(models.get(holder.getAdapterPosition()).getMrp());
        holder.qty.setText(models.get(holder.getAdapterPosition()).getQuantity());
        holder.rate.setText(models.get(holder.getAdapterPosition()).getRate());
        holder.cName.setText(models.get(holder.getAdapterPosition()).getCompanyName());
        holder.productID.setText(models.get(holder.getAdapterPosition()).getProductID());
        holder.packaging.setText(models.get(holder.getAdapterPosition()).getPackaging());

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
                Intent i = new Intent(context, Update_Item_Activity.class);
                i.putExtra("Keys", models.get(holder.getAdapterPosition()).getProductID());
                context.startActivity(i);

            }
        });

        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String item = models.get(holder.getAdapterPosition()).getProductName();
                mAuth = FirebaseAuth.getInstance();
                uid = mAuth.getCurrentUser().getUid();
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Inventory")
                        .child("productList").child(models.get(position).getProductID());
                db.keepSynced(true);
                db.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Alert!!");
                        alert.setMessage("Are you sure to delete this item?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dataSnapshot.getRef().removeValue();
                                dialog.dismiss();
                                models.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), models.size());

                                // log
                                add_log("Item Deleted");

                                Toast.makeText(context, item + " has been removed succesfully.", Toast.LENGTH_SHORT).show();
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
            }

        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void add_log(String message){

        DatabaseReference dblog=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Log");


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
