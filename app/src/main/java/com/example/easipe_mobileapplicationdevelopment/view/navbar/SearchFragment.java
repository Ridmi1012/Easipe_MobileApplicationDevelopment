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

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private HomeAdapter homeAdapter1;
    private HomeAdapter homeAdapter2;
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
        recyclerView1 = view.findViewById(R.id.home_recipes_list_1);
        recyclerView2 = view.findViewById(R.id.home_recipes_list_2);

        // Set layout managers for both RecyclerViews
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase Database Reference
        databaseReferenceHome = FirebaseDatabase.getInstance().getReference("recipes");

        // Configure FirebaseRecyclerOptions for the first RecyclerView
        FirebaseRecyclerOptions<Recipe> options1 = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseReferenceHome.limitToFirst(5), Recipe.class) // Change the query as needed
                .build();

        // Configure FirebaseRecyclerOptions for the second RecyclerView
        FirebaseRecyclerOptions<Recipe> options2 = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseReferenceHome.limitToLast(5), Recipe.class) // Change the query as needed
                .build();

        // Set up the FirebaseRecyclerAdapter for both RecyclerViews
        homeAdapter1 = new HomeAdapter(options1, getContext());
        homeAdapter2 = new HomeAdapter(options2, getContext());

        recyclerView1.setAdapter(homeAdapter1);
        recyclerView2.setAdapter(homeAdapter2);
    }

    @Override
    public void onStart() {
        super.onStart();
        homeAdapter1.startListening();
        homeAdapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (homeAdapter1 != null) {
            homeAdapter1.stopListening();
        }
        if (homeAdapter2 != null) {
            homeAdapter2.stopListening();
        }
    }
}
