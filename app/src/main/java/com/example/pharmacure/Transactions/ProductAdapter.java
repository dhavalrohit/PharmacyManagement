package com.example.pharmacure.Transactions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;
import com.example.pharmacure.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<MedicineModel> {


    Context context;
    ArrayList<MedicineModel> listModelsproduct = new ArrayList<>();

    int res;


    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MedicineModel> objects) {
        super(context, resource, objects);
        this.context=context;
        this.res=resource;
        this.listModelsproduct=objects;
    }


    private class ViewHolder {

        TextView  batchno, pack,expdate,mrp,productname,companyname;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       ViewHolder holder = null;
        final MedicineModel model = getItem(position);


        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(res, null);
            holder = new ViewHolder();

           holder.pack = (TextView) convertView.findViewById(R.id.packaging);
            holder.mrp = (TextView) convertView.findViewById(R.id.mrp);
            holder.expdate=(TextView) convertView.findViewById(R.id.expiry);
            holder.batchno=(TextView) convertView.findViewById(R.id.batchNo);


            convertView.setTag(holder);

        } else
            holder = (ProductAdapter.ViewHolder) convertView.getTag();


        holder.batchno.setText(model.getBatchNo());
        holder.mrp.setText(model.getMrp());
        holder.expdate.setText(model.getExpiryDate());
        holder.pack.setText(model.getPackaging());


        return convertView;

    }
}
