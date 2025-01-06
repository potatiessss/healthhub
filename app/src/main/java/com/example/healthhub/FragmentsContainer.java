package com.example.healthhub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentsContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragments_container);


        // Get NavController from the NavHostFragment
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.emptyFragment);
        NavController navController = host.getNavController();

        // Set up the Bottom Navigation menu
        setupBottomNavMenu(navController);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavi);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    // Method to update the toolbar dynamically
    public void updateToolbar(String title, int leftIconResId, int rightIconResId,
                              View.OnClickListener leftIconClickListener,
                              View.OnClickListener rightIconClickListener) {
        // Find and update the toolbar views
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        TextView titleTextView = toolbar.findViewById(R.id.toolbarTitle);
        ImageView leftIcon = toolbar.findViewById(R.id.leftIcon);
        ImageView rightIcon = toolbar.findViewById(R.id.rightIcon);

        titleTextView.setText(title);
        leftIcon.setImageResource(leftIconResId);
        leftIcon.setOnClickListener(leftIconClickListener);
        rightIcon.setImageResource(rightIconResId);
        rightIcon.setOnClickListener(rightIconClickListener);

        // Handle left icon
        if (leftIconResId != 0) {
            leftIcon.setImageResource(leftIconResId);
            leftIcon.setVisibility(View.VISIBLE); // Show icon if it's not empty
            leftIcon.setOnClickListener(leftIconClickListener);
        } else {
            leftIcon.setVisibility(View.GONE); // Hide icon if empty
        }

        // Handle right icon
        if (rightIconResId != 0) {
            rightIcon.setImageResource(rightIconResId);
            rightIcon.setVisibility(View.VISIBLE); // Show icon if it's not empty
            rightIcon.setOnClickListener(rightIconClickListener);
        } else {
            rightIcon.setVisibility(View.GONE); // Hide icon if empty
        }
    }

    public void setToolbarVisibility(boolean isVisible) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (isVisible) {
            toolbar.setVisibility(View.VISIBLE);  // Show toolbar
        } else {
            toolbar.setVisibility(View.GONE);  // Hide toolbar
        }
    }
    public interface ToolbarUpdateListener {
        // No need to define updateToolbar here anymore
    }
}

