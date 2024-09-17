package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.easipe_mobileapplicationdevelopment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NavigationBar extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    AddFragment AddFragment = new AddFragment();
    SavedFragment SavedFragment = new SavedFragment();
    SearchFragment SearchFragment = new SearchFragment();
    ProfileFragment ProfileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_bar);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

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
}