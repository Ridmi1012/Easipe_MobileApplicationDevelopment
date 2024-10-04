package com.example.easipe_mobileapplicationdevelopment.view.features;

//Started-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004

import android.content.Context;
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

import java.util.List;

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

    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_items,parent,false);
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
            profileDelete = itemView.findViewWithTag(R.id.recipe_delete);
        }
    }
}

//Finished-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004
