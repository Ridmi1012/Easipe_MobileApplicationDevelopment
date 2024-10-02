package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.auth.LoginActivity;
import com.example.easipe_mobileapplicationdevelopment.view.profile.EditAccountActivity;
import com.example.easipe_mobileapplicationdevelopment.view.profile.MyAccountActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class NavigationBar extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    AddFragment AddFragment = new AddFragment();
    SavedFragment SavedFragment = new SavedFragment();
    SearchFragment SearchFragment = new SearchFragment();
    ProfileFragment ProfileFragment = new ProfileFragment();
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_bar);

        // Setting up the toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        // Initialize FirebaseAuth
        authProfile = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_Navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_nav, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_nav, homeFragment).commit();
            } else if (itemId == R.id.navigation_saved) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_nav, SavedFragment).commit();
            } else if (itemId == R.id.navigation_add) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_nav, AddFragment).commit();
            } else if (itemId == R.id.navigation_search) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_nav, SearchFragment).commit();

            } else if (itemId == R.id.navigation_profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_nav, ProfileFragment).commit();
            }
            return true;
        });
    }

    // Updated - 2024/09/27 Author - Mishel Fernando StudentID - IM/2021/115
    //creating action menu bar
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
            Intent intent = new Intent(NavigationBar.this, MyAccountActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.editProfile) {
            Intent intent = new Intent(NavigationBar.this, EditAccountActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.logout){
            authProfile.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(NavigationBar.this, LoginActivity.class);

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