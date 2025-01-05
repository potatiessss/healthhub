package com.example.healthhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthhub.adapter.ArticleAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private com.example.healthhub.adapter.ArticleAdapter articleAdapter;
    private List<Appointment> appointments = new ArrayList<>();
    private List<Article> articles = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Navigation Buttons
        view.findViewById(R.id.btn_appointment_home).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_home_to_appointmentFragment)
        );

        view.findViewById(R.id.btn_pharmacy_home).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_home_to_articleFragment)
        );

        // Appointments RecyclerView
        RecyclerView rvAppointments = view.findViewById(R.id.rv_appointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Fetch appointments from Firebase
        fetchAppointmentsFromFirebase(rvAppointments);

        // Articles RecyclerView
        RecyclerView rvArticles = view.findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Fetch articles from Firebase
        fetchArticlesFromFirebase(rvArticles);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentsContainer activity = (FragmentsContainer) requireActivity();
        activity.updateToolbar(
                "HOME", // Title
                R.drawable.hamburger, // Left Icon
                R.drawable.search, // Right Icon
                v -> {
                    // Handle left icon click (e.g., open a drawer)
                },
                v -> {
                    // Handle right icon click (e.g., show notifications)
                }
        );
    }
    private void fetchAppointmentsFromFirebase(RecyclerView rvAppointments) {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments").child("current_user"); // Replace with actual user ID logic

        appointmentsRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        appointments.add(appointment);
                    }
                }
                AppointmentAdapter appointmentAdapter = new AppointmentAdapter(appointments, requireView());
                rvAppointments.setAdapter(appointmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void fetchArticlesFromFirebase(RecyclerView rvArticles) {
        DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference("Articles");

        articlesRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articles.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    if (article != null) {
                        articles.add(article);
                    }
                }
                articleAdapter = new ArticleAdapter(articles, requireContext());
                rvArticles.setAdapter(articleAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

    }
}
