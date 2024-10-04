package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.Repository.MyRecipesDataManager;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.example.easipe_mobileapplicationdevelopment.view.features.RecipeAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private DatabaseReference databaseReferenceProfile;

    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        // Initialize RecyclerView and set layout manager
        recyclerView = view.findViewById(R.id.my_recipes_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);

        // Fetch user ID from Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();
            Log.d("UserID", "Current User ID: " + userId); // Log the user ID

            databaseReferenceProfile = FirebaseDatabase.getInstance().getReference().child("recipes");

            // Configure the FirebaseRecyclerOptions
            FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                    .setQuery(databaseReferenceProfile.orderByChild("userId").equalTo(userId), Recipe.class)
                    .build();

            // Initialize the adapter with Firebase options
            recipeAdapter = new RecipeAdapter(options, getContext());

            // Set the adapter to the RecyclerView
            recyclerView.setAdapter(recipeAdapter);
        } else {
            Log.e("Firebase", "User is not authenticated");
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (recipeAdapter != null) {
            recipeAdapter.startListening();  // Start listening to Firebase data changes
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recipeAdapter != null) {
            recipeAdapter.stopListening();  // Stop listening to Firebase data changes
        }
    }


}