package com.example.easipe_mobileapplicationdevelopment.view.features;

//Hirun IM/2021/004
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchAdapter extends FirebaseRecyclerAdapter<Recipe,SearchAdapter.SearchViewHolder> {

    private Context context;

    public SearchAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position, @NonNull Recipe model) {

        String recipeId = getRef(position).getKey();

        holder.SearchRecipeTitle.setText(model.getRecipeTitle() != null ? model.getRecipeTitle() : "Untitled");
        holder.SearchRecipeTime.setText(model.getRecipeTime() != null ? model.getRecipeTime() : "Unknown time");

        // Using Glide to load the image from URL into ImageView
        Glide.with(context).load(model.getRecipeImageurl()).into(holder.SearchRecipeImage);

        //calculate average rating
        Rating.calculateAverageRating(recipeId,holder.SearchRecipeRatingBar);


        // Handle card click to navigate to RecipeContentFromHomeActivity with recipeId
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeContentFromHomeActivity.class);
            intent.putExtra("recipeId", getRef(holder.getBindingAdapterPosition()).getKey());
            context.startActivity(intent);
        });
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
