package com.example.easipe_mobileapplicationdevelopment.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.home.HomeActivity;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.HomeFragment;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;

public class MyAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void redirectToHomePage(View view) {
        startActivity(new Intent(this, NavigationBar.class));
        finish();
    }

    public void update(View view) {
        startActivity(new Intent(this, EditAccountActivity.class));
    }
}