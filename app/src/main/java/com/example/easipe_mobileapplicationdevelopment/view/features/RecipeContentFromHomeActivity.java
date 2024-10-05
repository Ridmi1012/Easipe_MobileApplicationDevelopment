package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.annotation.SuppressLint;
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

import java.io.File;
import java.io.FileOutputStream;

public class RecipeContentFromHomeActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewTime, textViewServings, textViewDescription, textViewIngredients, textViewMethod, textViewAdditionalNotes;

    private DatabaseReference recipeRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content_from_home);

        // Initialize UI elements
        textViewTitle = findViewById(R.id.textView_Title);
        textViewDescription = findViewById(R.id.textView_Description);
        textViewTime = findViewById(R.id.textView_Time);
        textViewServings = findViewById(R.id.textView_Servings);
        textViewIngredients = findViewById(R.id.textView_Ingredients);
        textViewMethod = findViewById(R.id.textView_Method);
        textViewAdditionalNotes = findViewById(R.id.textView_AdditionalNotes);


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
                    // Get recipe data (recipeRating in the database is equal to the servings here)
                    String recipeTitle = dataSnapshot.child("recipeTitle").getValue(String.class);
                    String description = dataSnapshot.child("recipeDiscription").getValue(String.class);
                    String recipeTime = dataSnapshot.child("recipeTime").getValue(String.class);
                    Long recipeServings = dataSnapshot.child("recipeRating").getValue(Long.class);
                    String recipeIngredients = dataSnapshot.child("ingredient").getValue(String.class);
                    String recipeMethod = dataSnapshot.child("method").getValue(String.class);
                    String recipeAdditionalNotes = dataSnapshot.child("additionalmethod").getValue(String.class);

                    // Set data to views
                    textViewTitle.setText(recipeTitle != null ? recipeTitle : "Untitled");
                    textViewTime.setText(recipeTime != null ? recipeTime : "Unknown time");
                    textViewServings.setText(recipeServings != null ? String.valueOf(recipeServings) + " Servings" : "Servings not available");
                    textViewDescription.setText(description != null && !description.isEmpty() ? description : "Description not available");
                    textViewIngredients.setText(recipeIngredients != null ? recipeIngredients : "Ingredients not available");

                    //seperate ingredients by comma and display
                    if (recipeIngredients != null) {
                        String[] ingredients = recipeIngredients.split(",");

                        StringBuilder formattedIngredients = new StringBuilder();

                        for (String step : ingredients) {
                            formattedIngredients.append(step.trim()).append("\n"); // Append each step with a newline
                        }

                        textViewIngredients.setText(formattedIngredients.toString());
                    } else {
                        textViewIngredients.setText("Ingredients are not available");
                    }

                    //seperate methods by comma and display
                    if (recipeMethod != null) {
                        String[] methodSteps = recipeMethod.split(",");

                        StringBuilder formattedMethod = new StringBuilder();

                        for (String step : methodSteps) {
                            formattedMethod.append(step.trim()).append("\n"); // Append each step with a newline
                        }

                        textViewMethod.setText(formattedMethod.toString());
                    } else {
                        textViewMethod.setText("Method not available");
                    }

                    //seperate additional notes by comma and display
                    if (recipeAdditionalNotes != null) {
                        String[] additionalNotes = recipeAdditionalNotes.split(",");

                        StringBuilder formattedAdditionalNotes = new StringBuilder();

                        for (String step : additionalNotes) {
                            formattedAdditionalNotes.append(step.trim()).append("\n"); // Append each step with a newline
                        }

                        textViewAdditionalNotes.setText(formattedAdditionalNotes.toString());
                    } else {
                        textViewAdditionalNotes.setText("Additional notes are not available");
                    }

                } else {
                    Toast.makeText(RecipeContentFromHomeActivity.this, "Recipe not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(RecipeContentFromHomeActivity.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void redirectToLogin(View view) {
        startActivity(new Intent(this, NavigationBar.class));
        finish();
    }

    public void sendRecipe(View view) {
        // Get the recipe details
        String title = textViewTitle.getText().toString();
        String time = textViewTime.getText().toString();
        String description = textViewDescription.getText().toString();
        String ingredients = textViewIngredients.getText().toString();
        String servings = textViewServings.getText().toString();
        String method = textViewMethod.getText().toString();
        String additionalNotes = textViewAdditionalNotes.getText().toString();

        // Format the recipe details into a message
        String recipeMessage = "Recipe: " + title + "\n\n" +
                "Time: " + time + "\n\n" +
                "Servings: " + servings + "\n\n" +
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
