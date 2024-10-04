package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.HomeAdapter;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.example.easipe_mobileapplicationdevelopment.view.features.SearchAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
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


        recyclerView = view.findViewById(R.id.search_recipes_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);
        // Initialize Firebase Database Reference
        databaseReferenceHome = FirebaseDatabase.getInstance().getReference("recipes");

        // Configure FirebaseRecyclerOptions for the first RecyclerView
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseReferenceHome.limitToFirst(5), Recipe.class) // Change the query as needed
                .build();

        // Set up the FirebaseRecyclerAdapter for both RecyclerViews
        searchAdapter = new SearchAdapter(options, getContext());


        recyclerView.setAdapter(searchAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        searchAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (searchAdapter != null) {
            searchAdapter.stopListening();
        }
        if (searchAdapter != null) {
            searchAdapter.stopListening();
        }
    }
}
