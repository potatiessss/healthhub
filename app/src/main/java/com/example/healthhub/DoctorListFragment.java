package com.example.healthhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoctorListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorListFragment newInstance(String param1, String param2) {
        DoctorListFragment fragment = new DoctorListFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctor_list, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rv_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch doctors from the database
        Database database = new Database(getContext(), "HealthHub.db", null, 1);
        List<Doctor> doctorList = database.getAllDoctors();

        // Set up the adapter with a click listener
        DoctorAdapter adapter = new DoctorAdapter(doctorList, doctor -> {
            // Navigate to DoctorDetailsFragment on doctor card click
            Bundle bundle = new Bundle();
            bundle.putInt("doctorId", doctor.getId()); // Pass the doctor ID to the details fragment
            Navigation.findNavController(view).navigate(R.id.action_doctorListFragment_to_doctorDetailsFragment, bundle);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }


}