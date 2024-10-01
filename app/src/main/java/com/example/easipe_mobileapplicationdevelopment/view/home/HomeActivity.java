package com.example.easipe_mobileapplicationdevelopment.view.home;

//Started-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.Repository.HomeRecipeDataManager;
import com.example.easipe_mobileapplicationdevelopment.view.auth.LoginActivity;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.example.easipe_mobileapplicationdevelopment.view.profile.EditAccountActivity;
import com.example.easipe_mobileapplicationdevelopment.view.profile.MyAccountActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<Recipe> homeReceipes;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize FirebaseAuth
        authProfile = FirebaseAuth.getInstance();

        // Setting up the toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);  // Set the toolbar as the ActionBar

        recyclerView = findViewById(R.id.home_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeReceipes = HomeRecipeDataManager.getRecipes();


        homeAdapter = new HomeAdapter(homeReceipes,this);
        recyclerView.setAdapter(homeAdapter);


    }

    // Updated - 2024/09/27 Author - Mishel Fernando StudentID - IM/2021/115
    //creating action menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when any meny item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.myProfile){
            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.editProfile) {
            Intent intent = new Intent(HomeActivity.this, EditAccountActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.logout){
            authProfile.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);

            //Clear the stack to prevent user coming back to the HomeAcitivity
            //on pressing back button after logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //close homeActivity
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

//Finished-2024-09-17  Author - Hirun Senarathna StudentID - IM/2021/004