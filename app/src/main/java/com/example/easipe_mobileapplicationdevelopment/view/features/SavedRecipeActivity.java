package com.example.easipe_mobileapplicationdevelopment.view.features;

//Started-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.Repository.SavedRecipeDataManager;

import java.util.List;

public class SavedRecipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  RecipeAdapter recipeAdapter;
    private List<Recipe> savedReceipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saved_recipe);

        recyclerView = findViewById(R.id.saved_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        savedReceipes = SavedRecipeDataManager.getRecipes();


        recipeAdapter = new RecipeAdapter(savedReceipes,this);
        recyclerView.setAdapter(recipeAdapter);
    }
}

//Finished-2024-09-16  Author - Hirun Senarathna StudentID - IM/2021/004