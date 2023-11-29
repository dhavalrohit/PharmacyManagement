package com.example.pharmacure.Model;

/**
 * Created by corei3 on 04-07-2017.
 */

public class MedicineModel {

    private String productName;
    private String batchNo;
    private String quantity;//quantity per packaging,10 tablets in one strip,quantity=10
    private String expiryDate;
    private String mrp;
    private String rate;
    private String companyName;
    private String productID;

    private String hsnNo;

    private String packaging;

    private String gstNo;
    private String discountperc;
    private String cstaxperc;

    public MedicineModel(String productName, String batchNo, String quantity, String expiryDate, String mrp, String rate, String companyName, String productID, String hsnNo, String packaging, String gstNo, String discountperc, String cstaxperc) {
        this.productName = productName;
        this.batchNo = batchNo;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.mrp = mrp;
        this.rate = rate;
        this.companyName = companyName;
        this.productID = productID;
        this.hsnNo = hsnNo;
        this.packaging = packaging;
        this.gstNo = gstNo;
        this.discountperc = discountperc;
        this.cstaxperc = cstaxperc;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getDiscountperc() {
        return discountperc;
    }

    public void setDiscountperc(String discountperc) {
        this.discountperc = discountperc;
    }

    public String getCstaxperc() {
        return cstaxperc;
    }

    public void setCstaxperc(String cstaxperc) {
        this.cstaxperc = cstaxperc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getHsnNo() {
        return hsnNo;
    }

    public void setHsnNo(String hsnNo) {
        this.hsnNo = hsnNo;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public MedicineModel(String productName, String batchNo, String quantity, String expiryDate, String mrp, String rate, String companyName, String productID, String hsnNo, String packaging) {
        this.productName = productName;
        this.batchNo = batchNo;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.mrp = mrp;
        this.rate = rate;
        this.companyName = companyName;
        this.productID = productID;
        this.hsnNo = hsnNo;
        this.packaging = packaging;
    }

    public MedicineModel(String productName, String batchNo, String quantity, String expiryDate, String mrp, String rate, String companyName){
        this.productName = productName;
        this.batchNo = batchNo;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.mrp = mrp;
        this.rate = rate;
        this.companyName = companyName;

    }

    public MedicineModel(){

    }
}
