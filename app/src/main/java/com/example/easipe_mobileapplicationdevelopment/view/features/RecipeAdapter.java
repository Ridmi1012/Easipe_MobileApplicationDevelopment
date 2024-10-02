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

import com.example.easipe_mobileapplicationdevelopment.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;

    public RecipeAdapter(List<Recipe> recipeList, Context context) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_items,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {

        Recipe recipe = recipeList.get(position);

        holder.Rtitle.setText(recipe.getRecipeTitle() != null ? recipe.getRecipeTitle() : "Untitled");
        holder.ratingBar.setRating(recipe.getRecipeRating());
        holder.Rtime.setText(recipe.getRecipeTime() != null ? recipe.getRecipeTime() : "Unknown time");


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{

        ImageView Rimage;
        TextView Rtitle,Rtime;
        RatingBar ratingBar;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            Rimage = itemView.findViewById(R.id.receipe_image);
            Rtitle = itemView.findViewById(R.id.receipe_title);
            Rtime = itemView.findViewById(R.id.receipe_time);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}

//Finished-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004
