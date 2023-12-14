package com.example.pharmacure.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacure.Model.DoctorModel;
import com.example.pharmacure.Model.TransactionModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Transactions.transHistory.CustomFilterTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorViewHolder>  {

    Context con;

    String uid;
    ArrayList<DoctorModel> models, filterList;
    DatabaseReference db;
    private FirebaseAuth mAuth;



    public DoctorAdapter(Context con,ArrayList<DoctorModel> models){
        this.con=con;
        this.models=models;


    }




    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(con).inflate(R.layout.doctor_cv,parent,false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, final int position) {

        holder.docotrname.setText(models.get(holder.getAdapterPosition()).getDoctorName());
        holder.specialisaion.setText(models.get(holder.getAdapterPosition()).getSpecialisation());
        holder.mobileno.setText(models.get(holder.getAdapterPosition()).getMobileno());
        holder.area.setText(models.get(holder.getAdapterPosition()).getLocality());
        holder.regno.setText(models.get(holder.getAdapterPosition()).getRegno());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
