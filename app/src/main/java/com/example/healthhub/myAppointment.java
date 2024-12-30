package com.example.healthhub;

public class myAppointment {
    private String doctorName;
    private String specialist;
    private String appointmentTime;
    private int doctorImage;

    public myAppointment(String doctorName, String specialist, String appointmentTime, int doctorImage) {
        this.doctorName = doctorName;
        this.specialist = specialist;
        this.appointmentTime = appointmentTime;
        this.doctorImage = doctorImage;
    }

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
