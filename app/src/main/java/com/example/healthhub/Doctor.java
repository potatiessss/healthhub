package com.example.healthhub;

public class Doctor {
    private int id;
    private String name;
    private String hospitalAddress;
    private String field;
    private int experience;
    private String mobileNo;
    private double consultationFee;
    private String about;
    private String workingTime;
    private int profilePic; // Resource ID or URL
    private int awards;

    // Constructor
    public Doctor(int id, String name, String hospitalAddress, String field, int experience, String mobileNo,
                  double consultationFee, String about, String workingTime, int profilePic, int awards) {
        this.id = id;
        this.name = name;
        this.hospitalAddress = hospitalAddress;
        this.field = field;
        this.experience = experience;
        this.mobileNo = mobileNo;
        this.consultationFee = consultationFee;
        this.about = about;
        this.workingTime = workingTime;
        this.profilePic = profilePic;
        this.awards = awards;
    }

    public Doctor(int id, String name, String field, String profilePic) {
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getHospitalAddress() { return hospitalAddress; }
    public String getField() { return field; }
    public int getExperience() { return experience; }
    public String getMobileNo() { return mobileNo; }
    public double getConsultationFee() { return consultationFee; }
    public String getAbout() { return about; }
    public String getWorkingTime() { return workingTime; }
    public int getProfilePic() { return profilePic; }
    public int getAwards() { return awards; }
}
