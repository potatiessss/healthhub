package com.example.healthhub.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Doctor_Firebase {

    private static final String TAG = "Doctor_Firebase";

    public static void fetchDoctorsFromFirebase(DoctorDataCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference doctorsRef = database.getReference("Doctors");

        doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() { // Use single fetch for non-real-time
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    List<Doctor> doctors = new ArrayList<>();
                    for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                        try {
                            String doctorId = doctorSnapshot.getKey(); // Firebase key as doctorId
                            Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                doctor.setDoctorId(doctorId); // Set the doctorId field
                                Log.d(TAG, "Doctor fetched: " + doctor.getDrName()); // Log to verify data
                                doctors.add(doctor);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing doctor data: ", e);
                        }
                    }

                    callback.onDataFetched(doctors);
                } else {
                    Log.w(TAG, "No doctors found in Firebase");
                    callback.onNoDataFound(); // Notify no data
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error fetching doctors: " + databaseError.getMessage());
                callback.onDataFetchFailed(databaseError.toException());
            }
        });
    }

    public interface DoctorDataCallback {
        void onDataFetched(List<Doctor> doctors);
        void onNoDataFound(); // Notify explicitly if no data
        void onDataFetchFailed(Exception exception);
    }
}
