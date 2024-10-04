package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.HomeAdapter;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private DatabaseReference databaseReferenceHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize both RecyclerViews
        recyclerView = view.findViewById(R.id.home_recipes_list);


        // Set layout managers for both RecyclerViews
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase Database Reference
        databaseReferenceHome = FirebaseDatabase.getInstance().getReference("recipes");

        // Configure FirebaseRecyclerOptions for the first RecyclerView
        FirebaseRecyclerOptions<Recipe> options1 = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseReferenceHome.limitToFirst(5), Recipe.class) // Change the query as needed
                .build();



        // Set up the FirebaseRecyclerAdapter for both RecyclerViews
        homeAdapter = new HomeAdapter(options1, getContext());


        recyclerView.setAdapter(homeAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        homeAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (homeAdapter != null) {
            homeAdapter.stopListening();
        }
        if (homeAdapter != null) {
            homeAdapter.stopListening();
        }
    }
}
