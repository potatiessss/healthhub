package com.example.healthhub;

public class Appointment {
    private String doctorName;
    private String specialist;
    private String appointmentTime;
    private int doctorImage; // Resource ID for the doctor's image

    public Appointment(String doctorName, String specialist, String appointmentTime, int doctorImage) {
        this.doctorName = doctorName;
        this.specialist = specialist;
        this.appointmentTime = appointmentTime;
        this.doctorImage = doctorImage;
    }

    // Getters
    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialist() {
        return specialist;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public int getDoctorImage() {
        return doctorImage;
    }
}
