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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAppointmentFragment extends Fragment {

    private RecyclerView rvAppointments;
    private AppointmentAdapter appointmentAdapter;
    private Button btnUpcoming, btnPast;
    private Database database;

    private List<Appointment> upcomingAppointments;
    private List<Appointment> pastAppointments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_appointment, container, false);

        // Initialize views
        rvAppointments = view.findViewById(R.id.rv_appointments);
        btnUpcoming = view.findViewById(R.id.btn_upcoming);
        btnPast = view.findViewById(R.id.btn_past);

        // Initialize database
        database = new Database(requireContext(), "HealthHub.db", null, 1);

        // Fetch appointments from the database
        List<Appointment> allAppointments = database.getAppointments("current_user");

        // Filter appointments into upcoming and past
        filterAppointments(allAppointments);

        // Setup RecyclerView
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentAdapter = new AppointmentAdapter(upcomingAppointments, view);
        rvAppointments.setAdapter(appointmentAdapter);

        // Button click listeners
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

        return view;
    }

    private void filterAppointments(List<Appointment> allAppointments) {
        upcomingAppointments = new ArrayList<>();
        pastAppointments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Appointment appointment : allAppointments) {
            try {
                Date appointmentDate = sdf.parse(appointment.getAppointmentTime());
                Date currentDate = new Date();
                if (appointmentDate.after(currentDate)) {
                    upcomingAppointments.add(appointment);
                } else {
                    pastAppointments.add(appointment);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
