package com.example.healthhub.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Appointment {
    private String appointmentId; // Unique ID for the appointment
    private String doctorName; // Doctor's name
    private String specialist; // Doctor's specialization
    private String appointmentTime; // Appointment date and time (e.g., "2025-01-03 10:00 AM")
    private String userId; // User ID associated with the appointment
    private String doctorID; // Doctor ID (if needed for linking with doctor details)
    private String contact; // Contact details of the user/patient
    private String address; // Address for the appointment (optional)
    private int doctorImage; // Resource ID or image URL for the doctor's image


    // Constructor with parameters
    public Appointment(String doctorName, String date, String time) {
        this.doctorName = doctorName;
        this.appointmentTime = date + " " + time; // Combine date and time into appointmentTime
    }


    // Default constructor required for Firebase
    public Appointment() {
    }

    // Constructor to initialize all fields
    public Appointment(String appointmentId, String doctorName, String specialist, String appointmentTime,
                       String userId, String doctorID, String contact, String address, int doctorImage) {
        this.appointmentId = appointmentId;
        this.doctorName = doctorName;
        this.specialist = specialist;
        this.appointmentTime = appointmentTime;
        this.userId = userId;
        this.doctorID = doctorID;
        this.contact = contact;
        this.address = address;
        this.doctorImage = doctorImage;
    }

    // Getters and Setters

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(int doctorImage) {
        this.doctorImage = doctorImage;
    }

    // Utility Methods

    /**
     * Determines if the appointment is upcoming based on the current date and time.
     *
     * @return true if the appointment is in the future, false otherwise
     */
    public boolean isUpcoming() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date appointmentDate = sdf.parse(this.appointmentTime);
            Date currentDate = new Date();
            return appointmentDate != null && appointmentDate.after(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
