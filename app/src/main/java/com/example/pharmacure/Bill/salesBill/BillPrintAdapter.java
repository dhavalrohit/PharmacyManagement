package com.example.pharmacure.Bill.salesBill;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Model.TransactionModel;
import com.example.pharmacure.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillPrintAdapter extends ArrayAdapter<BillModel> {
    int res;
    Context context;

    ArrayList<BillModel> listModels = new ArrayList<>();

    public BillPrintAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BillModel> listModels) {
        super(context, resource, listModels);
        this.res = resource;
        this.listModels = listModels;
        this.context = context;



    }


    private class ViewHolder {

        TextView batchno,packaging,expiry,mrp,rate,gst,amount,productname,companyname,qty,looseqty,totalamount,sr,gstperc;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        final BillModel model = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView==null){

            convertView = mInflater.inflate(res, null);
            holder = new ViewHolder();
            holder.productname=convertView.findViewById(R.id.productname_bill);
            holder.companyname=convertView.findViewById(R.id.company_name_bill);
            holder.expiry=convertView.findViewById(R.id.expiry_bill);
            holder.batchno=convertView.findViewById(R.id.batch_bill);
            holder.packaging=convertView.findViewById(R.id.packaging_bill);
            holder.totalamount=convertView.findViewById(R.id.amount_bill);
            holder.qty=(TextView)convertView.findViewById(R.id.quantity_bill);
            holder.looseqty=(TextView) convertView.findViewById(R.id.loosequantity_bill);
            holder.gstperc=(TextView)convertView.findViewById(R.id.gstperc_bill);
            holder.sr=(TextView) convertView.findViewById(R.id.serialNo_bill);

            convertView.setTag(holder);

        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.productname.setText(model.getProductName());
        holder.companyname.setText(model.getCompanyName());
        holder.expiry.setText(model.getExpirydate());
        holder.batchno.setText(model.getBatch());
        holder.packaging.setText("packaging:"+model.getPackaging());
        holder.totalamount.setText(model.getTotalAmount());
        holder.qty.setText(model.getQuantity());
        holder.looseqty.setText(model.getLooseqty());
        holder.gstperc.setText(model.getGst()+"%");

        return convertView;
    }
}
