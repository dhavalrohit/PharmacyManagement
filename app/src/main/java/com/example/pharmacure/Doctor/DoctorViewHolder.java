package com.example.pharmacure.Doctor;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacure.R;

public class DoctorViewHolder extends RecyclerView.ViewHolder {

    TextView docotrname,specialisaion,area,mobileno,regno;
    ImageView deletebtn;
    LinearLayout gone;
    CardView cv;


    public DoctorViewHolder(@NonNull View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.doctorcv);

        docotrname=(TextView) itemView.findViewById(R.id.docotrname_cv);
        regno=(TextView) itemView.findViewById(R.id.regnodoctor_cv);
        area=(TextView) itemView.findViewById(R.id.doctorarea_cv);
        mobileno=(TextView) itemView.findViewById(R.id.mobilenodoctor_cv);
        specialisaion=(TextView) itemView.findViewById(R.id.doctorspecialisation_cv);
        deletebtn = (ImageView) itemView.findViewById(R.id.doctor_deletebutton);
    }
}
