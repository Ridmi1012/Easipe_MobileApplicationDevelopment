package com.example.easipe_mobileapplicationdevelopment.view.features;

import com.example.easipe_mobileapplicationdevelopment.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesDataManager {

    public static List<Recipe> getRecipes() {
        List<Recipe> MyRecipes = new ArrayList<>();

        MyRecipes.add(new Recipe("Classy Burger", 3.7f, "30 min", R.drawable.burger));
        MyRecipes.add(new Recipe("Honey Pancakes with Bee Honey and Walnuts", 4.0f, "30 min", R.drawable.pancake));
        MyRecipes.add(new Recipe("Cheesy Pizza", 5.0f, "15 min", R.drawable.pizza));
        MyRecipes.add(new Recipe("Spicy Tacos", 4.5f, "20 min", R.drawable.tacos));
        MyRecipes.add(new Recipe("Garlic Butter Shrimp", 4.8f, "25 min", R.drawable.shrimp));

        return MyRecipes;
    }
}
