package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAppointmentFragment extends Fragment {

    private RecyclerView rvAppointments;
    private AppointmentAdapter appointmentAdapter;
    private Button btnUpcoming, btnPast;

    private List<Appointment> pastAppointments;
    private List<Appointment> upcomingAppointments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize lists
        pastAppointments = new ArrayList<>();
        upcomingAppointments = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_appointment, container, false);

        rvAppointments = view.findViewById(R.id.rv_appointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

        btnUpcoming = view.findViewById(R.id.btn_upcoming);
        btnPast = view.findViewById(R.id.btn_past);

        // Fetch appointments for the current user
        String userID = "current_user_id"; // Replace with actual user ID logic
        Appointment_Firebase.fetchAppointmentsForUser(userID, new Appointment_Firebase.AppointmentDataCallback() {
            @Override
            public void onAppointmentsFetched(List<Appointment> appointments) {
                splitAppointments(appointments);

                // Initialize adapter with upcoming appointments
                appointmentAdapter = new AppointmentAdapter(upcomingAppointments, view);
                rvAppointments.setAdapter(appointmentAdapter);

                // Set up button click listeners
                btnUpcoming.setOnClickListener(v -> {
                    btnUpcoming.setBackgroundResource(R.drawable.tab_active);
                    btnPast.setBackgroundResource(R.drawable.tab_inactive);
                    appointmentAdapter.updateData(upcomingAppointments);
                });

                btnPast.setOnClickListener(v -> {
                    btnPast.setBackgroundResource(R.drawable.tab_active);
                    btnUpcoming.setBackgroundResource(R.drawable.tab_inactive);
                    appointmentAdapter.updateData(pastAppointments);
                });
            }

            @Override
            public void onAppointmentsFetchFailed(Exception exception) {
                // Handle failure
            }
        });

        return view;
    }

    private void splitAppointments(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            if (isUpcoming(appointment.getAppointmentTime())) {
                upcomingAppointments.add(appointment);
            } else {
                pastAppointments.add(appointment);
            }
        }
    }

    private boolean isUpcoming(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date appointmentDate = sdf.parse(date);
            Date currentDate = new Date();
            return appointmentDate != null && appointmentDate.after(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}

