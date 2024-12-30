package com.example.healthhub;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_details, container, false);

        // Initialize components
        ImageView imgDoctorProfile = view.findViewById(R.id.img_doctor_profile);
        TextView tvDoctorName = view.findViewById(R.id.tv_doctor_name);
        TextView tvSpecialization = view.findViewById(R.id.tv_doctor_specialization);
        TextView tvRating = view.findViewById(R.id.tv_doctor_rating);
        TextView tvPatients = view.findViewById(R.id.tv_patients);
        TextView tvAwards = view.findViewById(R.id.tv_awards);
        TextView tvExperience = view.findViewById(R.id.tv_experience);
        TextView tvAboutDoctor = view.findViewById(R.id.tv_about_doctor);
        TextView tvWorkingTime = view.findViewById(R.id.tv_working_time);

        // Retrieve doctorId from arguments
        Bundle args = getArguments();
        if (args != null) {
            int doctorId = args.getInt("doctorId", -1);

            // Fetch doctor details from database (example logic)
            Database database = new Database(requireContext(), "HealthHub.db", null, 1);
            Doctor doctor = database.getDoctorById(doctorId);

            if (doctor != null) {
                tvDoctorName.setText(doctor.getName());
                tvSpecialization.setText(doctor.getField());
                tvRating.setText("⭐ 4.5 (50 reviews)"); // Placeholder value
                tvPatients.setText("129"); // Placeholder value
                tvAwards.setText(String.valueOf(doctor.getAwards()));
                tvExperience.setText(String.format("%d years", doctor.getExperience()));
                tvAboutDoctor.setText(doctor.getAbout());
                tvWorkingTime.setText(doctor.getWorkingTime());

                imgDoctorProfile.setImageResource(doctor.getProfilePic()); // Assuming profilePic is a drawable resource ID
            }
        }

        return view;
    }
}