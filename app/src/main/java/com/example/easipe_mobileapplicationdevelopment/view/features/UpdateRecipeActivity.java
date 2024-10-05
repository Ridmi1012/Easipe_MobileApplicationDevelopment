package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.example.easipe_mobileapplicationdevelopment.R;

import java.util.ArrayList;

public class UpdateRecipeActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextServings, editTextTime, editTextingredients, editTextAdditionalNotes ;
    private ImageView recipeImageView;
    private VideoView recipeVideoView;
    private ExoPlayer player;

    private Button publishBtn, selectImgBtn, selectVideoBtn, addIngredientBtn, addStepsBtn;

    private LinearLayout methodsContainer; // This should be defined in your XML layout
    private ArrayList<EditText> methodFields;
    private ArrayList<EditText> methodFields1;

    private Uri imageUri;
    private Uri videoUri;

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

        // Initialize image picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        // Load the selected image into ImageView
                        Glide.with(UpdateRecipeActivity.this)
                                .load(imageUri)
                                .error(R.drawable.baseline_add_a_photo_24) // Error placeholder
                                .into(recipeImageView);
                    }
                });

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

        selectVideoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            videoPickerLauncher.launch(intent);
        });

        // Retrieve data from Intent (title, description, etc.)
        String title = getIntent().getStringExtra("recipeTitle");
        String description = getIntent().getStringExtra("recipeDiscription");
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
            methodFields = new ArrayList<>(); // Initialize the list

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
                methodFields.add(newMethodField);
            }


        }

        if (methods != null) {
            methodsContainer = findViewById(R.id.methods_container1);
            methodFields1 = new ArrayList<>();

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
            methodFields = new ArrayList<>();

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
            methodFields.add(newMethodField);
        });

        // Add ingredient button listener to dynamically add new EditText fields
        addStepsBtn.setOnClickListener(v -> {
            methodsContainer = findViewById(R.id.methods_container1); // Find your LinearLayout
            methodFields1 = new ArrayList<>();

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

    @Override
    protected void onStop() {
        super.onStop();
        // Release player resources
        player.release();
    }
}
