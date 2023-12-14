package com.example.pharmacure.Model;

public class DoctorModel {

    private String doctorName;
    private String locality;
    private String regno;
    private String mobileno;
    private String specialisation;
    private String doctorID;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public DoctorModel(String doctorName, String locality, String regno, String mobileno, String specialisation, String doctorID) {
        this.doctorName = doctorName;
        this.locality = locality;
        this.regno = regno;
        this.mobileno = mobileno;
        this.specialisation = specialisation;
        this.doctorID = doctorID;
    }

    public DoctorModel() {
    }
}
