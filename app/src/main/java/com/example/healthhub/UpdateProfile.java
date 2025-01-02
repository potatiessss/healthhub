package com.example.healthhub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    EditText ET_UpdateName, ET_DOB;
    RadioGroup RG_UpdateGender;
    RadioButton RB_UpdatedGenderSelected;
    String textFName, textDOB, textGender;
    FirebaseAuth authProfile;
    ProgressBar progressBar;
    TextView changeEmail, uploadPic, changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);


        //Upload Pic TV
        uploadPic = findViewById(R.id.TV_uploadProfilepic);
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateProfile.this, "You can upload your profile pic now.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateProfile.this, UploadProfilePic.class));
            }
        });

        // Change Email TV
        changeEmail = findViewById(R.id.TV_updateEmail);
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateProfile.this, "You can change your email now.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateProfile.this, ChangeEmail.class));
            }
        });

        // Change Password TV
        changePassword = findViewById(R.id.TV_changePW); // Correct assignment
        changePassword.setOnClickListener(new View.OnClickListener() { // Correctly referencing changePassword
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateProfile.this, "You can change your password now.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateProfile.this, ChangePassword.class));
            }
        });


        progressBar = findViewById(R.id.progressBar);
        ET_UpdateName = findViewById(R.id.ET_UpdatedName);
        ET_DOB = findViewById(R.id.ET_UpdatedDOB);

        RG_UpdateGender = findViewById(R.id.RG_UpdateGender);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //Show Profile Data
        showProfile(firebaseUser);

        //Setting up DatePicker on EditText
        ET_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Extracting saved dd/mm/yyyy into different variable by creating an array delimited by "/"
                String textSADoB[] = textDOB.split("/");

                int day = Integer.parseInt(textSADoB[0]);
                int month = Integer.parseInt(textSADoB[1])-1; //to take care of the month index
                int year = Integer.parseInt(textSADoB[2]);;

                DatePickerDialog picker;

                //Date Picker Dialog
                picker = new DatePickerDialog(UpdateProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ET_DOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                picker.show();

            }
        });

        //Update Profile Button
        Button UpdateProfileButton = findViewById(R.id.updateProfile_btn);
        UpdateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }

            private void updateProfile(FirebaseUser firebaseUser) {
                int selectedGenderID = RG_UpdateGender.getCheckedRadioButtonId();
                RB_UpdatedGenderSelected = findViewById(selectedGenderID);


                if(TextUtils.isEmpty(textFName)){
                    Toast.makeText(UpdateProfile.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    ET_UpdateName.setError("Full Name is required");
                    ET_UpdateName.requestFocus();
                }else if(TextUtils.isEmpty(textDOB)){
                    Toast.makeText(UpdateProfile.this, "Please enter your Date of Birth", Toast.LENGTH_LONG).show();
                    ET_DOB.setError("Date of Birth is required");
                    ET_DOB.requestFocus();
                }else if(TextUtils.isEmpty(RB_UpdatedGenderSelected.getText())) {
                    Toast.makeText(UpdateProfile.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    RB_UpdatedGenderSelected.setError("Gender is required");
                    RB_UpdatedGenderSelected.requestFocus();
                }else{
                    textGender = RB_UpdatedGenderSelected.getText().toString();
                    textFName = ET_UpdateName.getText().toString();
                    textDOB = ET_DOB.getText().toString();

                    //Enter User Data into the Firebase Realtime Database .Setup Dependencies
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFName, textDOB, textGender);
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://health-hub-apps-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Registered Users");

                    String userID = firebaseUser.getUid();

                    progressBar.setVisibility(View.VISIBLE);

                    referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Setting new display Name
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(textFName).build();
                                firebaseUser.updateProfile(profileUpdates);

                                Toast.makeText(UpdateProfile.this, "Update Successful", Toast.LENGTH_LONG).show();

                                //Stop user from returning to Update Profile
                                Intent intent = new Intent(UpdateProfile.this, MyProfileFragment.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                try{
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdateProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.updateProfile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIDofRegistered = firebaseUser.getUid();

        //Extracting User Reference from Database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://health-hub-apps-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Registered Users");
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    textFName = readUserDetails.fullname;
                    textDOB = readUserDetails.dob;
                    textGender = readUserDetails.gender;

                    ET_UpdateName.setText(textFName);
                    ET_DOB.setText(textDOB);


                    //Show Gender thru Radio Button
                    if(textGender.equals("Male")){
                        RB_UpdatedGenderSelected = findViewById(R.id.RB_male);
                    }
                    else{
                        RB_UpdatedGenderSelected = findViewById(R.id.RB_female);
                    }
                    RB_UpdatedGenderSelected.setChecked(true);
                }else{
                    Toast.makeText(UpdateProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}