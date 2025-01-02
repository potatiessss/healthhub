package com.example.healthhub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Register extends AppCompatActivity {

    EditText edFullname, edEmail, edPassword, edConfirm, edDOB;
    ProgressBar progressBar;
    RadioGroup radioGroupGender;
    RadioButton radioButtonGenderSelected;
    DatePickerDialog picker;
    Button btnSignup;
    TextView tvHaveAcc;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        Toast.makeText(Register.this, "You can register now", Toast.LENGTH_LONG).show();

        // Initialize UI components
        edFullname = findViewById(R.id.ET_FName);
        edPassword = findViewById(R.id.ET_PW);
        edEmail = findViewById(R.id.ET_Email);
        edConfirm = findViewById(R.id.ET_Confirm);
        edDOB = findViewById(R.id.ET_DOB);
        progressBar = findViewById(R.id.progressBar);
        tvHaveAcc = findViewById(R.id.TV_haveAcc);

        //RadioButton for Gender
        radioGroupGender = findViewById(R.id.RG_Gender);
        radioGroupGender.clearCheck();

        //Setting up DatePicker on EditText
        edDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edDOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                picker.show();

            }
        });

        btnSignup = findViewById(R.id.Signup_btn);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGenderID = radioGroupGender.getCheckedRadioButtonId();
                radioButtonGenderSelected = findViewById(selectedGenderID);

                //Obtain the entered data
                String textFullName = edFullname.getText().toString();
                String textEmail = edEmail.getText().toString();
                String textPWD = edPassword.getText().toString();
                String textDOB = edDOB.getText().toString();
                String textConfirmPWD = edConfirm.getText().toString();
                String textGender;

                if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(Register.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    edFullname.setError("Full Name is required");
                    edFullname.requestFocus();
                }else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(Register.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    edEmail.setError("Email is required");
                    edEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(Register.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    edEmail.setError("Valid Email is required");
                    edEmail.requestFocus();
                }else if(TextUtils.isEmpty(textDOB)){
                    Toast.makeText(Register.this, "Please enter your Date of Birth", Toast.LENGTH_LONG).show();
                    edDOB.setError("Date of Birth is required");
                    edDOB.requestFocus();
                }else if(radioGroupGender.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(Register.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    radioButtonGenderSelected.setError("Gender is required");
                    radioButtonGenderSelected.requestFocus();
                }else if(textPWD.isEmpty()) {
                    Toast.makeText(Register.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    edPassword.setError("Password is required");
                    edPassword.requestFocus();
                }else if(!isValid(textPWD)){
                    Toast.makeText(Register.this, "Password must contain at least 8 characters, including a letter, digit, and special symbol", Toast.LENGTH_LONG).show();
                    edPassword.setError("Password too weak");
                    edPassword.requestFocus();
                }else if(TextUtils.isEmpty(textConfirmPWD)){
                    Toast.makeText(Register.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    edConfirm.setError("Password Confirmation is required");
                    edConfirm.requestFocus();
                }else if (!textPWD.equals(textConfirmPWD)){
                    Toast.makeText(Register.this, "Password and Confirm Password didn't match", Toast.LENGTH_LONG).show();
                    edConfirm.setError("Password Confirmation is required");
                    edConfirm.requestFocus();
                    edPassword.clearComposingText();
                    edConfirm.clearComposingText();
                }else{
                    textGender = radioButtonGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail,textDOB,textGender,textPWD);
                }
            }
        });

        // Handle navigation to Login page
        tvHaveAcc.setOnClickListener(v -> startActivity(new Intent(Register.this, Login.class)));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static boolean isValid(String password) {
        boolean hasLetter = false, hasDigit = false, hasSpecial = false;
        if (password.length() < 8) {
            return false;
        }
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if ((c >= 33 && c <= 46) || c == 64) hasSpecial = true; // ASCII for special symbols
        }
        return hasLetter && hasDigit && hasSpecial;
    }


    private void registerUser(String textFullName, String textEmail, String textDOB, String textGender, String textPWD) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPWD).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        //Enter User Data into Firebase Realtime Database
                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textDOB, textGender);

                        //Extracting User Reference from Database for "Registered Users"
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://health-hub-apps-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Registered Users");


                        referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {

                                    Toast.makeText(Register.this, "User registered successfully.", Toast.LENGTH_LONG).show();
                                    //Open User Profile after successful registration
                                    Intent intent = new Intent(Register.this, FragmentsContainer.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(Register.this, "User registered failed. Please try again",
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                            }
                        });
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            edPassword.setError("Password must be at least 8 characters, including a letter, digit, and special symbol.");
                            edPassword.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            edEmail.setError("The email address is invalid. Please enter a valid email.");
                            edEmail.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e) {
                            edEmail.setError("This email is already registered. Please use a different email address.");
                            edEmail.requestFocus();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }
            }
        });
    }

}