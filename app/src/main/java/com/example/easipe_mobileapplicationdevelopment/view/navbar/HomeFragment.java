package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.Repository.HomeRecipeDataManager;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.example.easipe_mobileapplicationdevelopment.view.home.HomeAdapter;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<Recipe> homeRecipes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and other views
        recyclerView = view.findViewById(R.id.home_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch the recipe data
        homeRecipes = HomeRecipeDataManager.getRecipes();

        // Set up the adapter
        homeAdapter = new HomeAdapter(homeRecipes, getContext());
        recyclerView.setAdapter(homeAdapter);
    }
}
