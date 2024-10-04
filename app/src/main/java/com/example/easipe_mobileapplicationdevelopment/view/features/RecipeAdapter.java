package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easipe_mobileapplicationdevelopment.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipeAdapter extends FirebaseRecyclerAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    private Context context;

    public RecipeAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position, @NonNull Recipe model) {
        holder.profileRecipeTitle.setText(model.getRecipeTitle() != null ? model.getRecipeTitle() : "Untitled");
        holder.profileRecipeRatingBar.setRating(model.getRecipeRating());
        holder.profileRecipeTime.setText(model.getRecipeTime() != null ? model.getRecipeTime() : "Unknown time");

        // Using Glide to load the image from URL into ImageView
        Glide.with(context).load(model.getRecipeImageurl()).into(holder.profileRecipeImage);

        // Add delete functionality
        holder.profileDelete.setOnClickListener(v -> {
            String recipeId = getRef(position).getKey(); // Get the unique ID of the recipe
            if (recipeId != null) {
                deleteRecipe(recipeId);
            }
        });
    }

    // Method to delete the recipe from both home and saved recipes
    private void deleteRecipe(String recipeId) {
        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId);
        DatabaseReference savedRecipeRef = FirebaseDatabase.getInstance().getReference("user_saved_recipes");

        // Remove recipe from "recipes"
        recipeRef.removeValue().addOnSuccessListener(aVoid -> {
            Log.d("RecipeAdapter", "Recipe deleted from 'recipes' successfully");

            // Get the current user's ID
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (userId != null) {
                // Remove recipe from user's saved recipes
                savedRecipeRef.child(userId).child(recipeId).removeValue()
                        .addOnSuccessListener(aVoid1 -> Log.d("RecipeAdapter", "Recipe deleted from 'saved recipes' successfully"))
                        .addOnFailureListener(e -> Log.e("RecipeAdapter", "Failed to delete recipe from 'saved recipes'", e));
            }
        }).addOnFailureListener(e -> Log.e("RecipeAdapter", "Failed to delete recipe from 'recipes'", e));
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_items, parent, false);
        return new RecipeViewHolder(view);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView profileRecipeImage, profileDelete;
        TextView profileRecipeTitle, profileRecipeTime;
        RatingBar profileRecipeRatingBar;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            profileRecipeImage = itemView.findViewById(R.id.receipe_image);
            profileRecipeTitle = itemView.findViewById(R.id.receipe_title);
            profileRecipeTime = itemView.findViewById(R.id.receipe_time);
            profileRecipeRatingBar = itemView.findViewById(R.id.receipe_rating_bar);
            profileDelete = itemView.findViewById(R.id.recipe_delete);
        }
    }
}
