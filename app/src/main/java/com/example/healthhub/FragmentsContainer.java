package com.example.healthhub;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class FragmentsContainer extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragments_container);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.main); // Ensure ID matches the DrawerLayout in XML
        NavigationView navigationView = findViewById(R.id.sidenavi); // Side NavigationView

        // Get NavController from the NavHostFragment
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.emptyFragment);
        NavController navController = host.getNavController();

        // Set up the Bottom Navigation menu
        setupBottomNavMenu(navController);

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Listen to destination changes in the NavController
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.MyAppointment || destination.getId() == R.id.doctorDetailsFragment || destination.getId() == R.id.doctorSearch ||
                    destination.getId() == R.id.MLTListFragment || destination.getId() == R.id.MLTDetailsFragment || destination.getId() == R.id.MyOrders ||
                    destination.getId() == R.id.MyCart || destination.getId() == R.id.savedArticleFragment) {
                // Hide Bottom Navigation for specific fragments
                findViewById(R.id.bottomNavi).setVisibility(View.GONE);
            } else {
                // Show Bottom Navigation for other fragments
                findViewById(R.id.bottomNavi).setVisibility(View.VISIBLE);
            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.PurchaseComplete || destination.getId() == R.id.Checkout ) {
                // Hide Bottom Navigation for specific fragments
                findViewById(R.id.sidenavi).setVisibility(View.GONE);
            } else {
                // Show Bottom Navigation for other fragments
                findViewById(R.id.sidenavi).setVisibility(View.VISIBLE);
            }
        });

        // Set up the custom hamburger button for toggling the drawer
        setupDrawerToggle();

        // Set up Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            // Navigate to the selected item
            navController.navigate(item.getItemId());
            // Close the drawer after selection
            drawerLayout.closeDrawer(navigationView);
            return true;
        });
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavi);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    private void setupDrawerToggle() {
        ImageButton hamburgerButton = findViewById(R.id.hamburger_button); // Ensure there's a button for this in your layout
        hamburgerButton.setOnClickListener(v -> {
            // Toggle the drawer open/close state
            if (drawerLayout.isDrawerOpen(findViewById(R.id.sidenavi))) {
                drawerLayout.closeDrawer(findViewById(R.id.sidenavi));
            } else {
                drawerLayout.openDrawer(findViewById(R.id.sidenavi));
            }
        });
    }
}

