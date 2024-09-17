package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;

import java.util.List;

public class MyRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  RecipeAdapter recipeAdapter;
    private List<Recipe> myReceipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_myrecipes);

        recyclerView = findViewById(R.id.my_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myReceipes = MyRecipesDataManager.getRecipes();


        recipeAdapter = new RecipeAdapter(myReceipes,this);
        recyclerView.setAdapter(recipeAdapter);



    }
}