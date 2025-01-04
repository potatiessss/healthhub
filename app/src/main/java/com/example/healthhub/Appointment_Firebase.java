package com.example.healthhub;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Appointment_Firebase {

    public static void fetchAppointmentsForUser(String userID, AppointmentDataCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference appointmentsRef = database.getReference("Appointments");

        appointmentsRef.orderByChild("userID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Appointment> appointments = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        appointments.add(appointment);
                    }
                }
                callback.onAppointmentsFetched(appointments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onAppointmentsFetchFailed(error.toException());
            }
        });
    }

    public static void addAppointmentToFirebase(String userID, Appointment appointment, AppointmentCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference appointmentsRef = database.getReference("Appointments");
        String appointmentID = appointmentsRef.push().getKey();
        if (appointmentID != null) {
            appointment.setAppointmentId(appointmentID);
            appointmentsRef.child(appointmentID).setValue(appointment).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onAppointmentAdded(appointmentID);
                } else {
                    callback.onAppointmentAddFailed(task.getException());
                }
            });
        } else {
            callback.onAppointmentAddFailed(new Exception("Failed to generate appointment ID"));
        }
    }

    public interface AppointmentCallback {
        void onAppointmentAdded(String appointmentID);
        void onAppointmentAddFailed(Exception exception);
    }

    public interface AppointmentDataCallback {
        void onAppointmentsFetched(List<Appointment> appointments);
        void onAppointmentsFetchFailed(Exception exception);
    }
}
