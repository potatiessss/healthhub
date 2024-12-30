package com.example.healthhub;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private View parentView;

    public AppointmentAdapter(List<Appointment> appointments, View parentView) {
        this.appointments = appointments;
        this.parentView = parentView;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_card, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.tvDoctorName.setText(appointment.getDoctorName());
        holder.tvSpecialist.setText(appointment.getSpecialist());
        holder.tvAppointmentTime.setText(appointment.getAppointmentTime());
        holder.ivDoctor.setImageResource(appointment.getDoctorImage());
    }


    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE; // Infinite cycle
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvSpecialist, tvAppointmentTime;
        ImageView ivDoctor;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvSpecialist = itemView.findViewById(R.id.tv_specialist);
            tvAppointmentTime = itemView.findViewById(R.id.tv_appointment_time);
            ivDoctor = itemView.findViewById(R.id.img_doctor_profile);
        }
    }
    public void updateData(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged(); // Notify the RecyclerView to rebind the views
    }

    private void loadAppointments(View view, Context context, Database database) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        AppointmentAdapter adapter = new AppointmentAdapter(database.getOrderData("current_user"), view);
        recyclerView.setAdapter(adapter);
    }







}

