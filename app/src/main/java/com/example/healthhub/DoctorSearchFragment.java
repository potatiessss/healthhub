package com.example.healthhub;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DoctorSearchFragment extends Fragment {

    private EditText etSearch;
    private ImageButton btnSearch;
    private RecyclerView rvSearchResults;
    private TextView tvNoResults;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_search, container, false);

        // Initialize views
        etSearch = view.findViewById(R.id.et_search_doctor);
        btnSearch = view.findViewById(R.id.btn_search_icon);
        rvSearchResults = view.findViewById(R.id.rv_search_results);
        tvNoResults = view.findViewById(R.id.tv_no_results);

        // Set up RecyclerView with DoctorAdapter
        doctorAdapter = new DoctorAdapter(doctorList, doctor -> {
            // Navigate to DoctorDetailsFragment on card click
            Bundle bundle = new Bundle();
            bundle.putString("doctorId", doctor.getDoctorId()); // Pass doctor ID to the next fragment
            Navigation.findNavController(view).navigate(R.id.action_doctorSearch_to_doctorDetailsFragment, bundle);
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSearchResults.setAdapter(doctorAdapter);

        // Fetch doctor list from Firebase
        fetchDoctorsFromFirebase();

        // Set up search button functionality
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim().toLowerCase();
            filterDoctors(query);
        });

        // Add TextWatcher for dynamic filtering
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDoctors(s.toString().trim().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void fetchDoctorsFromFirebase() {
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorsRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        doctor.setDoctorId(snapshot.getKey()); // Ensure doctor ID is set
                        doctorList.add(doctor);
                    }
                }

                // Set all doctors as the initial display
                doctorAdapter.updateDoctorList(doctorList);

                // Handle visibility of "No doctors found"
                handleNoResults(doctorList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DoctorSearchFragment", "Error fetching doctors: " + databaseError.getMessage());
            }
        });
    }

    private void filterDoctors(String query) {
        // Filter doctors dynamically based on the search query
        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getDrName().toLowerCase().contains(query) ||
                    doctor.getField().toLowerCase().contains(query)) {
                filteredList.add(doctor);
            }
        }

        // Update adapter with filtered results
        doctorAdapter.updateDoctorList(filteredList);

        // Handle visibility of "No doctors found"
        handleNoResults(filteredList);
    }

    private void handleNoResults(List<Doctor> list) {
        if (list.isEmpty()) {
            rvSearchResults.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            rvSearchResults.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
        }
    }
}
