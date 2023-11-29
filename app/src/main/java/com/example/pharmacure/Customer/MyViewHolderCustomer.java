package com.example.pharmacure.Customer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacure.R;


public class MyViewHolderCustomer extends RecyclerView.ViewHolder {

    TextView item, date, money, addr, phone;
    ImageView editBtn, delbtn;
    Button updateLogBtn, LogBtn;
    LinearLayout gone;
    CardView cv;

    public MyViewHolderCustomer(View itemView) {
        super(itemView);


        cv = (CardView) itemView.findViewById(R.id.customer_cv);
        gone = (LinearLayout) itemView.findViewById(R.id.gone_layout);

        item = (TextView) itemView.findViewById(R.id.Customername_cv);
        date = (TextView) itemView.findViewById(R.id.Customerdate_cv);
        money = (TextView) itemView.findViewById(R.id.Customermoney_cv);
        addr = (TextView) itemView.findViewById(R.id.Customeraddr_cv);
        phone = (TextView) itemView.findViewById(R.id.Customerphone_cv);

        editBtn = (ImageView) itemView.findViewById(R.id.customer_cv_editbutton);
        delbtn = (ImageView) itemView.findViewById(R.id.customer_cv_deletebutton);
        updateLogBtn = (Button) itemView.findViewById(R.id.customer_cv_update_log_btn);
        LogBtn = (Button) itemView.findViewById(R.id.customer_cv_logbtn);
    }

}
