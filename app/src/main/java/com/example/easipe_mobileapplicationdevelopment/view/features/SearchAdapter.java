package com.example.easipe_mobileapplicationdevelopment.view.features;

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

public class SearchAdapter extends FirebaseRecyclerAdapter<Recipe,SearchAdapter.SearchViewHolder> {

    private Context context;

    public SearchAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position, @NonNull Recipe model) {

        holder.SearchRecipeTitle.setText(model.getRecipeTitle() != null ? model.getRecipeTitle() : "Untitled");
        holder.SearchRecipeRatingBar.setRating(model.getRecipeRating());
        holder.SearchRecipeTime.setText(model.getRecipeTime() != null ? model.getRecipeTime() : "Unknown time");

        // Using Glide to load the image from URL into ImageView
        Glide.with(context).load(model.getRecipeImageurl()).into(holder.SearchRecipeImage);
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recipe_items,parent,false);
        return new SearchViewHolder(view);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView SearchRecipeImage;
        TextView SearchRecipeTitle, SearchRecipeTime;
        RatingBar  SearchRecipeRatingBar;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);


            SearchRecipeImage = itemView.findViewById(R.id.search_receipe_image);
            SearchRecipeTitle = itemView.findViewById(R.id.search_receipe_title);
            SearchRecipeTime = itemView.findViewById(R.id.search_receipe_time);
            SearchRecipeRatingBar = itemView.findViewById(R.id.search_receipe_rating_bar);

        }
    }
}
