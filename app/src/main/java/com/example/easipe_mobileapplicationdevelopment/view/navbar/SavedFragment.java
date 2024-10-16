package com.example.easipe_mobileapplicationdevelopment.view.navbar;

//hirun IM/2021/004
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
import android.widget.SearchView;

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
    private SearchView savedSearchView;

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
        savedSearchView = view.findViewById(R.id.saved_search_recipe_bar);

        // Get the current user's ID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            Log.d("UserID", "Current User ID: " + userId);

            savedRecipesRef = FirebaseDatabase.getInstance().getReference("user_saved_recipes").child(userId);
        } else {
            Log.e("Firebase", "User is not authenticated");
            return;
        }

        // Query for all the saved recipes under the current user
        Query query = savedRecipesRef;

        // Configure FirebaseRecyclerOptions to use Recipe class
        FirebaseRecyclerOptions<Recipe> options =
                new FirebaseRecyclerOptions.Builder<Recipe>()
                        .setQuery(query, Recipe.class) // Query retrieves recipes by recipeId
                        .build();

        savedRecipesAdapter = new HomeAdapter(options, getContext());
        savedRecipesRecyclerView.setAdapter(savedRecipesAdapter);

        //foe search
        savedSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // Handle the search on submit
                searchSavedRecipes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the search on submit
                searchSavedRecipes(newText);
                return false;
            }
        });
    }

    private void searchSavedRecipes(String searchText) {

        // Prevent searching for empty or null strings, reset to default query
        if (searchText == null || searchText.trim().isEmpty()) {
            resetToDefaultQuery();
            return;
        }

        // Create a query to search for recipes whose title contains the searchText
        Query searchQuery = savedRecipesRef.orderByChild("recipeTitle")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        // Update FirebaseRecyclerOptions with the new search query
        FirebaseRecyclerOptions<Recipe> searchOptions = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(searchQuery, Recipe.class)
                .build();

        // Update the adapter with the new search options
        savedRecipesAdapter.updateOptions(searchOptions);
        savedRecipesAdapter.notifyDataSetChanged();
    }

    private void resetToDefaultQuery() {

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(savedRecipesRef, Recipe.class)
                .build();
        savedRecipesAdapter.updateOptions(options);
        savedRecipesAdapter.notifyDataSetChanged();
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
