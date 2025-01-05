package com.example.healthhub;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Initialize CalendarView
        CalendarView calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            handleDateSelection(selectedDate);
        });

        // Load appointments into RecyclerView
        loadAppointments(view);

        Button findSpecialistButton = view.findViewById(R.id.btn_find_specialist);
        findSpecialistButton.setOnClickListener(v -> {
            Log.d("AppointmentFragment", "Find Specialist button clicked");
            Navigation.findNavController(view).navigate(R.id.action_Appointment_to_doctorSearch);

        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentsContainer activity = (FragmentsContainer) requireActivity();
        activity.updateToolbar(
                "APPOINTMENT", // Title
                R.drawable.hamburger, // Left Icon
                R.drawable.search, // Right Icon
                v -> {
                    // Handle left icon click (e.g., open a drawer)
                },
                v -> {
                    // Handle right icon click (e.g., show notifications)
                }
        );
    }

    private void loadAppointments(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            DatabaseReference appointmentsRef = FirebaseDatabase.getInstance()
                    .getReference("Appointments")
                    .child(userId);

            appointmentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Appointment> appointments = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Appointment appointment = snapshot.getValue(Appointment.class);
                        if (appointment != null) {
                            appointments.add(appointment);
                        }
                    }
                    setupRecyclerView(view, appointments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error loading appointments: " + databaseError.getMessage());
                }
            });
        }
    }

    private void setupRecyclerView(View view, List<Appointment> appointments) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        AppointmentAdapter adapter = new AppointmentAdapter(appointments, view);
        recyclerView.setAdapter(adapter);
    }

    private void handleDateSelection(String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Book Appointment");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_book_appointment, null);
        builder.setView(dialogView);

        EditText etDoctorName = dialogView.findViewById(R.id.et_doctor_name);
        EditText etTime = dialogView.findViewById(R.id.et_time);

        builder.setPositiveButton("Book", (dialog, which) -> {
            String doctorName = etDoctorName.getText().toString();
            String time = etTime.getText().toString();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = auth.getCurrentUser();

            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();

                DatabaseReference appointmentsRef = FirebaseDatabase.getInstance()
                        .getReference("Appointments")
                        .child(userId);

                String appointmentId = appointmentsRef.push().getKey();
                if (appointmentId != null) {
                    Appointment newAppointment = new Appointment(doctorName, date, time);
                    appointmentsRef.child(appointmentId).setValue(newAppointment)
                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Appointment booked successfully"))
                            .addOnFailureListener(e -> Log.e("Firebase", "Failed to book appointment: " + e.getMessage()));
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
