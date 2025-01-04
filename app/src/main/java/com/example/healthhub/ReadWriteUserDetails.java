package com.example.healthhub;

public class ReadWriteUserDetails {
    public String fullname, dob, gender;

    //Constructor
    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textFullName, String textDOB, String textGender){
        this.fullname = textFullName;
        this.dob = textDOB;
        this.gender = textGender;
    }
}
