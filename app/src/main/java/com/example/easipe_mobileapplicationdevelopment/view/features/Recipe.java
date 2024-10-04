package com.example.easipe_mobileapplicationdevelopment.view.features;

//Started-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004

//updated 2024-10-02 Author - Ridmi Silva Student ID- IM/2021/052
public class Recipe {

    private String userId;
    private String recipeTitle;
    private String recipeDiscription;
    private float recipeRating;
    private String recipeTime;
    private String ingredient;
    private String method;
    private String additionalmethod;
    private String recipeImageurl;
    private String recipeVideourl;
    private boolean issaved;


    //Add Recipe
    public Recipe(String userId, String recipeTitle, String recipeDiscription, float recipeRating, String recipeTime, String ingredient, String method, String additionalmethod, String recipeImageurl,String recipeVideourl, boolean issaved) {
        this.userId = userId;
        this.recipeTitle = recipeTitle;
        this.recipeDiscription = recipeDiscription;
        this.recipeRating = recipeRating;
        this.recipeTime = recipeTime;
        this.ingredient = ingredient;
        this.method = method;
        this.additionalmethod = additionalmethod;
        this.recipeImageurl = recipeImageurl;
        this.recipeVideourl = recipeVideourl;
        this.issaved = issaved;
    }


    //empty construter for firebase
    public Recipe() {
    }

    //my profile
    public Recipe(String recipeTitle, float recipeRating, String recipeTime) {
        this.recipeTitle = recipeTitle;
        this.recipeRating = recipeRating;
        this.recipeTime = recipeTime;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeDiscription() {
        return recipeDiscription;
    }

    public float getRecipeRating() {
        return recipeRating;
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

    public String getUserId() {
        return userId;
    }

    public boolean isIssaved() {
        return issaved;
    }

    public void setIssaved(boolean issaved) {
        this.issaved = issaved;
    }

    //updated 2024-10-02 Author - Ridmi Silva Student ID- IM/2021/052
}
