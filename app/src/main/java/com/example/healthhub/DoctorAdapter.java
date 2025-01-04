package com.example.healthhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private OnDoctorClickListener clickListener;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorAdapter(List<Doctor> doctorList, OnDoctorClickListener clickListener) {
        this.doctorList = doctorList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.tvName.setText(doctor.getDrName());
        holder.tvField.setText(doctor.getField());
        holder.tvExperience.setText(String.format("%d years experience", doctor.getExp()));
        holder.tvConsultationFee.setText(String.format("RM %.2f", doctor.getFee()));
        holder.tvWorkingTime.setText(doctor.getWorkingTime());

        Picasso.get()
                .load(doctor.getDrImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.imgDoctor);

        holder.itemView.setOnClickListener(v -> clickListener.onDoctorClick(doctor));
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public void updateDoctorList(List<Doctor> updatedList) {
        this.doctorList = updatedList;
        notifyDataSetChanged();
    }


    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvField, tvExperience, tvConsultationFee, tvWorkingTime;
        ImageView imgDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_doctor_name);
            tvField = itemView.findViewById(R.id.tv_field);
            tvExperience = itemView.findViewById(R.id.tv_experience);
            tvConsultationFee = itemView.findViewById(R.id.tv_consultation_fee);
            tvWorkingTime = itemView.findViewById(R.id.tv_working_time);
            imgDoctor = itemView.findViewById(R.id.img_doctor);
        }
    }
}

