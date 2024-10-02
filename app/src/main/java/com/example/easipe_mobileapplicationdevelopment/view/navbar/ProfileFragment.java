package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.Repository.MyRecipesDataManager;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.example.easipe_mobileapplicationdevelopment.view.features.RecipeAdapter;

import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> myRecipes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and set layout manager
        recyclerView = view.findViewById(R.id.my_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch recipe data
        myRecipes = MyRecipesDataManager.getRecipes();

        // Set up adapter and bind to RecyclerView
        recipeAdapter = new RecipeAdapter(myRecipes, getContext());
        recyclerView.setAdapter(recipeAdapter);

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}