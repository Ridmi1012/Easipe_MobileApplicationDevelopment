package com.example.easipe_mobileapplicationdevelopment.view.features;

//Hirun IM/2021/004

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeAdapter extends FirebaseRecyclerAdapter<Recipe, HomeAdapter.HomeRecipeViewHolder> {

    private Context context;
    private DatabaseReference savedRecipesRef;
    private String userId;


    public HomeAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.context = context;
        // Get the current user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        savedRecipesRef = FirebaseDatabase.getInstance().getReference("user_saved_recipes").child(userId);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeAdapter.HomeRecipeViewHolder holder, int position, @NonNull Recipe model) {

        String recipeId = getRef(position).getKey();

        holder.Htitle.setText(model.getRecipeTitle() != null ? model.getRecipeTitle() : "Untitled");
        holder.Hdiscription.setText(model.getRecipeDiscription());
        holder.Htime.setText(model.getRecipeTime() != null ? model.getRecipeTime() : "Unknown time");
        Glide.with(context).load(model.getRecipeImageurl()).into(holder.Himage);

        //calculate average rating
        Rating.calculateAverageRating(recipeId,holder.HratingBar);

        // Set bookmark icon color based on saved status
        savedRecipesRef.child(recipeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // If the recipe is saved, show a gray background
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.GRAY);
                drawable.setCornerRadius(30);
                holder.Hbookmark.setBackground(drawable);
            } else {
                // If the recipe is not saved, show a transparent background
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.TRANSPARENT);
                drawable.setCornerRadius(30);
                holder.Hbookmark.setBackground(drawable);
            }
        });

        // Handle bookmark click
        holder.Hbookmark.setOnClickListener(v -> {
            // Toggle the saved status
            model.setIssaved(!model.isIssaved());

            if (model.isIssaved()) {
                // Save the recipe to the database using userId and recipeId as the key
               savedRecipesRef.child(recipeId).setValue(model).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       GradientDrawable drawable = new GradientDrawable();
                       drawable.setColor(Color.GRAY);
                       drawable.setCornerRadius(30);

                       holder.Hbookmark.setBackground(drawable); // Change to saved color
                       Toast.makeText(context, "Recipe saved!", Toast.LENGTH_SHORT).show();
                   } else {

                       Toast.makeText(context, "Failed to save recipe.", Toast.LENGTH_SHORT).show();
                   }
               });
            } else {
                // Remove the recipe from the saved recipes
                savedRecipesRef.child(recipeId).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setColor(Color.TRANSPARENT);
                        drawable.setCornerRadius(30);

                        holder.Hbookmark.setBackground(drawable); // Change to default color
                        Toast.makeText(context, "Recipe removed from saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove recipe.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Handle card click to navigate to RecipeContentActivity with recipeId
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(context, RecipeContentFromHomeActivity.class);
            intent.putExtra("recipeId", getRef(holder.getBindingAdapterPosition()).getKey()); // Pass the recipeId
            context.startActivity(intent);
        });
    }

//

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
