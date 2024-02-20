package com.example.pharmacure.Bill.taxBill;

import java.util.ArrayList;

public interface DatatransferInterface {
    public void setdiscountvalues(double discvalue);
    public void settotalqty(int qty);


    public void settotal_taxablevalue(double total);
    public void settotalcgstvalue(double cgstvalue);
    public void settotalsgstvalue(double sgstvalue);
    public void settotalbillamount(double total);
    public void setsgstrate(String rate);
    public void setcgstrate(String rate);


    public double getdiscountvalues();
    public int gettotalqty();
    public double gettotal_taxable_value();
    public double gettotalcgstvalue();
    public double gettotalsgstvalue();
    public double gettotalbillamount();

}
