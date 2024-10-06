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
    private DatabaseReference searchRecipesRef;

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

        DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId).child("ratings");
        ratingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalRating = 0;
                int ratingCount = 0;

                // Calculate the total rating and count
                for (DataSnapshot ratingSnapshot : snapshot.getChildren()) { // 'dataSnapshot' instead of 'snapshot'
                    Float rating = ratingSnapshot.getValue(Float.class);
                    if (rating != null) {
                        totalRating += rating;
                        ratingCount++;
                    }
                }

                // Calculate average rating
                float averageRating = (ratingCount > 0) ? (totalRating / ratingCount) : 0;
                holder.SearchRecipeRatingBar.setRating(averageRating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load ratings", Toast.LENGTH_SHORT).show();

            }
        });


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
