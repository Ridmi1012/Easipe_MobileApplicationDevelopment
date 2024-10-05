package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.navbar.NavigationBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeContent extends AppCompatActivity {

    private TextView textViewTitle, textViewTime, textViewDescription, textViewIngredients, textViewMethod, textViewAdditionalNotes;
    private Button publishButton;

    private DatabaseReference recipeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);

        // Initialize UI elements
        textViewTitle = findViewById(R.id.textView_Title);
        textViewDescription = findViewById(R.id.textView_Description);
        textViewTime = findViewById(R.id.textView_Time);
        textViewIngredients = findViewById(R.id.textView_Ingredients);
        textViewMethod = findViewById(R.id.textView_Method);
        textViewAdditionalNotes = findViewById(R.id.textView_AdditionalNotes);
        publishButton = findViewById(R.id.PublishBtn);

        // Get recipe ID from intent (passed from previous activity)
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
                    String recipeIngredients = dataSnapshot.child("ingredient").getValue(String.class);
                    String recipeMethod = dataSnapshot.child("method").getValue(String.class);
                    String Notes = dataSnapshot.child("additionalmethod").getValue(String.class);
                    String imageUrl = dataSnapshot.child("recipeImageurl").getValue(String.class); // Fetch image URL
                    String videoUrl = dataSnapshot.child("recipeVideourl").getValue(String.class); // Fetch video URL

                    // Set data to views
                    textViewTitle.setText(recipeTitle != null ? recipeTitle : "Untitled");
                    textViewTime.setText(recipeTime != null ? recipeTime : "Unknown time");
                    textViewDescription.setText(description != null && !description.isEmpty() ? description : "Description not available");
                    textViewIngredients.setText(recipeIngredients != null ? recipeIngredients : "Ingredients not available");
                    textViewAdditionalNotes.setText(Notes != null ? Notes : "Ingredients not available");

                    // Separate ingredients by comma and display
                    if (recipeIngredients != null) {
                        String[] ingredients = recipeIngredients.split(",");
                        StringBuilder formattedIngredients = new StringBuilder();
                        for (String step : ingredients) {
                            formattedIngredients.append(step.trim()).append("\n"); // Append each step with a newline
                        }
                        textViewIngredients.setText(formattedIngredients.toString());
                    }

                    // Separate methods by comma and display
                    if (recipeMethod != null) {
                        String[] methodSteps = recipeMethod.split(",");
                        StringBuilder formattedMethod = new StringBuilder();
                        for (String step : methodSteps) {
                            formattedMethod.append(step.trim()).append("\n"); // Append each step with a newline
                        }
                        textViewMethod.setText(formattedMethod.toString());
                    }

                    // Set the redirect to the Update Recipe Activity
                    publishButton.setOnClickListener(v -> redirectToUpdateRecipe(recipeTitle, description, recipeTime, imageUrl, videoUrl, recipeIngredients, recipeMethod, Notes));

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

    public void redirectToUpdateRecipe(String title, String description, String duration, String imageUrl, String videoUrl, String recipeIngredients, String recipeMethod, String Notes) {
        Intent intent = new Intent(this, UpdateRecipeActivity.class); // Create an Intent instance
        intent.putExtra("recipeTitle", title);
        intent.putExtra("recipeDiscription", description);
        intent.putExtra("recipeTime", duration);
        intent.putExtra("recipeImageurl", imageUrl); // Pass image URL
        intent.putExtra("recipeVideourl", videoUrl); // Pass video URL
        intent.putExtra("ingredient", recipeIngredients);
        intent.putExtra("method", recipeMethod);
        intent.putExtra("additionalmethod", Notes);

        startActivity(intent);
        finish();
    }
}
