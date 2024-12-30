package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DoctorSearchFragment extends Fragment {

    private EditText etSearch;
    private ImageButton btnSearch;
    private RecyclerView rvSearchResults;
    private DoctorAdapter doctorAdapter;
    private Database database;
    private List<Doctor> doctorList;

    public DoctorSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_search, container, false);

        etSearch = view.findViewById(R.id.et_search_doctor);
        btnSearch = view.findViewById(R.id.btn_search_icon);
        rvSearchResults = view.findViewById(R.id.rv_search_results);
        database = new Database(requireContext(), "HealthHubDB", null, 1);

        // Fetch doctor list from the database
        doctorList = database.getAllDoctors();

        // Set up RecyclerView with DoctorAdapter
        doctorAdapter = new DoctorAdapter(doctorList, doctor -> {
            // Navigate to DoctorDetailsFragment on card click
            Bundle bundle = new Bundle();
            bundle.putInt("doctorId", doctor.getId()); // Use the doctor instance to call getId()
            Navigation.findNavController(view).navigate(R.id.action_doctorSearchFragment_to_doctorDetailsFragment, bundle);
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSearchResults.setAdapter(doctorAdapter);

        // Set up search functionality
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim().toLowerCase();
            filterDoctors(query);
        });

        return view;
    }

    private void filterDoctors(String query) {
        // Filter doctors dynamically based on the search query
        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getName().toLowerCase().contains(query) ||
                    doctor.getField().toLowerCase().contains(query)) {
                filteredList.add(doctor);
            }
        }
        doctorAdapter.updateDoctorList(filteredList);
    }
}
