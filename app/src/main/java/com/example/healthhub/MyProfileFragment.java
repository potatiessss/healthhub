
package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDOB, textViewGender, logout;
    private ProgressBar progressBar;
    private String fullName, email, dob, gender;
    private ImageView imageViewProfilePic;
    private FirebaseAuth authProfile;
    private Button updateProfile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        textViewWelcome = view.findViewById(R.id.textView_show_welcome);
        textViewFullName = view.findViewById(R.id.textView_show_fullname);
        textViewEmail = view.findViewById(R.id.textView_show_email);
        textViewDOB = view.findViewById(R.id.textView_show_dob);
        textViewGender = view.findViewById(R.id.textView_show_gender);
        updateProfile = view.findViewById(R.id.button_update_profile);
        progressBar = view.findViewById(R.id.progress_Bar);
        imageViewProfilePic = view.findViewById(R.id.imageview_profile_dp);
        logout = view.findViewById(R.id.logout_textview);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        logout.setOnClickListener(v -> {
            authProfile.signOut();
            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), Options.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        imageViewProfilePic.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UploadProfilePic.class);
            startActivity(intent);
        });

        if (firebaseUser == null) {
            Toast.makeText(getContext(), "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        updateProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "You can update your profile now", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), UpdateProfile.class));
        });

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.MyProfileFragment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://health-hub-apps-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    fullName = readUserDetails.fullname;
                    email = firebaseUser.getEmail();
                    dob = readUserDetails.dob;
                    gender = readUserDetails.gender;

                    textViewWelcome.setText("Welcome, " + fullName + "!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDOB.setText(dob);
                    textViewGender.setText(gender);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
