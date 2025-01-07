package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

public class CheckoutFragment extends Fragment {
    private TextInputLayout tilStreetAddress;
    private TextInputLayout tilCity;
    private TextInputLayout tilState;
    private TextInputLayout tilPostcode;
    private EditText edtStreetAddress;
    private EditText edtCity;
    private EditText edtState;
    private EditText edtPostcode;
    private RadioGroup rgPaymentMethod;
    private Button btnPay;
    ImageView backbutton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        backbutton = view.findViewById(R.id.back_button);
        backbutton.setOnClickListener(v -> requireActivity().onBackPressed());

        initializeViews(view);
        setupPayButton();

        return view;
    }

    private void initializeViews(View view) {
        // Initialize TextInputLayouts
        tilStreetAddress = view.findViewById(R.id.tilStreetAddress);
        tilCity = view.findViewById(R.id.tilCity);
        tilState = view.findViewById(R.id.tilState);
        tilPostcode = view.findViewById(R.id.tilPostcode);

        // Initialize EditTexts
        edtStreetAddress = view.findViewById(R.id.edtStreetAddress);
        edtCity = view.findViewById(R.id.edtCity);
        edtState = view.findViewById(R.id.edtState);
        edtPostcode = view.findViewById(R.id.edtPostcode);

        // Initialize RadioGroup and Button
        rgPaymentMethod = view.findViewById(R.id.rgPaymentMethod);
        btnPay = view.findViewById(R.id.btnPay);
    }

    private void setupPayButton() {
        btnPay.setOnClickListener(v -> {
            if (validateInputs()) {
                processPayment();


                // Create a new instance of PurchaseCompleteFragment
                PurchaseCompleteFragment fragment = new PurchaseCompleteFragment();

                // Optionally, pass arguments to the fragment if needed
                Bundle bundle = new Bundle();
                // Example: bundle.putString("key", "value");
                fragment.setArguments(bundle);

                // Hide the BottomNavigationView & side navi
                requireActivity().findViewById(R.id.bottomNavi).setVisibility(View.GONE);
                requireActivity().findViewById(R.id.sidenavi).setVisibility(View.GONE);
                // Use FragmentTransaction to replace the current fragment with PurchaseCompleteFragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.emptyFragment, fragment)
                        .addToBackStack(null) // Add to back stack so user can navigate back
                        .commit();
            }
        });
    }


    private boolean validateInputs() {
        boolean isValid = true;

        // Validate Street Address
        if (edtStreetAddress.getText().toString().trim().isEmpty()) {
            tilStreetAddress.setError("Street address is required");
            isValid = false;
        } else {
            tilStreetAddress.setError(null);
        }

        // Validate City
        if (edtCity.getText().toString().trim().isEmpty()) {
            tilCity.setError("City is required");
            isValid = false;
        } else {
            tilCity.setError(null);
        }

        // Validate State
        if (edtState.getText().toString().trim().isEmpty()) {
            tilState.setError("State is required");
            isValid = false;
        } else {
            tilState.setError(null);
        }

        // Validate Postcode
        String postcode = edtPostcode.getText().toString().trim();
        if (postcode.isEmpty()) {
            tilPostcode.setError("Postcode is required");
            isValid = false;
        } else if (!postcode.matches("\\d{5}")) {
            tilPostcode.setError("Invalid postcode format");
            isValid = false;
        } else {
            tilPostcode.setError(null);
        }

        // Validate Payment Method
        if (rgPaymentMethod.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void processPayment() {
        // Get the address details
        String address = String.format("%s, %s, %s %s",
                edtStreetAddress.getText().toString().trim(),
                edtCity.getText().toString().trim(),
                edtState.getText().toString().trim(),
                edtPostcode.getText().toString().trim()
        );

        // Get the selected payment method
        String paymentMethod = rgPaymentMethod.getCheckedRadioButtonId() == R.id.rbOnlineBanking
                ? "Online Banking"
                : "Debit Card";

        Toast.makeText(getContext(), "Horray!!! Success Payment!!", Toast.LENGTH_SHORT).show();
    }
}
