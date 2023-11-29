package com.example.pharmacure.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Transactions.BillAdapter;
import com.example.pharmacure.Transactions.ProductAdapter;

import java.util.ArrayList;

public class Utility {


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        BillAdapter listAdapter = (BillAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setListViewHeightBasedOnChildren_product(ListView listView) {
        ProductAdapter listAdapter = (ProductAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static double calculatetotalamount_bill(ArrayList<BillModel> billlistmodel){
        int childrencount=billlistmodel.size();
        double totalamount=0.0;
        for (int i=0;i<childrencount;i++){
            totalamount=totalamount+(Double.valueOf(billlistmodel.get(i).getAmount()));

        }
        return totalamount;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double calculate_base_amount(double rate,int quantity,double hs_amount){
        double base_amount=rate*quantity-hs_amount;
        return base_amount;
    }


    public static double calculatetax(double baseamount,int taxrate){
        double taxamount=(baseamount*taxrate)/100;
        return taxamount;
    }

    public static double calculate_discount(double baseamount,int discountpercentage){
        double discountamount=(baseamount*discountpercentage)/100;
        return discountamount;
    }

    public static double calculate_total_amount(double basamount,double cstaxamount,double discountamount){
        double totalamount=basamount+cstaxamount-discountamount;
        return totalamount;
    }


}
