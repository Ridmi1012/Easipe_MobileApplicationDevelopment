package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.example.easipe_mobileapplicationdevelopment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UpdateRecipeActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextServings, editTextTime, editTextingredients, editTextAdditionalNotes ;
    private ImageView recipeImageView;
    private VideoView recipeVideoView;
    private ExoPlayer player;

    private Button publishBtn, selectImgBtn, selectVideoBtn, addIngredientBtn, addStepsBtn;

    private LinearLayout methodsContainer; // This should be defined in your XML layout
    private ArrayList<EditText> ingredientsField;
    private ArrayList<EditText> methodFields;

    private DatabaseReference recipeRef;
    private StorageReference storageRef;

    private Uri imageUri;
    private Uri videoUri;

    private String existingImageUrl;
    private String existingVideoUrl;

    private ImageView recipeimg;

    // ActivityResultLaunchers for selecting image and video
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> videoPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);

        // Initialize EditText fields
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextServings = findViewById(R.id.editTextServings);
        editTextTime = findViewById(R.id.editTextDuration);
        recipeImageView = findViewById(R.id.recipeimg);
        recipeVideoView = findViewById(R.id.recipeVideoView);
        editTextAdditionalNotes = findViewById(R.id.editTextAddition);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        addStepsBtn = findViewById(R.id.AddMethodBtn);
        selectImgBtn = findViewById(R.id.SelectImgBtn);
        selectVideoBtn = findViewById(R.id.SelectVideoBtn);

        // Initialize the player
        player = new ExoPlayer.Builder(this).build();



//        // Initialize image picker
//        imagePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        imageUri = result.getData().getData();
//                        // Load the selected image into ImageView
//                        Glide.with(UpdateRecipeActivity.this)
//                                .load(imageUri)
//                                .error(R.drawable.baseline_add_a_photo_24) // Error placeholder
//                                .into(recipeImageView);
//                    }
//                });

        // Initialize video picker
        videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        videoUri = result.getData().getData();
                        // Load the selected video into VideoView
                        recipeVideoView.setVideoURI(videoUri);
                        recipeVideoView.setVisibility(View.VISIBLE);
                        recipeVideoView.start();
                    }
                });

        // Set up click listeners for buttons to select image and video
        selectImgBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Image picker result handler
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        if (imageUri != null) {
                            // Load the selected image into ImageView
                            Glide.with(UpdateRecipeActivity.this)
                                    .load(imageUri)
                                    .error(R.drawable.baseline_add_a_photo_24)
                                    .into(recipeImageView);
                            Toast.makeText(this, "New image selected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        selectVideoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            videoPickerLauncher.launch(intent);
        });

        // Retrieve data from Intent (title, description, etc.)
        String title = getIntent().getStringExtra("recipeTitle");
        String description = getIntent().getStringExtra("recipeDiscription");
        String serving = getIntent().getStringExtra("recipeServing");
        String duration = getIntent().getStringExtra("recipeTime");
        String imageUrl = getIntent().getStringExtra("recipeImageurl"); // Fetch image URL
        Uri videoUrl = Uri.parse(getIntent().getStringExtra("recipeVideourl"));// Fetch video URL
        String ingredients = getIntent().getStringExtra("ingredient");
        String methods = getIntent().getStringExtra("method");
        String additionalNotes = getIntent().getStringExtra("additionalmethod");

        existingImageUrl = getIntent().getStringExtra("recipeImageurl");
        existingVideoUrl = getIntent().getStringExtra("recipeVideourl");

        // Load the image into the ImageView using Glide
        if (existingImageUrl != null) {
            Glide.with(this)
                    .load(existingImageUrl)
                    .error(R.drawable.baseline_add_a_photo_24)
                    .into(recipeImageView);
        }

        // Load the video into the VideoView
        if (existingVideoUrl != null) {
            recipeVideoView.setVideoURI(Uri.parse(existingVideoUrl));
            recipeVideoView.setVisibility(View.VISIBLE);
            recipeVideoView.start();
        }

        // Set values to EditText fields
        if (title != null) {
            editTextTitle.setText(title);
        }

        if (description != null) {
            editTextDescription.setText(description);
        }

        if (duration != null) {
            editTextTime.setText(duration);
        }

        if(serving != null){
            editTextServings.setText(serving);
        }

        if (additionalNotes != null) {
            editTextAdditionalNotes.setText(additionalNotes);
        }

        // Load image into the ImageView using Glide
        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .error(R.drawable.baseline_add_a_photo_24) // Error placeholder
                    .into(recipeImageView);
        }

        // Load video into the VideoView
        if (videoUrl != null) {
            recipeVideoView.setVideoURI(videoUrl); // Display the selected video in VideoView
            recipeVideoView.setVisibility(View.VISIBLE); // Make the VideoView visible
            recipeVideoView.start();
        }

        if (ingredients != null) {
            methodsContainer = findViewById(R.id.methods_container); // Find your LinearLayout
            ingredientsField = new ArrayList<>(); // Initialize the list

            String[] recipeIngredients = ingredients.split(",");

            for (String step : recipeIngredients) {
                EditText newMethodField = new EditText(this);
                newMethodField.setText(step);

                // Set layout parameters for the new EditText
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // or widthInPx
                        LinearLayout.LayoutParams.WRAP_CONTENT // height
                );
                layoutParams.setMargins(15, 8, 15, 0); // Add margins as per your design

                newMethodField.setLayoutParams(layoutParams);

                // Apply the custom background (rounded corners, padding, and border) from the drawable XML
                newMethodField.setBackgroundResource(R.drawable.rounded_background);

                // Optionally set text size and other properties
                newMethodField.setTextSize(15);

                // Add the new EditText to the methods container (LinearLayout)
                methodsContainer.addView(newMethodField);

                // Add the new EditText to the list of method fields to track them
                ingredientsField.add(newMethodField);
            }


        }

        if (methods != null) {
            methodsContainer = findViewById(R.id.methods_container1);
            methodFields = new ArrayList<>();

            String[] recipeMethods = methods.split(",");

            for (String step : recipeMethods) {
                EditText newMethodField = new EditText(this);
                newMethodField.setText(step);

                // Set layout parameters for the new EditText
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(15, 8, 15, 0);

                newMethodField.setLayoutParams(layoutParams);

                // Apply the custom background
                newMethodField.setBackgroundResource(R.drawable.rounded_background);

                // Optionally set text size and other properties
                newMethodField.setTextSize(15);

                methodsContainer.addView(newMethodField);

                methodFields.add(newMethodField);
            }
        }

        // Add ingredient button listener to dynamically add new EditText fields
        addIngredientBtn.setOnClickListener(v -> {
            methodsContainer = findViewById(R.id.methods_container);
            ingredientsField = new ArrayList<>();

            // Create a new EditText for the new ingredient
            EditText newMethodField = new EditText(this);
            newMethodField.setHint("Enter additional ingredients");

            // Set layout parameters for the new EditText
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // or widthInPx
                    LinearLayout.LayoutParams.WRAP_CONTENT // height
            );
            layoutParams.setMargins(15, 8, 15, 0); // Add margins as per your design

            newMethodField.setLayoutParams(layoutParams);

            // Apply the custom background (rounded corners, padding, and border) from the drawable XML
            newMethodField.setBackgroundResource(R.drawable.rounded_background);

            // Optionally set text size and other properties
            newMethodField.setTextSize(15);

            // Add the new EditText to the methods container (LinearLayout)
            methodsContainer.addView(newMethodField);

            // Add the new EditText to the list of method fields to track them
            ingredientsField.add(newMethodField);
        });

        // Add ingredient button listener to dynamically add new EditText fields
        addStepsBtn.setOnClickListener(v -> {
            methodsContainer = findViewById(R.id.methods_container1); // Find your LinearLayout
            methodFields = new ArrayList<>();

            // Create a new EditText for the new ingredient
            EditText newMethodField = new EditText(this);
            newMethodField.setHint("Enter another step");

            // Set layout parameters for the new EditText
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // or widthInPx
                    LinearLayout.LayoutParams.WRAP_CONTENT // height
            );
            layoutParams.setMargins(15, 8, 15, 0); // Add margins as per your design

            newMethodField.setLayoutParams(layoutParams);

            // Apply the custom background (rounded corners, padding, and border) from the drawable XML
            newMethodField.setBackgroundResource(R.drawable.rounded_background);

            // Optionally set text size and other properties
            newMethodField.setTextSize(15);

            // Add the new EditText to the methods container (LinearLayout)
            methodsContainer.addView(newMethodField);

            // Add the new EditText to the list of method fields to track them
            methodFields.add(newMethodField);
        });
    }


    public void updateRecipe(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId;

        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Log.e("Firebase", "User is not authenticated");
            return;
        }

        // Get the data from the EditText fields
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String serving = editTextServings.getText().toString();
        String duration = editTextTime.getText().toString();
        String additionalMethod = editTextAdditionalNotes.getText().toString();

        // Collect dynamically added ingredients
        StringBuilder ingredientsList = new StringBuilder();
        for (EditText ingredientField : ingredientsField) {
            String ingredient = ingredientField.getText().toString();
            if (!ingredient.isEmpty()) {
                if (ingredientsList.length() > 0) ingredientsList.append(", ");
                ingredientsList.append(ingredient);
            }
        }

        // Collect dynamically added method steps
        StringBuilder methodList = new StringBuilder();
        for (EditText methodField : methodFields) {
            String method = methodField.getText().toString();
            if (!method.isEmpty()) {
                if (methodList.length() > 0) methodList.append(", ");
                methodList.append(method);
            }
        }

        // Get recipe ID from intent or create a new one if necessary
        String recipeId = getIntent().getStringExtra("recipeId");
        if (recipeId == null) {
            Toast.makeText(UpdateRecipeActivity.this, "Can't fetch recipe ID", Toast.LENGTH_SHORT).show();
            return;
        }

        recipeRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId);

        // Upload image and video, and update the recipe when both uploads are complete
        uploadImageAndVideo(userId, title, description, serving, duration, ingredientsList.toString(), methodList.toString(), additionalMethod, recipeId);
        
    }

    private void uploadImageAndVideo(String userId, String title, String description, String serving, String duration, String ingredients, String methods, String additionalNotes, String recipeId) {

        // Check if the user selected a new image
        if (imageUri == null && existingImageUrl != null) {
            // Use existing image URL if no new image is selected
            imageUri = Uri.parse(existingImageUrl);
        }

        // Check if the user selected a new video
        if (videoUri == null && existingVideoUrl != null) {
            // Use existing video URL if no new video is selected
            videoUri = Uri.parse(existingVideoUrl);
        }

        if (imageUri == null && videoUri == null) {
            // If no image or video selected
            Toast.makeText(UpdateRecipeActivity.this, "Select image", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri != null) {
                // Upload image if a new image was selected
                StorageReference imageRef = FirebaseStorage.getInstance().getReference("images/" + recipeId + "/image.jpg");
                imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(imageDownloadUrl -> {
                        // Continue to upload video after image is uploaded
                        if (videoUri != null) {
                            uploadVideo(userId, title, description, serving, duration, ingredients, methods, additionalNotes, recipeId, imageDownloadUrl.toString());
                        } else {
                            // No video to upload, so update the recipe with the new image URL
                            Toast.makeText(UpdateRecipeActivity.this, "Select video", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).addOnFailureListener(e -> {
                    Log.e("Firebase", "Image upload failed", e);
                    Toast.makeText(UpdateRecipeActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
            } else if (videoUri != null && !videoUri.toString().equals(existingVideoUrl)) {
                // Only video is being uploaded (image remains unchanged)
                uploadVideo(userId, title, description, serving, duration, ingredients, methods, additionalNotes, recipeId, existingImageUrl);
            }
        }
    }
    private void uploadVideo(String userId, String title, String description, String serving, String duration, String ingredients, String methods, String additionalNotes, String recipeId, String imageUrl) {
        StorageReference videoRef = FirebaseStorage.getInstance().getReference("videos/" + recipeId + "/video.mp4");
        videoRef.putFile(videoUri).addOnSuccessListener(taskSnapshot -> {
            videoRef.getDownloadUrl().addOnSuccessListener(videoDownloadUrl -> {
                // Now that video is uploaded, update the recipe with image and video URLs
                updateRecipeInDatabase(userId, title, description, serving, duration, ingredients, methods, additionalNotes, recipeId, imageUrl, videoDownloadUrl.toString());
            });
        }).addOnFailureListener(e -> {
            Log.e("Firebase", "Video upload failed", e);
            Toast.makeText(UpdateRecipeActivity.this, "Failed to upload video", Toast.LENGTH_SHORT).show();
        });
    }
    private void updateRecipeInDatabase(String userId, String title, String description, String serving, String duration, String ingredients, String methods, String additionalNotes, String recipeId, String imageUrl, String videoUrl) {
        recipeRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId);

        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean status = Boolean.TRUE.equals(snapshot.child("issaved").getValue(boolean.class));
                float rating = snapshot.child("ratings").getValue(float.class);

                Recipe recipe = new Recipe(userId, title, description, serving, duration, ingredients, methods, additionalNotes, imageUrl, videoUrl, status, recipeId);

                // Update recipe in Firebase
                recipeRef.setValue(recipe)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateRecipeActivity.this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdateRecipeActivity.this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateRecipeActivity.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Release player resources
        player.release();
    }

}
