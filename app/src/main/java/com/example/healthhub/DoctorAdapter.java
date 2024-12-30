package com.example.healthhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private OnDoctorClickListener clickListener;

    // Interface for handling doctor card clicks
    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    // Constructor to initialize the doctor list and click listener
    public DoctorAdapter(List<Doctor> doctorList, OnDoctorClickListener clickListener) {
        this.doctorList = doctorList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item_doctor.xml as the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        // Get the current doctor object
        Doctor doctor = doctorList.get(position);

        // Bind data to the views in item_doctor.xml
        holder.tvName.setText(doctor.getName());
        holder.tvField.setText(doctor.getField());
        holder.tvExperience.setText(String.format("%d years experience", doctor.getExperience()));
        holder.tvConsultationFee.setText(String.format("RM %.2f", doctor.getConsultationFee()));
        holder.tvWorkingTime.setText(doctor.getWorkingTime());
        holder.imgDoctor.setImageResource(doctor.getProfilePic());

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDoctorClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    // ViewHolder class to hold references to each item's views
    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvField, tvExperience, tvConsultationFee, tvWorkingTime;
        ImageView imgDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views from item_doctor.xml
            tvName = itemView.findViewById(R.id.tv_doctor_name);
            tvField = itemView.findViewById(R.id.tv_field);
            tvExperience = itemView.findViewById(R.id.tv_experience);
            tvConsultationFee = itemView.findViewById(R.id.tv_consultation_fee);
            tvWorkingTime = itemView.findViewById(R.id.tv_working_time);
            imgDoctor = itemView.findViewById(R.id.img_doctor);
        }
    }

    // Method to update the doctor list dynamically
    public void updateDoctorList(List<Doctor> updatedList) {
        this.doctorList = updatedList;
        notifyDataSetChanged();
    }
}
