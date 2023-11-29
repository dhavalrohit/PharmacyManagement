package com.example.pharmacure.Model;

public class TransactionModel {

    private String transactionDate;
    private String doctorName;
    private String medicineDetails;
    private String patientName;
    private String totalAmount;
    private String transactionID;
    private String key;
    private String patientAge;


    public TransactionModel(){

    }

    public TransactionModel(String transactionDate, String doctorName, String medicineDetails, String patientName, String totalAmount, String transactionID, String key, String patientAge) {
        this.transactionDate = transactionDate;
        this.doctorName = doctorName;
        this.medicineDetails = medicineDetails;
        this.patientName = patientName;
        this.totalAmount = totalAmount;
        this.transactionID = transactionID;
        this.key = key;
        this.patientAge = patientAge;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicineDetails() {
        return medicineDetails;
    }

    public void setMedicineDetails(String medicineDetails) {
        this.medicineDetails = medicineDetails;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }
}
