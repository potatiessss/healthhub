package com.example.healthhub.models;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Appointment_Firebase {

    private static final String TAG = "Appointment_Firebase";

    // This method adds an appointment to Firebase for a specific user
    public static void addAppointmentToFirebase(String userID, Appointment appointment, AppointmentCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference appointmentsRef = database.getReference("Appointments");
        DatabaseReference registeredUsersRef = database.getReference("Registered Users");

        // Generate a unique appointment ID
        String appointmentID = appointmentsRef.push().getKey();
        if (appointmentID != null) {
            appointment.setAppointmentId(appointmentID); // Set the generated ID to the appointment object

            // Save the appointment in the "Appointments" node
            appointmentsRef.child(appointmentID).setValue(appointment)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update the "Registered Users" node with the appointment
                            Map<String, Object> userUpdate = new HashMap<>();
                            userUpdate.put("Appointment made/" + appointmentID, true);

                            registeredUsersRef.child(userID).updateChildren(userUpdate)
                                    .addOnCompleteListener(userTask -> {
                                        if (userTask.isSuccessful()) {
                                            callback.onAppointmentAdded(appointmentID);
                                        } else {
                                            callback.onAppointmentAddFailed(userTask.getException());
                                        }
                                    });
                        } else {
                            callback.onAppointmentAddFailed(task.getException());
                        }
                    });
        } else {
            callback.onAppointmentAddFailed(new Exception("Failed to generate appointment ID"));
        }
    }

    // Callback interface to handle success/failure of appointment addition
    public interface AppointmentCallback {
        void onAppointmentAdded(String appointmentID);  // Called when appointment is successfully added
        void onAppointmentAddFailed(Exception exception);  // Called if there is a failure
    }
}
