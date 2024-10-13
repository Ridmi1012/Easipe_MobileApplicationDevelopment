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
import android.widget.ProgressBar;
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
import com.example.easipe_mobileapplicationdevelopment.view.GetStartedActivity;
import com.example.easipe_mobileapplicationdevelopment.view.auth.LoginActivity;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.HomeFragment;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

public class UpdateRecipeActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText editTextTitle, editTextDescription, editTextServings, editTextTime, editTextIngredient, editTextMethod,editTextAdditionalNotes ;
    private ImageView recipeImageView;
    private VideoView recipeVideoView;
    private ExoPlayer player;

    private Button publishBtn, selectImgBtn, selectVideoBtn, addIngredientBtn, addStepsBtn;

    private LinearLayout ingredientsContainer,methodsContainer; // This should be defined in your XML layout
    private ArrayList<EditText> ingredientsField;
    private ArrayList<EditText> methodFields;

    private DatabaseReference recipeRef,savedRecipeRef;
    private StorageReference storageRef;

    private Uri imageUri;
    private Uri videoUri;
    
    private String imageUrl;
    private  String videoUrl;


    private ImageView recipeimg;

    // ActivityResultLaunchers for selecting image and video
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> videoPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);

        progressBar = findViewById(R.id.progressBar);

        // Initialize EditText fields
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextServings = findViewById(R.id.editTextServings);
        editTextTime = findViewById(R.id.editTextDuration);
        recipeImageView = findViewById(R.id.recipeimg);
        recipeVideoView = findViewById(R.id.recipeVideoView);
        ingredientsContainer =findViewById(R.id.ingredientsContainer);
        methodsContainer = findViewById(R.id.methodsContainer);
        editTextAdditionalNotes = findViewById(R.id.editTextAddition);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        addStepsBtn = findViewById(R.id.AddMethodBtn);
        selectImgBtn = findViewById(R.id.SelectImgBtn);
        selectVideoBtn = findViewById(R.id.SelectVideoBtn);

        // Initialize the lists for ingredients and methods
        ingredientsField = new ArrayList<>();
        methodFields = new ArrayList<>();

        // Initialize the player
        player = new ExoPlayer.Builder(this).build();



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
            ingredientsContainer = findViewById(R.id.ingredientsContainer);
            ingredientsField = new ArrayList<>();

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
                ingredientsContainer.addView(newMethodField);

                // Add the new EditText to the list of method fields to track them
                ingredientsField.add(newMethodField);
            }


        }

        if (methods != null) {
            methodsContainer = findViewById(R.id.methodsContainer);
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
            ingredientsContainer.addView(newMethodField);

            // Add the new EditText to the list of method fields to track them
            ingredientsField.add(newMethodField);
        });

        // Add method button listener to dynamically add new EditText fields
        addStepsBtn.setOnClickListener(v -> {

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


        // Get recipe ID from intent or create a new one if necessary
        String recipeId = getIntent().getStringExtra("recipeId");
        if (recipeId == null) {
            Toast.makeText(UpdateRecipeActivity.this, "Cannot fetch recipe ID", Toast.LENGTH_SHORT).show();
            return;
        }


        // Get the existing image and video URLs from intent
        String currentImageUrl = getIntent().getStringExtra("recipeImageurl");
        String currentVideoUrl = getIntent().getStringExtra("recipeVideourl");

        // If the user selected new image, upload it. Otherwise, use the existing image URL.
        if (imageUri != null) {
            uploadImage();
        } else {
            imageUrl = currentImageUrl;
        }

        // If the user selected new video, upload it. Otherwise, use the existing video URL.
        if (videoUri != null) {
            uploadVideo();
        } else {
            videoUrl = currentVideoUrl;
        }

        progressBar.setVisibility(View.VISIBLE);

        checkUploadsComplete();

    }

    private void checkUploadsComplete() {
        // Check if both imageUrl and videoUrl are not null
        if (imageUrl != null && (videoUri == null || videoUrl != null)) {
            // Both uploads are done (or no new uploads), proceed to update the recipe
            updateRecipeInDatabase(imageUrl, videoUrl);
        }
    }

    private void updateRecipeInDatabase(String imageUrl, String videoUrl) {

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

        // Collect all existing and newly added ingredients
        StringBuilder ingredientsList = new StringBuilder();

      // First, get the existing ingredients that were loaded from Firebase
        for (EditText ingredientField : ingredientsField) {
            String ingredient = ingredientField.getText().toString();
            if (!ingredient.isEmpty()) {
                if (ingredientsList.length() > 0) ingredientsList.append(", ");
                ingredientsList.append(ingredient);
            }
        }

     // Collect all existing and newly added methods (steps)
        StringBuilder methodList = new StringBuilder();

     // First, get the existing steps that were loaded from Firebase
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
            Toast.makeText(UpdateRecipeActivity.this, "Cannot fetch recipe ID", Toast.LENGTH_SHORT).show();
            return;
        }

        recipeRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId);
        savedRecipeRef =FirebaseDatabase.getInstance().getReference("user_saved_recipes");


        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Fetch the averageRating from within the ratings node
                DataSnapshot ratingsSnapshot = snapshot.child("ratings");
                Float currentAverageRating = ratingsSnapshot.child("averageRating").getValue(float.class);

                if (currentAverageRating == null) {
                    currentAverageRating = 0f;
                }
                boolean status = Boolean.TRUE.equals(snapshot.child("issaved").getValue(boolean.class));

                //Create a Recipe object to hold the updated data
                Recipe recipe = new Recipe(userId, title, description, serving, duration,
                        ingredientsList.toString(), methodList.toString(),
                        additionalMethod, imageUrl, videoUrl, status, recipeId, currentAverageRating );

                // Update recipe in Firebase
                recipeRef.setValue(recipe)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateRecipeActivity.this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                                // Hide the progress bar
                                progressBar.setVisibility(View.GONE);

                                savedRecipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                            // Check if this user saved the current recipe
                                            if (userSnapshot.hasChild(recipeId)) {
                                                // Get the reference to the specific user's saved recipe
                                                DatabaseReference userRecipeRef = savedRecipeRef.child(userSnapshot.getKey()).child(recipeId);

                                                // Update the recipe for this user
                                                userRecipeRef.setValue(recipe).addOnCompleteListener(savetask -> {
                                                    if (savetask.isSuccessful()) {
                                                        Toast.makeText(UpdateRecipeActivity.this, "Recipe updated for user: " + userSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                                        // Hide the progress bar
                                                        progressBar.setVisibility(View.GONE);
                                                    } else {
                                                        Toast.makeText(UpdateRecipeActivity.this, "Failed to update recipe for user: " + userSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                                        // Hide the progress bar
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(UpdateRecipeActivity.this, "Failed to update saved recipe", Toast.LENGTH_SHORT).show();
                                        // Hide the progress bar
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });
                                startActivity(new Intent(UpdateRecipeActivity.this, NavigationBar.class));
                            } else {
                                Toast.makeText(UpdateRecipeActivity.this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
                                // Hide the progress bar
                                progressBar.setVisibility(View.GONE);
                            }
                        });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateRecipeActivity.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void uploadVideo() {

        if (videoUri != null) {
            String videoName = UUID.randomUUID().toString() + ".mp4";
            StorageReference videoRef = FirebaseStorage.getInstance().getReference("videos/" + videoName);

            videoRef.putFile(videoUri)
                    .addOnSuccessListener(taskSnapshot -> videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        videoUrl = uri.toString();
                        checkUploadsComplete();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(UpdateRecipeActivity.this, "Video upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            checkUploadsComplete();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            String imageName = UUID.randomUUID().toString() + ".jpg";
            StorageReference imageRef = FirebaseStorage.getInstance().getReference("images/" + imageName);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrl = uri.toString();
                        checkUploadsComplete();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(UpdateRecipeActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            checkUploadsComplete();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Release player resources
        player.release();
    }

    public void redirectToHomePage(View view) {
        startActivity(new Intent(this, NavigationBar.class));
    }
}
