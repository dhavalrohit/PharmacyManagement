package com.example.pharmacure.Model;

public class BillModel {
    private String productName;
    private String rate;
    private String quantity;
    private String discperc;

    public String getDiscperc() {
        return discperc;
    }

    public void setDiscperc(String discperc)
    {
        this.discperc = discperc;
    }

    public String getHsnocode() {
        return hsnocode;
    }

    public void setHsnocode(String hsnocode) {
        this.hsnocode = hsnocode;
    }

    private String hsnocode;
    private String totalAmount;
    private String batch;
    private String expirydate;
    private String key;
    private String originalQuantity;
    private String packaging;
    private String gst;
    private String looseqty;

    private String mrp;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getLooseqty() {
        return looseqty;
    }

    public void setLooseqty(String looseqty) {
        this.looseqty = looseqty;
    }



    String amount;


    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    private String companyName;




    public BillModel(String productName, String rate, String quantity, String batch, String expirydate, String key, String originalQuantity, String packaging, String gst, String looseqty, String mrp, String totalAmount, String companyName, String hsnocode, String discperc) {
        this.productName = productName;
        this.rate = rate;
        this.quantity = quantity;
        this.batch = batch;
        this.expirydate = expirydate;
        this.key = key;
        this.originalQuantity = originalQuantity;
        this.packaging = packaging;
        this.gst = gst;
        this.looseqty = looseqty;
        this.discperc=discperc;
        this.hsnocode=hsnocode;
        this.mrp = mrp;
        this.totalAmount = totalAmount;
        this.companyName = companyName;
    }



    public BillModel(String productName, String rate, String quantity, String batch, String expirydate, String packaging, String gst, String looseqty, String mrp, String totalAmount, String companyName,String hsncode,String discperc) {
        this.productName = productName;
        this.rate = rate;
        this.quantity = quantity;
        this.batch = batch;
        this.expirydate = expirydate;
        this.packaging = packaging;
        this.gst = gst;
        this.looseqty = looseqty;
        this.mrp = mrp;
        this.totalAmount = totalAmount;
        this.companyName = companyName;
        this.discperc=discperc;
        this.hsnocode=hsncode;
    }









    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(String originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }


    public BillModel(){

    }

}
