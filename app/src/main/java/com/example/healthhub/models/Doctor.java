package com.example.healthhub.models;

public class Doctor {
    private String doctorId; // Unique ID for the doctor
    private String about; // Description about the doctor
    private String awards; // Awards received by the doctor
    private String drName; // Doctor's name
    private String drAddress; // Doctor's address
    private String drImage; // URL or path to the doctor's image
    private String drPhone; // Doctor's phone number
    private String exp; // Experience of the doctor
    private double fee; // Consultation fee
    private String field; // Medical field or specialization
    private String workingTime; // Working hours of the doctor

    // Default constructor required for Firebase
    public Doctor() {}

    // Constructor to initialize all fields
    public Doctor(String doctorId, String about, String awards, String drName, String drAddress, String drImage, String drPhone, String exp, double fee, String field, String workingTime) {
        this.doctorId = doctorId;
        this.about = about;
        this.awards = awards;
        this.drName = drName;
        this.drAddress = drAddress;
        this.drImage = drImage;
        this.drPhone = drPhone;
        this.exp = exp;
        this.fee = fee;
        this.field = field;
        this.workingTime = workingTime;
    }

    // Getters and setters
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getDrAddress() {
        return drAddress;
    }

    public void setDrAddress(String drAddress) {
        this.drAddress = drAddress;
    }

    public String getDrImage() {
        return drImage;
    }

    public void setDrImage(String drImage) {
        this.drImage = drImage;
    }

    public String getDrPhone() {
        return drPhone;
    }

    public void setDrPhone(String drPhone) {
        this.drPhone = drPhone;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }
}