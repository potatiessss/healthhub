package com.example.healthhub.models;

public class Appointment {
    private String appointmentId; // Added field for appointment ID
    private String doctorID;
    private String fullname;
    private String address;
    private String contact;
    private String date;
    private String time;

    // Default constructor required for Firebase
    public Appointment() {
    }

    // Constructor to initialize all fields
    public Appointment(String appointmentId, String doctorID, String fullname, String address, String contact, String date, String time) {
        this.appointmentId = appointmentId;
        this.doctorID = doctorID;
        this.fullname = fullname;
        this.address = address;
        this.contact = contact;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}