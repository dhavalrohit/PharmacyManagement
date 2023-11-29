package com.example.pharmacure.Transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pharmacure.Model.MedicineModel;
import com.example.pharmacure.R;

import java.util.ArrayList;
import java.util.List;

public class AutoAdapterMedicine extends ArrayAdapter<MedicineModel> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<MedicineModel> items, tempItems, suggestions;
    String productName = "", mrp = "", packaging = "", expdate = "", batch = "", key = "", oqty="", productID = "",gstNo="",rate="";

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    String companyName="";

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public AutoAdapterMedicine(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<MedicineModel> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.resource=resource;
        this.textViewResourceId=textViewResourceId;
        this.items=objects;
        this.tempItems= (ArrayList<MedicineModel>) objects.clone();
        this.suggestions=new ArrayList<MedicineModel>();


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autotext_list, parent, false);
        }
        final MedicineModel model = items.get(position);
        if (model != null) {

            TextView item = (TextView) view.findViewById(R.id.item_name);

            if (item != null)

                item.setText(model.getProductName() + " (" + model.getProductID() + ")");
        }
        return view;
    }

    @Override
    public Filter getFilter() {

        return nameFilter;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((MedicineModel) resultValue).getProductName() + " (" + ((MedicineModel) resultValue).getProductID() + ")";
            productName = ((MedicineModel) resultValue).getProductName();
            mrp = ((MedicineModel) resultValue).getMrp();
            expdate = ((MedicineModel) resultValue).getExpiryDate();
            batch = ((MedicineModel) resultValue).getBatchNo();
            key = ((MedicineModel) resultValue).getProductID();
            oqty = ((MedicineModel) resultValue).getQuantity();
            packaging = ((MedicineModel) resultValue).getPackaging();
            productID = ((MedicineModel) resultValue).getProductID();
            companyName=((MedicineModel)resultValue).getCompanyName();
            gstNo=((MedicineModel)resultValue).getCstaxperc();
            rate=((MedicineModel)resultValue).getRate();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (MedicineModel model : tempItems) {
                    if (model.getProductName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(model);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<MedicineModel> filterList = (ArrayList<MedicineModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (MedicineModel model : filterList) {
                    add(model);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public String[] det() {
        String[] str = { productName, mrp, batch, expdate, key, oqty, packaging, productID };
        return str;
    }

    public String getProductName() {
        return productName;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getOqty() {
        return oqty;
    }

    public void setOqty(String oqty) {
        this.oqty = oqty;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
