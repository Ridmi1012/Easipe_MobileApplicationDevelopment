package com.example.easipe_mobileapplicationdevelopment.view.home;

//Started-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.Repository.HomeRecipeDataManager;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<Recipe> homeReceipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.home_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeReceipes = HomeRecipeDataManager.getRecipes();


        homeAdapter = new HomeAdapter(homeReceipes,this);
        recyclerView.setAdapter(homeAdapter);
    }
}

//Finished-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004