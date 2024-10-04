// 2024-10-02  Author - Ridmi Silva StudentID - IM/2021/052

package com.example.easipe_mobileapplicationdevelopment.view.navbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddFragment extends Fragment {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ImageView recipeImg;
    private Uri imageUri;
    private EditText editTextTitle, editTextDescription, editTextServings, editTextDuration, editTextMethod, editTextIngredient, editTextAddition;
    private Button publishBtn, selectImgBtn, addIngredientBtn, addStepsBtn;
    private LinearLayout ingredientsContainer, methodsContainer; // Declare methodsContainer
    private List<EditText> ingredientFields = new ArrayList<>(); // List to store dynamically added ingredient fields
    private List<EditText> methodFields = new ArrayList<>(); // List to store dynamically added method fields

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recipeImg = view.findViewById(R.id.recipeimg);
        selectImgBtn = view.findViewById(R.id.SelectImgBtn);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextServings = view.findViewById(R.id.editTextServings);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        editTextIngredient = view.findViewById(R.id.editTextIngredient);
        editTextMethod = view.findViewById(R.id.editTextMethod);
        editTextAddition = view.findViewById(R.id.editTextAddition);
        ingredientsContainer = view.findViewById(R.id.ingredientsContainer);
        methodsContainer = view.findViewById(R.id.methodsContainer); // Initialize methodsContainer
        addIngredientBtn = view.findViewById(R.id.addIngredientBtn);
        addStepsBtn = view.findViewById(R.id.AddMethodBtn); // Button to add more ingredients

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("recipes");

        publishBtn = view.findViewById(R.id.PublishBtn);
        publishBtn.setOnClickListener(v -> publish());

        selectImgBtn.setOnClickListener(v -> openGallery());

        int widthInDp = 325; // width in dp
        float scale = getContext().getResources().getDisplayMetrics().density; // Get screen density
        int widthInPx = (int) (widthInDp * scale + 0.5f);

        // Add ingredient button listener to dynamically add new EditText fields
        addIngredientBtn.setOnClickListener(v -> {
            // Create a new EditText for the new ingredient
            EditText newIngredientField = new EditText(getContext());
            newIngredientField.setHint("Type Ingredient");

            // Set layout parameters for the new EditText
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthInPx, // width in pixels
                    LinearLayout.LayoutParams.WRAP_CONTENT // height
            );
            layoutParams.setMargins(15, 8, 15, 0); // Add margins as per your design

            newIngredientField.setLayoutParams(layoutParams);

            // Apply the custom background (rounded corners, padding, and border) from the drawable XML
            newIngredientField.setBackgroundResource(R.drawable.rounded_background);

            // Optionally set text size and other properties
            newIngredientField.setTextSize(15);

            // Add the new EditText to the container (LinearLayout)
            ingredientsContainer.addView(newIngredientField);

            // Add the new EditText to the list of ingredient fields if you want to track them
            ingredientFields.add(newIngredientField);
        });

        addStepsBtn.setOnClickListener(v -> {
            // Create a new EditText for the new method step
            EditText newMethodField = new EditText(getContext());
            newMethodField.setHint("Type The Next Step");

            // Set layout parameters for the new EditText
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthInPx, // width in pixels
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

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    recipeImg.setImageURI(imageUri); // Display the selected image
                }
            }
    );

    private void publish() {
        if (imageUri != null) {
            uploadImage();
        } else {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        // Create a unique filename for the image
        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageReference.child("images/" + imageName);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    addRecipe(imageUrl); // Pass the URL to save in the database
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void addRecipe(String imageUrl) {
        String userId;
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        float rate = Float.parseFloat(editTextServings.getText().toString());
        String duration = editTextDuration.getText().toString();
        String ingredient = editTextIngredient.getText().toString();
        String Methods = editTextMethod.getText().toString();
        String additionalMethod = editTextAddition.getText().toString();
        boolean Status = false;
        String recipeId = UUID.randomUUID().toString();  ;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Log.e("Firebase", "User is not authenticated");
            return;
        }


        // Collect all dynamically added ingredients
        StringBuilder ingredientsList = new StringBuilder(ingredient);
        for (EditText ingredientField : ingredientFields) {
            String additionalIngredient = ingredientField.getText().toString();
            if (!additionalIngredient.isEmpty()) {
                ingredientsList.append(", ").append(additionalIngredient);
            }
        }

        // Collect all dynamically added method steps
        StringBuilder methodList = new StringBuilder(Methods);
        for (EditText methodField : methodFields) {
            String step = methodField.getText().toString();
            if (!step.isEmpty()) {
                methodList.append(", ").append(step);
            }
        }

        Recipe recipe = new Recipe(userId ,title, description, rate, duration, ingredientsList.toString(), methodList.toString(), additionalMethod, imageUrl, Status, recipeId);

        databaseReference.push().setValue(recipe);
        Toast.makeText(getContext(), "Recipe added", Toast.LENGTH_SHORT).show();
    }
}
