package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeContent extends AppCompatActivity {

    private TextView textViewTitle, textViewTime, textViewDescription,textViewServings, textViewIngredients, textViewMethod, textViewAdditionalNotes;
    private Button publishButton;

    private DatabaseReference recipeRef;

    private PlayerView playerView;
    private ExoPlayer player;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);

        // Initialize UI elements
        textViewTitle = findViewById(R.id.textView_Title);
        textViewDescription = findViewById(R.id.textView_Description);
        textViewServings = findViewById(R.id.textView_Servings);
        textViewTime = findViewById(R.id.textView_Time);
        textViewIngredients = findViewById(R.id.textView_Ingredients);
        textViewMethod = findViewById(R.id.textView_Method);
        textViewAdditionalNotes = findViewById(R.id.textView_AdditionalNotes);
        publishButton = findViewById(R.id.PublishBtn);

        // Find the PlayerView in your layout
        playerView = findViewById(R.id.player_view);

        // Get recipe ID from intent
        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("recipeId");

        if (recipeId == null) {
            Toast.makeText(this, "Recipe ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get a reference to the Firebase database for the recipe
        recipeRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId);

        // Fetch recipe details from the database
        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get recipe data

                    String recipeTitle = dataSnapshot.child("recipeTitle").getValue(String.class);
                    String description = dataSnapshot.child("recipeDiscription").getValue(String.class);
                    String recipeTime = dataSnapshot.child("recipeTime").getValue(String.class);
                    String recipeServings = dataSnapshot.child("recipeServing").getValue(String.class);
                    String recipeIngredients = dataSnapshot.child("ingredient").getValue(String.class);
                    String recipeMethod = dataSnapshot.child("method").getValue(String.class);
                    String Notes = dataSnapshot.child("additionalmethod").getValue(String.class);
                    String imageUrl = dataSnapshot.child("recipeImageurl").getValue(String.class); // Fetch image URL
                    String videoUrl = dataSnapshot.child("recipeVideourl").getValue(String.class); // Fetch video URL
                    String recipeAdditionalNotes = dataSnapshot.child("additionalmethod").getValue(String.class);
                    String recipeURL = dataSnapshot.child("recipeVideourl").getValue(String.class);

                    // Set data to views

                    // Initialize ExoPlayer
                    player = new ExoPlayer.Builder(RecipeContent.this).build();
                    playerView.setPlayer(player);

                    Uri videoUri = Uri.parse(recipeURL);
                    MediaItem mediaItem = MediaItem.fromUri(videoUri);

                    player.setMediaItem(mediaItem);

                    player.prepare();

                    player.play();

                    textViewTitle.setText(recipeTitle != null ? recipeTitle : "Untitled");
                    textViewTime.setText(recipeTime != null ? recipeTime : "Unknown time");
                    textViewServings.setText(recipeServings != null ? recipeServings + " Servings" : "Servings not available");
                    textViewDescription.setText(description != null && !description.isEmpty() ? description : "Description not available");
                    textViewIngredients.setText(recipeIngredients != null ? recipeIngredients : "Ingredients not available");
                    textViewAdditionalNotes.setText(Notes != null ? Notes : "Ingredients not available");

                    // Separate ingredients by comma and display
                    if (recipeIngredients != null) {
                        String[] ingredients = recipeIngredients.split(",");
                        StringBuilder formattedIngredients = new StringBuilder();
                        for (String step : ingredients) {
                            formattedIngredients.append(step.trim()).append("\n");
                        }
                        textViewIngredients.setText(formattedIngredients.toString());
                    } else {
                        textViewIngredients.setText("Ingredients are not available");
                    }

                    // Separate methods by comma and display
                    if (recipeMethod != null) {
                        String[] methodSteps = recipeMethod.split(",");
                        StringBuilder formattedMethod = new StringBuilder();
                        for (String step : methodSteps) {
                            formattedMethod.append(step.trim()).append("\n");
                        }
                        textViewMethod.setText(formattedMethod.toString());
                    }

                    // Set the redirect to the Update Recipe Activity
                    publishButton.setOnClickListener(v -> redirectToUpdateRecipe(recipeId, recipeTitle, description, recipeTime,recipeServings, imageUrl, videoUrl, recipeIngredients, recipeMethod, Notes));

                    // Handle back press using OnBackPressedDispatcher
                    getOnBackPressedDispatcher().addCallback(RecipeContent.this, new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            Intent intent = new Intent(RecipeContent.this, NavigationBar.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();  // Finish the current activity
                        }
                    });
                } else {
                    Toast.makeText(RecipeContent.this, "Recipe not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(RecipeContent.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void redirectToUpdateRecipe(String recipeId,String title, String description, String duration,String serving, String imageUrl, String videoUrl, String recipeIngredients, String recipeMethod, String Notes) {
        Intent intent = new Intent(this, UpdateRecipeActivity.class);

        intent.putExtra("recipeId",recipeId);
        intent.putExtra("recipeTitle", title);
        intent.putExtra("recipeDiscription", description);
        intent.putExtra("recipeTime", duration);
        intent.putExtra("recipeServing",serving);
        intent.putExtra("recipeImageurl", imageUrl);
        intent.putExtra("recipeVideourl", videoUrl);
        intent.putExtra("ingredient", recipeIngredients);
        intent.putExtra("method", recipeMethod);
        intent.putExtra("additionalmethod", Notes);

        startActivity(intent);
    }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            // Release the player when not needed
            if (player != null) {
                player.release();
            }
        }

        public void redirectToLogin (View view){
            startActivity(new Intent(this, NavigationBar.class));

        }

        public void sendRecipe (View view){
            // Get the recipe details
            String title = textViewTitle.getText().toString();
            String time = textViewTime.getText().toString();
            String description = textViewDescription.getText().toString();
            String ingredients = textViewIngredients.getText().toString();
            String method = textViewMethod.getText().toString();
            String additionalNotes = textViewAdditionalNotes.getText().toString();

            // Format the recipe details into a message
            String recipeMessage = "Recipe: " + title + "\n\n" +
                    "Time: " + time + "\n\n" +
                    "Description: " + description + "\n\n" +
                    "Ingredients:\n" + ingredients + "\n" +
                    "Method:\n" + method + "\n" +
                    "Additional Notes:\n" + additionalNotes;

            // Create the intent for sending text
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, recipeMessage);
            intent.setType("text/plain");

            // Check if any app can handle the intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Send Recipe via"));
            }
        }
}
