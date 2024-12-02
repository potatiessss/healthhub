package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btnSignup;
    TextView tvHaveAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize UI components
        edUsername = findViewById(R.id.ET_UName);
        edPassword = findViewById(R.id.ET_PW);
        edEmail = findViewById(R.id.ET_Email);
        edConfirm = findViewById(R.id.ET_Confirm);
        btnSignup = findViewById(R.id.Signup_btn);
        tvHaveAcc = findViewById(R.id.TV_haveAcc);

        // Handle Sign Up Button click
        btnSignup.setOnClickListener(v -> {
            String username = edUsername.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString();
            String confirm = edConfirm.getText().toString();

            Database db = new Database(getApplicationContext(), "healthcare", null, 1);

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirm)) {
                    if (isValid(password)) {
                        db.register(username, email, password);
                        Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class)); // Use correct class names
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Password must contain at least 8 characters, including a letter, digit, and special symbol",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle navigation to Login page
        tvHaveAcc.setOnClickListener(v -> startActivity(new Intent(Register.this, Login.class)));

        // Adjust padding for insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Password validation method
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
}
