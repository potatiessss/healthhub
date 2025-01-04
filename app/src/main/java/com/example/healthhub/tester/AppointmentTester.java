package com.example.healthhub.tester;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthhub.Doctor;
import com.example.healthhub.R;
import com.example.healthhub.Doctor_Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentTester extends Fragment {

    private EditText fullNameEditText, addressEditText, contactEditText, dateEditText, timeEditText;
    private RadioGroup doctorRadioGroup;
    private Button saveAppointmentButton;

    private FirebaseAuth firebaseAuth;
    private String userId;

    private static final String TAG = "AppointmentFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tester_appointment, container, false);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser () != null ? firebaseAuth.getCurrentUser ().getUid() : null; // Get logged-in user's ID

        // Initialize UI elements
        fullNameEditText = rootView.findViewById(R.id.fullNameEditText);
        addressEditText = rootView.findViewById(R.id.addressEditText);
        contactEditText = rootView.findViewById(R.id.contactEditText);
        dateEditText = rootView.findViewById(R.id.dateEditText);
        timeEditText = rootView.findViewById(R.id.timeEditText);
        doctorRadioGroup = rootView.findViewById(R.id.doctorRadioGroup);
        saveAppointmentButton = rootView.findViewById(R.id.saveAppointmentButton);

        // Fetch doctors from Firebase
        fetchDoctors();

        // Set up button click listener
        saveAppointmentButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String contact = contactEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            String time = timeEditText.getText().toString().trim();

            // Get selected doctor
            int selectedDoctorId = doctorRadioGroup.getCheckedRadioButtonId();
            if (selectedDoctorId == -1) {
                Toast.makeText(getContext(), "Please select a doctor", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "No doctor selected.");
                return;
            }

            RadioButton selectedRadioButton = rootView.findViewById(selectedDoctorId);
            if (selectedRadioButton == null) {
                Log.e(TAG, "Selected radio button is null.");
                return;
            }

            String selectedDoctorIdValue = selectedRadioButton.getTag() != null
                    ? selectedRadioButton.getTag().toString()
                    : null;

            if (selectedDoctorIdValue == null) {
                Log.e(TAG, "Doctor ID is null for the selected radio button.");
                return;
            }

            Log.d(TAG, "Selected doctor ID: " + selectedDoctorIdValue);

            // Validate user inputs
            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(contact)
                    || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Validation failed: Some fields are empty.");
            } else {
                saveAppointmentToFirebase(fullName, address, contact, date, time, selectedDoctorIdValue);
            }
        });

        return rootView;
    }

    private void fetchDoctors() {
        Doctor_Firebase.fetchDoctorsFromFirebase(new Doctor_Firebase.DoctorDataCallback() {
            @Override
            public void onDataFetched(List<Doctor> doctors) {
                doctorRadioGroup.removeAllViews();
                for (Doctor doctor : doctors) {
                    RadioButton radioButton = new RadioButton(getContext());
                    radioButton.setText(doctor.getDrName());
                    radioButton.setTag(doctor.getDoctorId()); // Attach doctorId as a tag
                    Log.d(TAG, "Added doctor: " + doctor.getDrName() + " (ID: " + doctor.getDoctorId() + ")");
                    doctorRadioGroup.addView(radioButton);
                }
            }

            @Override
            public void onNoDataFound() {

            }

            @Override
            public void onDataFetchFailed(Exception exception) {
                Log.e(TAG, "Error fetching doctors: " + exception.getMessage());
            }
        });
    }

    private void saveAppointmentToFirebase(String fullName, String address, String contact, String date, String time, String doctorId) {
        if (userId == null) {
            Log.e(TAG, "User  ID is null. Cannot save appointment.");
            return;
        }

        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments").child(userId);
        String appointmentId = appointmentsRef.push().getKey(); // Generate a unique ID for the appointment

        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("fullName", fullName);
        appointmentData.put("address", address);
        appointmentData.put("contact", contact);
        appointmentData.put("date", date);
        appointmentData.put("time", time);
        appointmentData.put("doctorId", doctorId);

        if (appointmentId != null) {
            appointmentsRef.child(appointmentId).setValue(appointmentData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Appointment saved successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Appointment saved: " + appointmentId);
                        updateDoctorWithAppointment(doctorId, appointmentId); // Update doctor's node
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save appointment", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error saving appointment: " + e.getMessage());
                    });
        } else {
            Log.e(TAG, "Failed to generate appointment ID.");
        }
    }

    private void updateDoctorWithAppointment(String doctorId, String appointmentId) {
        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference("Doctors").child(doctorId);
        doctorRef.child("apptDr").child(appointmentId).setValue(true) // or any value you want to store
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment ID added to doctor: " + appointmentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to add appointment ID to doctor: " + e.getMessage());
                });
    }




}