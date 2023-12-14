package com.example.pharmacure.Transactions.newTransaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.pharmacure.Model.CustomerModel;
import com.example.pharmacure.R;

import java.util.ArrayList;
import java.util.List;

public class AutoCustomerAdapter extends ArrayAdapter<CustomerModel> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<CustomerModel> modelcust, tempModels, suggestions;
    String custID = "", balanceamount = "", customerName = "";

    public AutoCustomerAdapter(Context context, int resource, int textViewResourceId, ArrayList<CustomerModel> modelcust) {

        super(context, resource, textViewResourceId, modelcust);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.modelcust = modelcust;
        // tempItems = new ArrayList<Model>(items);
        this.tempModels = (ArrayList<CustomerModel>) modelcust.clone();
        this.suggestions = new ArrayList<CustomerModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autotext_list, parent, false);
        }
        final CustomerModel model = modelcust.get(position);
        if (model != null) {

            TextView item = (TextView) view.findViewById(R.id.item_name);

            if (item != null)

                item.setText(model.getCustomerName() + "-" + "( " + model.getAmount() + " )");
        }
        return view;
    }

    @Override
    public Filter getFilter() {

        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CustomerModel) resultValue).getCustomerName();
            custID = ((CustomerModel) resultValue).getCustomerID();
            balanceamount = ((CustomerModel) resultValue).getAmount();
            customerName = ((CustomerModel) resultValue).getCustomerName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CustomerModel model : tempModels) {
                    if (model.getCustomerName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<CustomerModel> filterList = (ArrayList<CustomerModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CustomerModel model : filterList) {
                    add(model);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public String[] detcust() {
        String[] str = { custID, balanceamount, customerName };
        return str;
    }
}
