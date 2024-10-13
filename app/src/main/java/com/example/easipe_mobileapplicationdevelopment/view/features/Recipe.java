package com.example.easipe_mobileapplicationdevelopment.view.features;

//Started-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004

//updated 2024-10-02 Author - Ridmi Silva Student ID- IM/2021/052
public class Recipe {

    private String userId;
    private String recipeTitle;
    private String recipeDiscription;
    private String recipeServing;
    private String recipeTime;
    private String ingredient;
    private String method;
    private String additionalmethod;
    private String recipeImageurl;
    private String recipeVideourl;
    private boolean issaved;
    private String recipeId;
    private float averageRating;


    //for Add Recipe

    public Recipe(String userId, String recipeTitle, String recipeDiscription, String recipeServing, String recipeTime, String ingredient, String method, String additionalmethod, String recipeImageurl, String recipeVideourl, boolean issaved, String recipeId, float averageRating) {
        this.userId = userId;
        this.recipeTitle = recipeTitle;
        this.recipeDiscription = recipeDiscription;
        this.recipeServing = recipeServing;
        this.recipeTime = recipeTime;
        this.ingredient = ingredient;
        this.method = method;
        this.additionalmethod = additionalmethod;
        this.recipeImageurl = recipeImageurl;
        this.recipeVideourl = recipeVideourl;
        this.issaved = issaved;
        this.recipeId = recipeId;
        this.averageRating = averageRating;
    }

    //empty construter for firebase
    public Recipe() {
    }

    //for my profile

    public Recipe(String recipeTitle, String recipeServing, String recipeTime, float averageRating) {
        this.recipeTitle = recipeTitle;
        this.recipeServing = recipeServing;
        this.recipeTime = recipeTime;
        this.averageRating = averageRating;
    }

    public String getUserId() {
        return userId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeDiscription() {
        return recipeDiscription;
    }

    public String getRecipeServing() {
        return recipeServing;
    }

    public String getRecipeTime() {
        return recipeTime;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMethod() {
        return method;
    }

    public String getAdditionalmethod() {
        return additionalmethod;
    }

    public String getRecipeImageurl() {
        return recipeImageurl;
    }

    public String getRecipeVideourl() {
        return recipeVideourl;
    }

    public boolean isIssaved() {
        return issaved;
    }

    public void setIssaved(boolean issaved) {
        this.issaved = issaved;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }
    //updated 2024-10-02 Author - Ridmi Silva Student ID- IM/2021/052
}
