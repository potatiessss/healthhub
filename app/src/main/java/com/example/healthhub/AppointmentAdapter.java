package com.example.healthhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        return appointments.size();
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
        notifyDataSetChanged();
    }

    // Load appointments dynamically from Firebase
    public static void loadAppointments(View view, Context context) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            DatabaseReference appointmentsRef = FirebaseDatabase.getInstance()
                    .getReference("Appointments")
                    .child(userId);

            appointmentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Appointment> appointments = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Appointment appointment = snapshot.getValue(Appointment.class);
                        if (appointment != null) {
                            appointments.add(appointment);
                        }
                    }
                    AppointmentAdapter adapter = new AppointmentAdapter(appointments, view);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        } else {
            // Handle case when no user is logged in (e.g., redirect to login screen)
        }
    }

}


