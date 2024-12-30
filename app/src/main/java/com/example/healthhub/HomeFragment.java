package com.example.healthhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class HomeFragment extends Fragment {
    private ArticleAdapter articleAdapter;

    private Database db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the database
        db = new Database(getContext(), "HealthHub.db", null, 1);

        // Navigation Buttons
        view.findViewById(R.id.btn_appointment_home).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_home_to_appointmentFragment)
        );

        view.findViewById(R.id.btn_pharmacy_home).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_home_to_articleFragment)
        );


        // Appointment RecyclerView
        RecyclerView rvAppointments = view.findViewById(R.id.rv_appointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Appointment> appointments = db.getOrderData("current_user"); // Now returns List<Appointment>
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(appointments, view);
        rvAppointments.setAdapter(appointmentAdapter);

        // Fetch and display the bottom 3 articles
        RecyclerView rvArticles = view.findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Fetch latest 3 articles dynamically from the database
        List<Article> articles = db.getLatestArticles(3);

        // Set up the ArticleAdapter
        ArticleAdapter articleAdapter = new ArticleAdapter(articles);
        rvArticles.setAdapter(articleAdapter);

        // Fetch and display the top 2 appointments
        LinearLayout appointmentContainer = view.findViewById(R.id.appointment_container);
        List<Appointment> appointmentList = db.getOrderData("current_user"); // Replace with actual username
        int appointmentCount = Math.min(appointments.size(), 2); // Only show top 2 appointments
        for (int i = 0; i < appointmentCount; i++) {
            Appointment appointment = appointmentList.get(i);
            addAppointmentCard(appointmentContainer, appointment);
        }



        // Fetch and display the bottom 3 articles
        LinearLayout articleContainer = view.findViewById(R.id.article_container);
        List<Article> articleList = db.getLatestArticles(3); // Fetch the latest 3 articles
        for (Article article : articleList) {
            addArticleCard(articleContainer, article, view);
        }

        return view;
    }

    private void addAppointmentCard(LinearLayout container, Appointment appointment) {
        // Inflate the appointment card layout
        View card = LayoutInflater.from(getContext()).inflate(R.layout.item_appointment_card, container, false);

        // Find and populate views with Appointment data
        TextView tvDoctorName = card.findViewById(R.id.tv_doctor_name);
        TextView tvSpecialist = card.findViewById(R.id.tv_specialist);
        TextView tvAppointmentTime = card.findViewById(R.id.tv_appointment_time);

        tvDoctorName.setText(appointment.getDoctorName());
        tvSpecialist.setText(appointment.getSpecialist());
        tvAppointmentTime.setText(appointment.getAppointmentTime());

        // Add the card to the container
        container.addView(card);
    }


    private void addArticleCard(LinearLayout container, Article article, View parentView) {
        View card = LayoutInflater.from(getContext()).inflate(R.layout.item_article_card, container, false);

        ImageView imgArticle = card.findViewById(R.id.img_article);
        TextView tvArticleTitle = card.findViewById(R.id.tv_article_title);

        imgArticle.setImageResource(article.getImage()); // Assuming image is a drawable resource ID
        tvArticleTitle.setText(article.getTitle());

        // Click listener for the article title
        tvArticleTitle.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("articleId", article.getId());
            Navigation.findNavController(parentView).navigate(R.id.action_home_to_articleFragment, bundle);
        });

        container.addView(card);
    }

    private void refreshArticles() {
        // Fetch updated articles from the database
        List<Article> updatedArticles = db.getLatestArticles(3);

        // Update the adapter with the new data
        articleAdapter.updateArticles(updatedArticles);

        // Notify the adapter that the data set has changed
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshArticles();
    }
}
