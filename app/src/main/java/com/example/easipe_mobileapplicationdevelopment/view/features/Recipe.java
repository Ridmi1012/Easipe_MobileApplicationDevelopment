package com.example.easipe_mobileapplicationdevelopment.view.features;

//Started-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004

public class Recipe {

    private String recipeTitle;
    private String recipeDiscription;
    private float  recipeRating;
    private String recipeTime;
    private  int recipeImage;

    public Recipe(String recipeTitle, float recipeRating, String recipeTime, int recipeImage) {
        this.recipeTitle = recipeTitle;
        this.recipeRating = recipeRating;
        this.recipeTime = recipeTime;
        this.recipeImage = recipeImage;
    }

    public Recipe(String recipeTitle, String recipeDiscription, float recipeRating, String recipeTime, int recipeImage) {
        this.recipeTitle = recipeTitle;
        this.recipeDiscription = recipeDiscription;
        this.recipeRating = recipeRating;
        this.recipeTime = recipeTime;
        this.recipeImage = recipeImage;
    }

    public String getRecipeTitle() {

        return recipeTitle;
    }

    public float getRecipeRating() {
        return recipeRating;
    }

    public String getRecipeTime() {
        return recipeTime;
    }

    public int getRecipeImage() {
        return recipeImage;
    }

    public String getRecipeDiscription() {
        return recipeDiscription;
    }
}

//Finished-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004