package com.example.healthhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


//placeholder homepage to test my fragments
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //fragment to activity using MainActivity bro
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        SharedPreferences sharedpreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "");
        Toast.makeText(requireContext(), "Welcome " + username, Toast.LENGTH_SHORT).show();

        //debug
        Log.d("HomeFragment", "isLoggedIn: " + sharedpreferences.getBoolean("isLoggedIn", false));


        CardView exit = view.findViewById(R.id.cardExit);
        exit.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            startActivity(new Intent(requireActivity(), Login.class));
            requireActivity().finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //go to mltlistfragment (temporary)
        View cardLabTest = view.findViewById(R.id.cardLabTest);
        cardLabTest.setOnClickListener(v -> {
            if (sharedpreferences.getBoolean("isLoggedIn", false)) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MLTListFragment())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            } else {
                Toast.makeText(requireContext(), "Please log in first.", Toast.LENGTH_SHORT).show();
            }
        });

        //pharmacy done (temporary)
        View cardPharmacy = view.findViewById(R.id.cardPharmacy);
        cardPharmacy.setOnClickListener(v -> {
            // Navigate to PharmacyFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PharmacyFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        });

        return view;
    }
}
