package com.example.healthhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DoctorListFragment extends Fragment {

    private static final String TAG = "DoctorListFragment";

    public DoctorListFragment() {
        // Required empty public constructor
    }

    public static DoctorListFragment newInstance(String param1, String param2) {
        DoctorListFragment fragment = new DoctorListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_list, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rv_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch doctors from Firebase
        fetchDoctorsFromFirebase(recyclerView, view);

        return view;
    }

    private void fetchDoctorsFromFirebase(RecyclerView recyclerView, View view) {
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorsRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Doctor> doctorList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        doctor.setDoctorId(snapshot.getKey()); // Set the Firebase key as doctorId
                        doctorList.add(doctor);
                    }
                }

                // Set up the adapter with a click listener
                DoctorAdapter adapter = new DoctorAdapter(doctorList, doctor -> {
                    // Navigate to DoctorDetailsFragment on doctor card click
                    Bundle bundle = new Bundle();
                    bundle.putString("doctorId", doctor.getDoctorId()); // Pass the doctorId to the details fragment
                    Navigation.findNavController(view).navigate(R.id.action_doctorSearch_to_doctorDetailsFragment, bundle);
                });

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching doctors: " + databaseError.getMessage());
            }
        });
    }
}
