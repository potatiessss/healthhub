package com.example.healthhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user is logged in before loading HomeFragment
        if (savedInstanceState == null) {
            if (isUserLoggedIn()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            } else {
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        }
    }

    // Dummy method to check if user is logged in
    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("isLoggedIn", false);
        Log.d("MainActivity", "isUserLoggedIn: " + loggedIn);
        return loggedIn;
    }
}
