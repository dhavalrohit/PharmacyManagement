package com.example.pharmacure.Model;

import java.util.ArrayList;

public class TransactionModel {

    private String Date;
    private String doctorName;

    private String patientName;
    private String total_bill_amount;
    private String bill_ID;

    private String patientAge;

    public ArrayList<BillModel> getBillItemsList() {
        return billItemsList;
    }

    public void setBillItemsList(ArrayList<BillModel> billItemsList) {
        this.billItemsList = billItemsList;
    }

    private ArrayList<BillModel> billItemsList;


    public TransactionModel(){

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTotal_bill_amount() {
        return total_bill_amount;
    }

    public void setTotal_bill_amount(String total_bill_amount) {
        this.total_bill_amount = total_bill_amount;
    }

    public String getBill_ID() {
        return bill_ID;
    }

    public void setBill_ID(String bill_ID) {
        this.bill_ID = bill_ID;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public TransactionModel(String date, String doctorName, String patientName, String total_bill_amount, String bill_ID, String patientAge, ArrayList<BillModel> billItemsList) {
        Date = date;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.total_bill_amount = total_bill_amount;
        this.bill_ID = bill_ID;
        this.patientAge = patientAge;
        this.billItemsList = billItemsList;
    }
}
