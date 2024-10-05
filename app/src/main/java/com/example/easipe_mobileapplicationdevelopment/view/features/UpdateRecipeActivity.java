package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

    private Button publishBtn, selectImgBtn,selectVideoBtn, addIngredientBtn, addStepsBtn;

    private LinearLayout methodsContainer; // This should be defined in your XML layout
    private ArrayList<EditText> methodFields;
    private ArrayList<EditText> methodFields1;

    private Uri imageUri;
    private Uri videoUri;

    // Create activity result launchers for image and video selection
    private final ActivityResultLauncher<Intent> getImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        recipeImageView.setImageURI(imageUri);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> getVideoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    videoUri = result.getData().getData();
                    if (videoUri != null) {
                        player.setMediaItem(MediaItem.fromUri(videoUri));
                        player.prepare();
                        player.play();
                    }
                }
            });
    private ImageView recipeimg;

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

        // Initialize the player
        player = new ExoPlayer.Builder(this).build();

        // Retrieve data from Intent (title, description, etc.)
        String title = getIntent().getStringExtra("recipeTitle");
        String description = getIntent().getStringExtra("recipeDiscription");
        String duration = getIntent().getStringExtra("recipeTime");
        String imageUrl = getIntent().getStringExtra("recipeImageurl");
        Uri videoUrl = Uri.parse(getIntent().getStringExtra("recipeVideourl"));
        String ingredients =getIntent().getStringExtra("ingredient");
        String methods =getIntent().getStringExtra("method");
        String additionalNotes =getIntent().getStringExtra("additionalmethod");

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

        // Load video into the VideoView using ExoPlayer
        if (videoUrl != null) {
            recipeVideoView.setVideoURI(videoUrl);
            recipeVideoView.setVisibility(View.VISIBLE);
            recipeVideoView.start();
        }

        if (ingredients != null) {
            methodsContainer = findViewById(R.id.methods_container);
            methodFields = new ArrayList<>();

            String[] recipeIngredients = ingredients.split(",");

            for (String step : recipeIngredients) {
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

                newMethodField.setTextSize(15);

                methodsContainer.addView(newMethodField);

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
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(15, 8, 15, 0); // Add margins as per your design

            newMethodField.setLayoutParams(layoutParams);

            // Apply the custom background
            newMethodField.setBackgroundResource(R.drawable.rounded_background);

            newMethodField.setTextSize(15);

            methodsContainer.addView(newMethodField);

            methodFields.add(newMethodField);
        });

        // Add ingredient button listener to dynamically add new EditText fields
        addStepsBtn.setOnClickListener(v -> {
            methodsContainer = findViewById(R.id.methods_container1);
            methodFields1 = new ArrayList<>();

            // Create a new EditText for the new ingredient
            EditText newMethodField = new EditText(this);
            newMethodField.setHint("Enter another step");

            // Set layout parameters for the new EditText
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(15, 8, 15, 0);

            newMethodField.setLayoutParams(layoutParams);

            // Apply the custom background
            newMethodField.setBackgroundResource(R.drawable.rounded_background);


            newMethodField.setTextSize(15);

            methodsContainer.addView(newMethodField);

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
