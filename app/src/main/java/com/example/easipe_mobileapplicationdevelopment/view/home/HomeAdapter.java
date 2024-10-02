package com.example.easipe_mobileapplicationdevelopment.view.home;

//Started-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004

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
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
////Started-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeRecipeViewHolder> {

    private List<Recipe> homeRecipeList;
    private Context context;

    public HomeAdapter(List<Recipe> homeRecipeList, Context context) {
        this.homeRecipeList = homeRecipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recipe_items,parent,false);
        return new HomeRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeRecipeViewHolder holder, int position) {

        Recipe hrecipe = homeRecipeList.get(position);

        holder.Htitle.setText(hrecipe.getRecipeTitle() != null ? hrecipe.getRecipeTitle() : "Untitled");
        holder.Hdiscription.setText(hrecipe.getRecipeDiscription());
        holder.HratingBar.setRating(hrecipe.getRecipeRating());
        holder.Htime.setText(hrecipe.getRecipeTime() != null ? hrecipe.getRecipeTime() : "Unknown time");
//        holder.Himage.setImageResource(hrecipe.getRecipeImage() != 0 ? hrecipe.getRecipeImage() : R.drawable.burger);

    }

    @Override
    public int getItemCount() {
        return homeRecipeList.size();
    }

    public class HomeRecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView Himage;
        TextView Htitle,Htime,Hdiscription;
        RatingBar HratingBar;


        public HomeRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            Himage = itemView.findViewById(R.id.home_receipe_image);
            Htitle = itemView.findViewById(R.id.home_receipe_title);
            Htime = itemView.findViewById(R.id.home_receipe_time);
            Hdiscription = itemView.findViewById(R.id.home_receipe_discription);
            HratingBar = itemView.findViewById(R.id.home_rating_bar);
        }
    }
}

//Finished-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004