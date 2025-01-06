
package com.example.healthhub;

import android.os.Bundle;
import android.view.View;

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

        // Listen to destination changes in the NavController
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.myAppointmentFragment || destination.getId() == R.id.doctorDetailsFragment || destination.getId() == R.id.doctorSearch ||
            destination.getId() == R.id.MLTListFragment ||destination.getId() == R.id.MLTDetailsFragment ) {
                // Hide Bottom Navigation when navigating to Product List or Product Details
                findViewById(R.id.bottomNavi).setVisibility(View.GONE);
            } else {
                // Show Bottom Navigation for other fragments
                findViewById(R.id.bottomNavi).setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavi);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }


}
