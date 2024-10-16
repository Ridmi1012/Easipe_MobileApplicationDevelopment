package com.example.easipe_mobileapplicationdevelopment.view.features;

//Hirun IM/2021/004

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeAdapter extends FirebaseRecyclerAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    private Context context;

    public RecipeAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position, @NonNull Recipe model) {

        String recipeId = getRef(position).getKey();

        holder.profileRecipeTitle.setText(model.getRecipeTitle() != null ? model.getRecipeTitle() : "Untitled");
        holder.profileRecipeTime.setText(model.getRecipeTime() != null ? model.getRecipeTime() : "Unknown time");

        // Using Glide to load the image from URL into ImageView
        Glide.with(context).load(model.getRecipeImageurl()).into(holder.profileRecipeImage);

        //calculate average rating
        Rating.calculateAverageRating(recipeId,holder.profileRecipeRatingBar);

        // Add delete functionality
        holder.profileDelete.setOnClickListener(v -> {
            if (recipeId != null) {
                deleteRecipe(model.getUserId(), recipeId);
            }
        });

        // Handle card click to navigate to RecipeContentActivity with recipeId
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, RecipeContent.class);
            intent.putExtra("recipeId", getRef(holder.getBindingAdapterPosition()).getKey()); // Pass the recipeId
            context.startActivity(intent);
        });
    }

    // Method to delete the recipe from both "recipes" and all users' saved recipes
    private void deleteRecipe(String userId, String recipeId) {

        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId);

        DatabaseReference savedRecipesRef = FirebaseDatabase.getInstance().getReference("user_saved_recipes");

        // Remove recipe from the "recipes" node
        recipeRef.removeValue().addOnSuccessListener(aVoid -> {
            Log.d("RecipeAdapter", "Recipe deleted from 'recipes' successfully");


            savedRecipesRef.orderByChild(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userSnapshot.getRef().child(recipeId).removeValue()
                                .addOnSuccessListener(aVoid1 -> Log.d("RecipeAdapter", "Recipe deleted from 'user_saved_recipes' successfully"))
                                .addOnFailureListener(e -> Log.e("RecipeAdapter", "Failed to delete recipe from 'user_saved_recipes'", e));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("RecipeAdapter", "Error accessing saved recipes", databaseError.toException());
                }
            });
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
