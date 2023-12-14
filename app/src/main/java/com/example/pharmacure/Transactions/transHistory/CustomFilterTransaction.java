package com.example.pharmacure.Transactions.transHistory;

import android.widget.Filter;

import com.example.pharmacure.Model.TransactionModel;

import java.util.ArrayList;

public class CustomFilterTransaction extends Filter {

    MyAdapterTransactions adapter;
    ArrayList<TransactionModel> filterList;


    public CustomFilterTransaction(MyAdapterTransactions adapter, ArrayList<TransactionModel> filterList) {
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
            ArrayList<TransactionModel> filteredPlayers = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                // CHECK
                if (filterList.get(i).getBill_ID().toUpperCase().contains(constraint)) {
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
        adapter.models = (ArrayList<TransactionModel>) results.values;
        // REFRESH

        adapter.notifyDataSetChanged();


    }
}
