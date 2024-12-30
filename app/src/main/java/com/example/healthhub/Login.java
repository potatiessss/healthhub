package com.example.healthhub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText editTextEmail, editTextPW;
    ProgressBar progressBar;
    FirebaseAuth authProfile;
    TextView TV_signUp;
    private static final String TAG = "LoginActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPW = findViewById(R.id.editTextPW);
        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();



        TV_signUp = findViewById(R.id.TV_Signup);
        TV_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        Button buttonLogin = findViewById(R.id.Login_Button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextEmail.getText().toString();
                String textPW = editTextPW.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(Login.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Valid email is required");
                    editTextEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPW)){
                    Toast.makeText(Login.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editTextPW.setError("Password is required");
                    editTextPW.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPW);
                }
            }
        });



        // Reference the root ConstraintLayout by its ID
        ConstraintLayout rootLayout = findViewById(R.id.login);

        // Apply window insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void loginUser(String Email, String PW) {
        authProfile.signInWithEmailAndPassword(Email, PW).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "You are logged in now", Toast.LENGTH_LONG).show();

                    //Get instance of the current User
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    //Open User Profile
                    //Start the UserProfile Activity
                    startActivity(new Intent(Login.this, FragmentsContainer.class));
                    finish();

                }else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        editTextEmail.setError("User does not exist or is no longer valid. Please register again.");
                        editTextEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextEmail.setError("Invalid credentials. Kindly, check and re-enter. ");
                        editTextEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

        });
    }

    //Check if User is already logged in. In such case, straightaway take the User to the User's profile
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() !=null){
            Toast.makeText(Login.this, "Already Logged In!", Toast.LENGTH_SHORT).show();

            //Start the UserProfile Activity
            startActivity(new Intent(Login.this, FragmentsContainer.class));
            finish(); //Close Login activity
        }
        else{
            Toast.makeText(Login.this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}