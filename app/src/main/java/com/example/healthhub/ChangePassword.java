package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class ChangePassword extends AppCompatActivity {


    FirebaseAuth authProfile;
    EditText editTextCPW, editTextNPW, edtEditTextNPWConfirm;
    TextView textViewAuthenticate;
    Button buttonChangePW, buttonReAuthenticate;
    ProgressBar progressBar;
    String userCPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        editTextCPW = findViewById(R.id.ET_CurrentPassword);
        editTextNPW = findViewById(R.id.ET_NewPW);
        edtEditTextNPWConfirm = findViewById(R.id.ET_ConfirmNewPW);
        textViewAuthenticate = findViewById(R.id.textView_VerifyPWHead);
        progressBar = findViewById(R.id.progressBar);
        buttonReAuthenticate = findViewById(R.id.authenticate_btn);
        buttonChangePW = findViewById(R.id.updateEmail_btn);

        //Disable the EditText for New Password, Make New PW and Make Change PW Button un clickable
        editTextNPW.setEnabled(false);
        edtEditTextNPWConfirm.setEnabled(false);
        buttonChangePW.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser.equals("")){
            Toast.makeText(ChangePassword.this, " Something went wrong! User's details not available", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ChangePassword.this, MyProfileFragment.class);
            startActivity(intent);
            finish();
        }else{
            reAuthenticateUser(firebaseUser);
        }






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ChangePW), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //ReAuthenticate User before changing password
    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCPW = editTextCPW.getText().toString();
                if(TextUtils.isEmpty(userCPW)){
                    Toast.makeText(ChangePassword.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    editTextCPW.setError("Please enter your current password to authenticate");
                    editTextCPW.requestFocus();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);

                    //ReAuthenticate User now
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userCPW);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                //Disable editText for Current Password, Enable component in new PW
                                editTextCPW.setEnabled(false);
                                editTextNPW.setEnabled(true);
                                edtEditTextNPWConfirm.setEnabled(true);

                                //Enable Change PW button, Disable Authenticate Button
                                buttonReAuthenticate.setEnabled(false);
                                buttonChangePW.setEnabled(true);

                                //Set TV to show user is authenticated
                                textViewAuthenticate.setText("You are verified." + "Change password now! ");
                                Toast.makeText(ChangePassword.this, "Password has been verified."+
                                        "Change password now ", Toast.LENGTH_SHORT).show();

                                //Update Color Change Password Button
                                buttonChangePW.setBackgroundTintList(ContextCompat.getColorStateList(ChangePassword.this, R.color.darkgreen));

                                buttonChangePW.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });

                            } else{
                                try{
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(ChangePassword.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String NPW = editTextNPW.getText().toString();
        String ConfirmNPW = edtEditTextNPWConfirm.getText().toString();

        if (TextUtils.isEmpty(NPW)) {
            Toast.makeText(ChangePassword.this," New Password is needed", Toast.LENGTH_SHORT).show();
            editTextNPW.setError("Please enter your new password");
            editTextNPW.requestFocus();
        }else if( TextUtils.isEmpty(ConfirmNPW)){
            Toast.makeText(ChangePassword.this," Please confirm your new password", Toast.LENGTH_SHORT).show();
            edtEditTextNPWConfirm.setError("Please re-enter your new password");
            edtEditTextNPWConfirm.requestFocus();
        }else if(!NPW.matches(ConfirmNPW)){
            Toast.makeText(ChangePassword.this," Password did not match", Toast.LENGTH_SHORT).show();
            edtEditTextNPWConfirm.setError("Please re-enter your same password");
            edtEditTextNPWConfirm.requestFocus();
        }else if(userCPW.matches(NPW)){
            Toast.makeText(ChangePassword.this," New password cannot be the same as old password", Toast.LENGTH_SHORT).show();
            editTextNPW.setError("Please enter new password");
            editTextNPW.requestFocus();
        }else{
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(NPW).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChangePassword.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassword.this, MyProfileFragment.class);
                        startActivity(intent);
                        finish();
                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}