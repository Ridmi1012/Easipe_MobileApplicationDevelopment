package com.example.easipe_mobileapplicationdevelopment.controller;

////Started-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;

import java.util.ArrayList;
import java.util.List;

public class HomeRecipeDataManager {

    public static List<Recipe> getRecipes() {
        List<Recipe> HomeRecipes = new ArrayList<>();

        HomeRecipes.add(new Recipe("Classy Burger",
                "A gourmet burger made with a juicy beef patty, fresh lettuce, tomato, melted cheese, and a soft sesame bun.",
                3.7f, "30 min", R.drawable.burger));

        HomeRecipes.add(new Recipe("Honey Pancakes with Bee Honey and Walnuts",
                "Fluffy pancakes drizzled with pure bee honey and sprinkled with crunchy walnuts, perfect for a delicious breakfast or brunch.",
                4.0f, "30 min", R.drawable.pancake));

        HomeRecipes.add(new Recipe("Cheesy Pizza",
                "A classic Italian pizza topped with a generous layer of mozzarella, cheddar cheese, and a rich tomato basil sauce.",
                5.0f, "15 min", R.drawable.pizza));

        HomeRecipes.add(new Recipe("Spicy Tacos",
                "Soft tortillas filled with seasoned ground beef, fresh vegetables, spicy salsa, and a drizzle of lime crema for extra flavor.",
                4.5f, "20 min", R.drawable.tacos));

        HomeRecipes.add(new Recipe("Garlic Butter Shrimp",
                "Succulent shrimp sautéed in a rich garlic butter sauce, served with a side of fresh herbs and a hint of lemon.",
                4.8f, "25 min", R.drawable.shrimp));

        return HomeRecipes;
    }

// Finished-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004
}
