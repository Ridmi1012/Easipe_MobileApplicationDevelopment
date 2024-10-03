package com.example.easipe_mobileapplicationdevelopment.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {

    private TextView userNameTextView, userEmailTextView, userLocationTextView, userAboutTextView;
    private DatabaseReference databaseReference;
    private String userId;
    private String firstName, lastName, email, location, aboutMe; // Declare user data variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_account);

        // Bind TextViews with XML elements
        userNameTextView = findViewById(R.id.user_name);
        userEmailTextView = findViewById(R.id.user_email);
        userLocationTextView = findViewById(R.id.user_location);
        userAboutTextView = findViewById(R.id.user_about);

        // Fetch user ID from Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            Log.d("UserID", "Current User ID: " + userId); // Log the user ID
        } else {
            Log.e("Firebase", "User is not authenticated");
            return;
        }

        // Setup Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered User").child(userId);

        // Fetch and display user data
        fetchUserData();

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchUserData() {
        // Add a listener to fetch user data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot.toString()); // Log the snapshot

                if (dataSnapshot.exists()) {
                    // Extract username from Firebase
                    String username = dataSnapshot.child("username").getValue(String.class);

                    // Set the username to the TextView
                    userNameTextView.setText(username != null ? username : "N/A");

                    // Extract other user details from Firebase
                    email = dataSnapshot.child("email").getValue(String.class);
                    location = dataSnapshot.child("location").getValue(String.class);
                    aboutMe = dataSnapshot.child("description").getValue(String.class);

                    // Set the combined name and other details to TextViews

                    userEmailTextView.setText(email != null ? email : "N/A");
                    userLocationTextView.setText(location != null ? location : "N/A");
                    userAboutTextView.setText(aboutMe != null ? aboutMe : "N/A");
                } else {
                    Log.e("Firebase", "User not found in database");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
                Log.e("Firebase", "Error fetching user data", databaseError.toException());
            }
        });
    }

    // Method to redirect to home page
    public void redirectToHomePage(View view) {
        startActivity(new Intent(this, NavigationBar.class));
        finish();
    }

    // Method to redirect to update account page
    public void update(View view) {
        Intent intent = new Intent(this, EditAccountActivity.class);
        intent.putExtra("username", userNameTextView.getText().toString()); // Pass the full name
        intent.putExtra("email", email);
        intent.putExtra("location", location);
        intent.putExtra("description", aboutMe);
        startActivity(intent);
    }
}



//Author IM/2021/116 Nuzha Kitchilan