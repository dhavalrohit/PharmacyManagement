package com.example.pharmacure.Customer;

import android.widget.Filter;

import com.example.pharmacure.Model.CustomerModel;

import java.util.ArrayList;

public class CustomFilterCustomer extends Filter {

    MyAdapterCustomer adapter;
    ArrayList<CustomerModel> filterList;

    public CustomFilterCustomer(ArrayList<CustomerModel> filterList, MyAdapterCustomer adapter) {
        this.adapter = adapter;
        this.filterList = filterList;

    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            // CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            // STORE OUR FILTERED PLAYERS
            ArrayList<CustomerModel> filteredPlayers = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                // CHECK
                if (filterList.get(i).getCustomerName().toUpperCase().contains(constraint)) {
                    // ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.models = (ArrayList<CustomerModel>) results.values;
        // REFRESH
        adapter.notifyDataSetChanged();

    }
}
