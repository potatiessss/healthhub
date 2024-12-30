package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {
    FirebaseAuth authProfile;
    FirebaseUser firebaseUser;
    ProgressBar progress_Bar;
    TextView textViewAuthenticated;
    String userOldEmail, userNewEmail, userPW;
    Button buttonUpdateEmail;
    EditText editTextNewEmail, editTextPW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_email);

        progress_Bar = findViewById(R.id.progressBar);
        editTextNewEmail = findViewById(R.id.ET_NewEmail);
        editTextPW = findViewById(R.id.ET_Password);
        textViewAuthenticated = findViewById(R.id.textView_UpdateEmailHead);
        buttonUpdateEmail = findViewById(R.id.updateEmail_btn);

        buttonUpdateEmail.setEnabled(false);
        editTextNewEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        //Set old email ID on TV
        userOldEmail = firebaseUser.getEmail();
        TextView textViewOldEmail = findViewById(R.id.TV_EnterCurrentEmail);
        textViewOldEmail.setText(userOldEmail);

        if(firebaseUser== null){
            Toast.makeText(ChangeEmail.this, "Something went wrong! User's details not available.", Toast.LENGTH_LONG).show();
        }else{
            reAuthenticate(firebaseUser);
        }





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.changeEmail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Re authenticate before update email
    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button buttonVerifyUser = findViewById(R.id.authenticate_btn);
        buttonVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtain password for authentication
                userPW = editTextPW.getText().toString();

                if(TextUtils.isEmpty(userPW)){
                    Toast.makeText(ChangeEmail.this, " Password is needed to continue", Toast.LENGTH_LONG).show();
                    editTextPW.setError("Please enter your password for authentication");
                    editTextPW.requestFocus();
                }
                else{
                    progress_Bar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, userPW);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progress_Bar.setVisibility(View.GONE);

                                Toast.makeText(ChangeEmail.this, "Password has been verified."
                                        + "You can update email now. ", Toast.LENGTH_LONG).show();

                                //Set TextView to show that user is authenticated
                                textViewAuthenticated.setText("You are authenticated. You can update your email now. ");

                                //Disable ET for PW, button to verify user and enable ET for new email and update email
                                editTextNewEmail.setEnabled(true);
                                editTextPW.setEnabled(false);
                                buttonVerifyUser.setEnabled(false);
                                buttonUpdateEmail.setEnabled(true);

                                //Change color of Update Email Button
                                buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(ChangeEmail.this,
                                        R.color.darkgreen));

                                buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = editTextNewEmail.getText().toString();
                                        if (TextUtils.isEmpty(userNewEmail)) {
                                            Toast.makeText(ChangeEmail.this, "New Email is required", Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please enter new Email");
                                            editTextNewEmail.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()) {
                                            Toast.makeText(ChangeEmail.this, "Please enter valid Email", Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please provide valid Email");
                                            editTextNewEmail.requestFocus();
                                        } else if (userOldEmail.matches(userNewEmail)) {
                                            Toast.makeText(ChangeEmail.this, "New Email cannot be the same as old Email", Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please enter new Email");
                                            editTextNewEmail.requestFocus();
                                        } else {
                                            progress_Bar.setVisibility(View.VISIBLE);
                                            updateEmail(firebaseUser);
                                        }
                                    }

                                });
                            }else{
                                try{
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(ChangeEmail.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    });
                }

            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {

        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){

                    Toast.makeText(ChangeEmail.this,"Email has been updated.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangeEmail.this, MyProfileFragment.class);
                    startActivity(intent);
                    finish();
                }else{
                    try {
                        throw task.getException();
                    }catch(Exception e){
                        Toast.makeText(ChangeEmail.this, "Unsuccessful Email Updates", Toast.LENGTH_LONG).show();
                    }
                }
                progress_Bar.setVisibility(View.GONE);
            }
        });
    }
}