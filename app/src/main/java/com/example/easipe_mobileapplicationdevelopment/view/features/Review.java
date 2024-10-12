package com.example.easipe_mobileapplicationdevelopment.view.features;

public class Review {

    private String userId;
    private String recipeId;
    private float recipeRating;

    public Review() {
    }

    public Review(String userId, String recipeId, float recipeRating) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.recipeRating = recipeRating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public float getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(float recipeRating) {
        this.recipeRating = recipeRating;
    }
}
