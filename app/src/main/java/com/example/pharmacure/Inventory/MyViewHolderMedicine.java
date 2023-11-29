    package com.example.pharmacure.Inventory;


    import android.view.View;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import androidx.cardview.widget.CardView;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.pharmacure.R;

    public class MyViewHolderMedicine extends RecyclerView.ViewHolder {

        TextView productName, btc, qty, mrp, exp, rate, cName, productID, hsnno,  packaging;
        ImageView editBtn, delbtn;
        LinearLayout gone;
        CardView cv;

        public MyViewHolderMedicine(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            gone = (LinearLayout) itemView.findViewById(R.id.gone_layout);

            productName = (TextView) itemView.findViewById(R.id.itemName);
            btc = (TextView) itemView.findViewById(R.id.batchText_cardLayout);
            qty = (TextView) itemView.findViewById(R.id.qtyText);
            mrp = (TextView) itemView.findViewById(R.id.mrpText_cardlayout);
            exp = (TextView) itemView.findViewById(R.id.expText_cardlayout);
            rate = (TextView) itemView.findViewById(R.id.spText);
            cName = (TextView) itemView.findViewById(R.id.cNameText);
            mrp = (TextView) itemView.findViewById(R.id.cpText);
            productID = (TextView) itemView.findViewById(R.id.codeText);
            hsnno = (TextView) itemView.findViewById(R.id.descText);
            packaging = (TextView) itemView.findViewById(R.id.unitText);

            editBtn = (ImageView) itemView.findViewById(R.id.editbtn);
            delbtn = (ImageView) itemView.findViewById(R.id.delbtn);
        }
    }
