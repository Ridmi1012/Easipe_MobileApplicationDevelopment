package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easipe_mobileapplicationdevelopment.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeAdapter extends FirebaseRecyclerAdapter<Recipe, HomeAdapter.HomeRecipeViewHolder> {

    private Context context;
    private DatabaseReference savedRecipesRef;
    private String userId;


    public HomeAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.context = context;
        // Get the current user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Initialize savedRecipesRef with the user's ID, without recipeId
        savedRecipesRef = FirebaseDatabase.getInstance().getReference("user_saved_recipes").child(userId);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeAdapter.HomeRecipeViewHolder holder, int position, @NonNull Recipe model) {

        String recipeId = getRef(position).getKey();

        holder.Htitle.setText(model.getRecipeTitle() != null ? model.getRecipeTitle() : "Untitled");
        holder.Hdiscription.setText(model.getRecipeDiscription());
        holder.HratingBar.setRating(model.getRecipeRating());
        holder.Htime.setText(model.getRecipeTime() != null ? model.getRecipeTime() : "Unknown time");
        Glide.with(context).load(model.getRecipeImageurl()).into(holder.Himage);

        // Set bookmark icon color based on saved status
        holder.Hbookmark.setBackgroundColor(model.isIssaved() ? Color.GRAY : Color.TRANSPARENT);

        // Handle bookmark click
        holder.Hbookmark.setOnClickListener(v -> {
            // Toggle the saved status
            model.setIssaved(!model.isIssaved());



            if (model.isIssaved()) {
                // Save the recipe to the database using userId and recipeId as the key
               savedRecipesRef.child(recipeId).setValue(model).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       holder.Hbookmark.setBackgroundColor(Color.GRAY); // Change to saved color
                       Toast.makeText(context, "Recipe saved!", Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(context, "Failed to save recipe.", Toast.LENGTH_SHORT).show();
                   }
               });
            } else {
                // Remove the recipe from the saved recipes
                savedRecipesRef.child(recipeId).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        holder.Hbookmark.setBackgroundColor(Color.GRAY); // Change to default color
                        Toast.makeText(context, "Recipe removed from saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove recipe.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

//    // Method to save the recipe under userId with recipeId as the key
//    public void saveRecipeToUser(String recipeId, Recipe recipe, HomeRecipeViewHolder holder) {
//        DatabaseReference savedRecipeRef = savedRecipesRef.child(userId).child(recipeId); // Use userId and recipeId here
//
//        savedRecipeRef.setValue(recipe).addOnSuccessListener(aVoid -> {
//            holder.Hbookmark.setBackgroundColor(Color.GRAY); // Change to saved color
//            Toast.makeText(context, "Recipe saved!", Toast.LENGTH_SHORT).show();
//        }).addOnFailureListener(e -> {
//            Toast.makeText(context, "Failed to save recipe.", Toast.LENGTH_SHORT).show();
//        });
//    }

    @NonNull
    @Override
    public HomeAdapter.HomeRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recipe_items, parent, false);
        return new HomeRecipeViewHolder(view);
    }

    public class HomeRecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView Himage, Hbookmark;
        TextView Htitle, Htime, Hdiscription;
        RatingBar HratingBar;

        public HomeRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            Himage = itemView.findViewById(R.id.home_receipe_image);
            Htitle = itemView.findViewById(R.id.home_receipe_title);
            Htime = itemView.findViewById(R.id.home_receipe_time);
            Hdiscription = itemView.findViewById(R.id.home_receipe_discription);
            HratingBar = itemView.findViewById(R.id.home_rating_bar);
            Hbookmark = itemView.findViewById(R.id.home_bookmark_icon);
        }
    }
}
