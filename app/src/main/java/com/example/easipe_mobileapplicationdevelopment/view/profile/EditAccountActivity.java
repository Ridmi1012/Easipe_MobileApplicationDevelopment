package com.example.easipe_mobileapplicationdevelopment.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;
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
    private String userId;
    private Uri imageUri;

    // Firebase storage
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Initialize views
        editUserName = findViewById(R.id.user_name);
        editEmail = findViewById(R.id.user_email);
        editLocation = findViewById(R.id.user_location);
        editDescription = findViewById(R.id.user_about);
        submitButton = findViewById(R.id.edit_button);
        profileImageView = findViewById(R.id.profile_image);
        editImageIcon = findViewById(R.id.edit_image_button);

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

        // Set the EditText fields with existing data
        editUserName.setText(username);
        editEmail.setText(email);
        editLocation.setText(location);
        editDescription.setText(description);

        // ImageView click to choose a profile image
        editImageIcon.setOnClickListener(view -> openGallery());

        // Set up the submit button click listener
        submitButton.setOnClickListener(view -> updateUserData());
    }

    // Method to open gallery for image selection
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            imageUri = data.getData();

            // Set the selected image to the ImageView (profile picture)
            Glide.with(EditAccountActivity.this)
                    .load(imageUri)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop())) // Apply CircleCrop transformation
                    .into(profileImageView);

        }
    }

    private void updateUserData() {
        // Get updated data from EditTexts
        String updatedUserName = editUserName.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String updatedLocation = editLocation.getText().toString().trim();
        String updatedDescription = editDescription.getText().toString().trim();

        // Check if an image was selected and upload it to Firebase Storage
        if (imageUri != null) {
            uploadImageToFirebase(imageUri, updatedUserName, updatedEmail, updatedLocation, updatedDescription);
        } else {
            // If no image is selected, just update the text fields
            saveUserInfo(updatedUserName, updatedEmail, updatedLocation, updatedDescription, null);
        }
    }

    private void uploadImageToFirebase(Uri imageUri, String updatedUserName, String updatedEmail, String updatedLocation, String updatedDescription) {
        // Create a storage reference for the image
        StorageReference fileReference = storageReference.child("profile_images/" + userId + "_profile.jpg");

        // Upload the image
        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL for the uploaded image
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();

                // Save user info with the profile image URL
                saveUserInfo(updatedUserName, updatedEmail, updatedLocation, updatedDescription, imageUrl);

            }).addOnFailureListener(e -> {
                Toast.makeText(EditAccountActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(EditAccountActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveUserInfo(String updatedUserName, String updatedEmail, String updatedLocation, String updatedDescription, String profileImageUrl) {
        // Update the data in Firebase Realtime Database
        databaseReference.child("username").setValue(updatedUserName);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("location").setValue(updatedLocation);
        databaseReference.child("description").setValue(updatedDescription);
        if (profileImageUrl != null) {
            databaseReference.child("profileImageURL").setValue(profileImageUrl);
        }
        databaseReference.child("profileImageURL").setValue(profileImageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditAccountActivity.this, "Account updated successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to MyAccountActivity
                    } else {
                        Toast.makeText(EditAccountActivity.this, "Failed to update account. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



