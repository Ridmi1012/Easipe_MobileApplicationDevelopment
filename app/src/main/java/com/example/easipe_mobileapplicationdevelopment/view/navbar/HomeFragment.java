package com.example.easipe_mobileapplicationdevelopment.view.navbar;

//hirun IM/2021/004
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.HomeAdapter;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private DatabaseReference databaseReferenceHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and other views
        recyclerView = view.findViewById(R.id.home_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase Database Reference
        databaseReferenceHome = FirebaseDatabase.getInstance().getReference("recipes");

        // Configure FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseReferenceHome, Recipe.class)
                .build();

        // Set up the FirebaseRecyclerAdapter
        homeAdapter = new HomeAdapter(options, getContext());
        recyclerView.setAdapter(homeAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        homeAdapter.startListening();
    }

    // Correct lifecycle method for fragments
    @Override
    public void onStop() {
        super.onStop();
        if (homeAdapter != null) {
            homeAdapter.stopListening();
        }
    }
}
