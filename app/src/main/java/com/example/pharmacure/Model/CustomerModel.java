package com.example.pharmacure.Model;

public class CustomerModel {

    private String customerName;
    private String mobileno;
    private String date;
    private String amount;

    private String address;

    private String log;

    private String customerID;

    public CustomerModel() {

    }

    public CustomerModel(String customerName, String mobileno, String date, String amount, String address, String log, String customerID) {
        this.customerName = customerName;
        this.mobileno = mobileno;
        this.date = date;
        this.amount = amount;
        this.address = address;
        this.log = log;
        this.customerID = customerID;
    }
    public CustomerModel(String customerName, String mobileno, String date, String amount, String address, String customerID) {
        this.customerName = customerName;
        this.mobileno = mobileno;
        this.date = date;
        this.amount = amount;
        this.address = address;

        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
