package com.example.easipe_mobileapplicationdevelopment.view.features;

//Started-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004

import com.example.easipe_mobileapplicationdevelopment.R;

import java.util.ArrayList;
import java.util.List;

public class SavedRecipeDataManager {

    public static List<Recipe> getRecipes() {
        List<Recipe> savedRecipes = new ArrayList<>();
        savedRecipes.add(new Recipe("Classy Burger", 3.7f, "30 min", R.drawable.burger));
        savedRecipes.add(new Recipe("Honey Pancakes with Bee Honey and Walnuts", 4.0f, "30 min", R.drawable.pancake));
        savedRecipes.add(new Recipe("Cheesy Pizza", 5.0f, "15 min", R.drawable.pizza));
        savedRecipes.add(new Recipe("Spicy Tacos", 4.5f, "20 min", R.drawable.tacos));
        savedRecipes.add(new Recipe("Garlic Butter Shrimp", 4.8f, "25 min", R.drawable.shrimp));
        savedRecipes.add(new Recipe("Vegetable Stir-Fry", 4.2f, "15 min", R.drawable.stir_fry));
        savedRecipes.add(new Recipe("Chicken Alfredo Pasta", 4.3f, "30 min", R.drawable.alfredo_pasta));
        return savedRecipes;
    }
}

//Finished-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004