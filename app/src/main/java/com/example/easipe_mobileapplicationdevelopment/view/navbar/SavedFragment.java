package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.example.easipe_mobileapplicationdevelopment.view.features.HomeAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SavedFragment extends Fragment {

    private RecyclerView savedRecipesRecyclerView;
    private HomeAdapter savedRecipesAdapter;
    private DatabaseReference savedRecipesRef;
    private String userId;

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedRecipesRecyclerView = view.findViewById(R.id.saved_recipes_list);
        savedRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the current user's ID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            Log.d("UserID", "Current User ID: " + userId); // Log the user ID
            savedRecipesRef = FirebaseDatabase.getInstance().getReference("user_saved_recipes").child(userId); // User-specific path
        } else {
            Log.e("Firebase", "User is not authenticated");
            return;
        }

        Query query = savedRecipesRef; // Query for the current user's recipes

        FirebaseRecyclerOptions<Recipe> options =
                new FirebaseRecyclerOptions.Builder<Recipe>()
                        .setQuery(query, Recipe.class)
                        .build();

        savedRecipesAdapter = new HomeAdapter(options, getContext());
        savedRecipesRecyclerView.setAdapter(savedRecipesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        savedRecipesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        savedRecipesAdapter.stopListening();
    }
}
