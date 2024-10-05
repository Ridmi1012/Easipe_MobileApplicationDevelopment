package com.example.easipe_mobileapplicationdevelopment.view.navbar;
//hirun IM/2021/004
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.example.easipe_mobileapplicationdevelopment.view.features.SearchAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private DatabaseReference databaseReferenceSearch;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the views
        recyclerView = view.findViewById(R.id.search_recipes_list);
        searchView = view.findViewById(R.id.search_recipe_bar);

        // Set up RecyclerView with GridLayoutManager (2 columns)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Initialize Firebase Database Reference
        databaseReferenceSearch = FirebaseDatabase.getInstance().getReference("recipes");


        // Set up FirebaseRecyclerOptions for the default query
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseReferenceSearch, Recipe.class)
                .build();

        // Initialize and set up the adapter
        searchAdapter = new SearchAdapter(options, getContext());
        recyclerView.setAdapter(searchAdapter);

        // Add search functionality for real-time search as user types in the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search on submit
                searchRecipes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle real-time search when text changes
                searchRecipes(newText);
                return false;
            }
        });
    }

    // Method to perform search with Firebase query
    private void searchRecipes(String searchText) {
        // Prevent searching for empty or null strings, reset to default query
        if (searchText == null || searchText.trim().isEmpty()) {
            resetToDefaultQuery();
            return;
        }

        // Create a query to search for recipes whose title contains the searchText
        Query searchQuery = databaseReferenceSearch.orderByChild("recipeTitle")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        // Update FirebaseRecyclerOptions with the new search query
        FirebaseRecyclerOptions<Recipe> searchOptions = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(searchQuery, Recipe.class)
                .build();

        // Update the adapter with the new search options
        searchAdapter.updateOptions(searchOptions);
        searchAdapter.notifyDataSetChanged();
    }

    // Method to reset the RecyclerView to the default query when search input is empty
    private void resetToDefaultQuery() {

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(databaseReferenceSearch, Recipe.class)
                .build();

        searchAdapter.updateOptions(options);
        searchAdapter.notifyDataSetChanged();
    }

    // Start listening to the FirebaseRecyclerAdapter when fragment is visible
    @Override
    public void onStart() {
        super.onStart();
        if (searchAdapter != null) {
            searchAdapter.startListening();
        }
    }

    // Stop listening to the FirebaseRecyclerAdapter when fragment is stopped
    @Override
    public void onStop() {
        super.onStop();
        if (searchAdapter != null) {
            searchAdapter.stopListening();
        }
    }
}
