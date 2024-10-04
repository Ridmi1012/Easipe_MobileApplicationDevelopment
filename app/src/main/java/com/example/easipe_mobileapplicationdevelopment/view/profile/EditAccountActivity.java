package com.example.easipe_mobileapplicationdevelopment.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditAccountActivity extends AppCompatActivity {

    private EditText editUserName, editEmail, editLocation, editDescription;
    private Button submitButton;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_account);

        // Initialize EditText and Button views
        editUserName = findViewById(R.id.user_name);
        editEmail = findViewById(R.id.user_email);
        editLocation = findViewById(R.id.user_location);
        editDescription = findViewById(R.id.user_about);
        submitButton = findViewById(R.id.edit_button);

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

        // Get data from intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String location = intent.getStringExtra("location");
        String description = intent.getStringExtra("description");

        // Set the EditText fields with existing data
        editUserName.setText(username);
        editEmail.setText(email);
        editLocation.setText(location);
        editDescription.setText(description);

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the submit button click listener
        submitButton.setOnClickListener(view -> updateUserData());
    }

    private void updateUserData() {
        // Get updated data from EditTexts
        String updatedUserName = editUserName.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String updatedLocation = editLocation.getText().toString().trim();
        String updatedDescription = editDescription.getText().toString().trim();

        // Split full name into first name and last name
        String[] nameParts = updatedUserName.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Update the data in Firebase
        databaseReference.child("username").setValue(updatedUserName);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("location").setValue(updatedLocation);
        databaseReference.child("description").setValue(updatedDescription)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditAccountActivity.this, "Account updated successfully!", Toast.LENGTH_SHORT).show();
                        redirectToHomePage();
                    } else {
                        Toast.makeText(EditAccountActivity.this, "Failed to update account. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to redirect to home page
    public void redirectToHomePage() {
        startActivity(new Intent(this, NavigationBar.class));
        finish();
    }
}