package com.example.pharmacure.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pharmacure.Model.BillModel;
import com.example.pharmacure.Transactions.newTransaction.BillAdapter;
import com.example.pharmacure.Bill.salesBill.BillPrintAdapter;
import com.example.pharmacure.Transactions.newTransaction.ProductAdapter;

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

    public static void setListViewHeightBasedOnChildren_bill(ListView listView) {
        BillPrintAdapter listAdapter = (BillPrintAdapter) listView.getAdapter();
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


    public static double calculatetaxablevalue(double mrp,int quantity,double sgstvalue,double cgstvalue,double discountvalue){
        double taxablevalue=(mrp*quantity)-sgstvalue-cgstvalue-discountvalue;
        return taxablevalue;
    }
    public static double calculatediscountvalue(double mrp,int unit,int quantity,double sgstvalue,double cgstvalue,double taxablevalue){
        //double discountvalue=(mrp*quantity/unit)-sgstvalue-cgstvalue-taxablevalue;
        double discountvalue=(mrp*(Double.parseDouble(String.valueOf(quantity))/Double.parseDouble(String.valueOf(unit))))-sgstvalue-cgstvalue-taxablevalue;
        return discountvalue;
    }
    public static double calucatediscperc(double discountvalue,double mrp,int unit,int quantity){
        double discperc=(discountvalue/mrp*((Double.parseDouble(String.valueOf(quantity))/Double.parseDouble(String.valueOf(unit)))))*100;
        return discperc;
    }

    double setdiscperc=5.00;
    //double calculateddiscvalue=(rateperunit*finalquantity*setdiscperc)*100.00;
    //double finalamount=(rateperunit*finalquantity)-calculateddiscvalue;
    //double taxvalue=(rateperunit*finalquantity)*(Double.parseDouble(model.getGst())/100.00+Double.parseDouble(model.getGst()));
   // double valuewithouttax=rateperunit*finalamount-taxvalue;
    //double calctaxvalue=valuewithouttax-(valuewithouttax*setdiscperc/100.00);

    /*public static double calctaxvalue(double taxrate,double rateperunit,double discper,double quantity){
        double calculateddiscvalue=(rateperunit*quantity*discper)/100.00;
        //168.50*5/100=8.425
        double finalamount=(rateperunit*quantity)-calculateddiscvalue;
        //168.50-8.425=160.07
        double taxvalue=(rateperunit*quantity)*(taxrate/100.00+taxrate);
        //168*12/112= 18.05
        double valuewithouttax=(rateperunit*quantity)-taxvalue;
        //168.50-18.05=150.45
        //150.45-(150.45*5/100)
        return valuewithouttax-(valuewithouttax*(discper/100.00));

    }*/

    public static double calctaxvalue(double taxrate, double rateperunit, double discper, double quantity){
        double calculateddiscvalue = (rateperunit * quantity * discper) / 100.00;
        double finalamount = (rateperunit * quantity) - calculateddiscvalue;

        double taxvalue = (rateperunit * quantity) * (taxrate / 100.00);

        double valuewithouttax = (rateperunit * quantity) - taxvalue;

        return valuewithouttax - (valuewithouttax * (discper / 100.00));
    }




}
