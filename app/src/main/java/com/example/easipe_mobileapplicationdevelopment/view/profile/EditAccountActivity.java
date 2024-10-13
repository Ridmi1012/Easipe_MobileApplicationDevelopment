package com.example.easipe_mobileapplicationdevelopment.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import com.example.easipe_mobileapplicationdevelopment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

public class EditAccountActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText editUserName, editEmail, editLocation, editDescription;
    private Button submitButton;
    private ImageView profileImageView, editImageIcon;
    private DatabaseReference databaseReference;
    private String userId, profileImageURL;
    private ProgressBar progressBar;
    private Uri imageUri;

    // Firebase storage
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        progressBar = findViewById(R.id.progressBar);

        editUserName = findViewById(R.id.user_name);
        editEmail = findViewById(R.id.user_email);
        editLocation = findViewById(R.id.user_location);
        editDescription = findViewById(R.id.user_about);
        submitButton = findViewById(R.id.edit_button);
        profileImageView = findViewById(R.id.profile_image);
        editImageIcon = findViewById(R.id.edit_image_button);

        // Disable the email field
        editEmail.setEnabled(false);

        // Fetch user ID from Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            Log.d("UserID", "Current User ID: " + userId);
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
        profileImageURL = intent.getStringExtra("profileImageURL");  // Get the existing profile image URL

        // Showing the EditText fields with current user data
        editUserName.setText(username);
        editEmail.setText(email);
        editLocation.setText(location);
        editDescription.setText(description);

        // Load existing profile image using Glide
        if (profileImageURL != null && !profileImageURL.isEmpty()) {
            Glide.with(this)
                    .load(profileImageURL)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(profileImageView);
        }

        editImageIcon.setOnClickListener(view -> openGallery());
        submitButton.setOnClickListener(view -> updateUserData());
    }

    // Method to open gallery
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {

            // Getting the URI and showing it on the ImageView
            imageUri = data.getData();
            Glide.with(EditAccountActivity.this)
                    .load(imageUri)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(profileImageView);
        }
    }

    private void updateUserData() {
        // Get updated data from EditTexts
        String updatedUserName = editUserName.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String updatedLocation = editLocation.getText().toString().trim();
        String updatedDescription = editDescription.getText().toString().trim();

        // Save to Firebase Database
        if (imageUri != null) {
            String imagePath = "profileImages/" + userId + ".jpg";
            StorageReference imageReference = storageReference.child(imagePath);
            imageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String profileImageURL = uri.toString();;

                    progressBar.setVisibility(View.VISIBLE);
                    // Update user data in the database
                    updateDatabase(updatedUserName, updatedEmail, updatedLocation, updatedDescription, profileImageURL);
                });
            }).addOnFailureListener(e -> Toast.makeText(EditAccountActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        } else {

            progressBar.setVisibility(View.VISIBLE);

            // Update user data without changing the profile image
            updateDatabase(updatedUserName, updatedEmail, updatedLocation, updatedDescription, profileImageURL);
        }
    }

    private void updateDatabase(String updatedUserName, String updatedEmail, String updatedLocation, String updatedDescription, String profileImageURL) {
        // Update the user data in Firebase
        databaseReference.child("username").setValue(updatedUserName);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("location").setValue(updatedLocation);
        databaseReference.child("description").setValue(updatedDescription);
        databaseReference.child("profileImageURL").setValue(profileImageURL);

        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        // Hide the progress bar
        progressBar.setVisibility(View.GONE);

        finish();
    }
}
