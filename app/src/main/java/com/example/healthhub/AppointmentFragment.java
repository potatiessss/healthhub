package com.example.healthhub;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Initialize CalendarView
        CalendarView calendarView = view.findViewById(R.id.calendar_view);

        // Initialize Database
        Database database = new Database(requireContext(), "HealthHub.db", null, 1);

        // Add listener for CalendarView date selection
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Format selected date
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

            // Handle date selection (e.g., open booking dialog)
            handleDateSelection(selectedDate, database);
        });

        // Load appointments into RecyclerView
        loadAppointments(view, database);

        return view;
    }
    private void loadAppointments(View view, Database database) {
        List<Appointment> appointments = database.getOrderData("current_user"); // Replace with actual username logic

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rv_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        AppointmentAdapter adapter = new AppointmentAdapter(appointments, view);
        recyclerView.setAdapter(adapter);
    }


    private void handleDateSelection(String date, Database database) {
        // Example dialog for booking an appointment
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Book Appointment");

        // Inflate custom booking layout
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_book_appointment, null);
        builder.setView(dialogView);

        // Input fields in the dialog
        EditText etDoctorName = dialogView.findViewById(R.id.et_doctor_name);
        EditText etTime = dialogView.findViewById(R.id.et_time);

        builder.setPositiveButton("Book", (dialog, which) -> {
            String doctorName = etDoctorName.getText().toString();
            String time = etTime.getText().toString();

            // Save appointment to the database
            database.addAppointment("current_user", doctorName, date, time); // Replace "current_user" with actual username

            // Refresh appointments in RecyclerView
            loadAppointments(requireView(), database);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


}