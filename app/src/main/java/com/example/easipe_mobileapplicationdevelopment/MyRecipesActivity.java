package com.example.easipe_mobileapplicationdevelopment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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