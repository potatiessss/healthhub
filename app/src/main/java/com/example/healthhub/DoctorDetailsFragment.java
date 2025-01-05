package com.example.healthhub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DoctorDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_details, container, false);

        ImageView imgDoctorProfile = view.findViewById(R.id.img_doctor_profile);
        TextView tvDoctorName = view.findViewById(R.id.tv_doctor_name);
        TextView tvSpecialization = view.findViewById(R.id.tv_doctor_specialization);
        TextView tvExperience = view.findViewById(R.id.tv_experience);
        TextView tvAboutDoctor = view.findViewById(R.id.tv_about_doctor);

        String doctorId = getArguments() != null ? getArguments().getString("doctorId") : null;

        if (doctorId != null) {
            DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference("Doctors").child(doctorId);

            doctorRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        tvDoctorName.setText(doctor.getDrName());
                        tvSpecialization.setText(doctor.getField());
                        tvExperience.setText(String.format("%d years", doctor.getExp()));
                        tvAboutDoctor.setText(doctor.getAbout());

                        Picasso.get()
                                .load(doctor.getDrImage())
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.error_image)
                                .into(imgDoctorProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("DoctorDetails", "Error fetching doctor details: " + error.getMessage());
                }
            });
        }

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentsContainer activity = (FragmentsContainer) requireActivity();
        activity.updateToolbar(
                "DOCTOR DETAILS", // Title
                R.drawable.back_button_inversed, // Left Icon
                0, // Right Icon
                v -> {
                    // Handle left icon click (e.g., open a drawer)
                },
                v -> {
                    // Handle right icon click (e.g., show notifications)
                }
        );
    }
}

