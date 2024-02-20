package com.example.pharmacure.Transactions.transHistory;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacure.R;


public class MyViewHolderTrans extends RecyclerView.ViewHolder {

    TextView transaction,date,customerName,amount,totalitems;
    ImageView deletebtn;
    LinearLayout gone;
    CardView cv;

    public  MyViewHolderTrans(View itemview){
        super(itemview);
        cv = (CardView) itemView.findViewById(R.id.transaction_cv);

        transaction = (TextView) itemView.findViewById(R.id.transactionId_cv);
        date = (TextView) itemView.findViewById(R.id.transactiondate_cv);
        amount = (TextView) itemView.findViewById(R.id.money);
        customerName = (TextView) itemView.findViewById(R.id.transactioncard_custname);
        totalitems=(TextView)itemview.findViewById(R.id.totalitemstext_cv) ;

        deletebtn = (ImageView) itemView.findViewById(R.id.transactioncard_deletebutton);


    }

}
