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
import android.widget.VideoView;

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
    private VideoView recipeVideoView;
    private Uri imageUri,videoUri;
    private EditText editTextTitle, editTextDescription, editTextServings, editTextDuration, editTextMethod, editTextIngredient, editTextAddition;
    private Button publishBtn, selectImgBtn,selectVideoBtn, addIngredientBtn, addStepsBtn;
    private LinearLayout ingredientsContainer, methodsContainer;
    private List<EditText> ingredientFields = new ArrayList<>();
    private List<EditText> methodFields = new ArrayList<>();

    private String imageUrl = null;
    private String videoUrl = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // the inflate fragment layout
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recipeImg = view.findViewById(R.id.recipeimg);
        recipeVideoView = view.findViewById(R.id.recipeVideoView);
        selectImgBtn = view.findViewById(R.id.SelectImgBtn);
        selectVideoBtn = view.findViewById(R.id.SelectVideoBtn);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextServings = view.findViewById(R.id.editTextServings);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        editTextIngredient = view.findViewById(R.id.editTextIngredient);
        editTextMethod = view.findViewById(R.id.editTextMethod);
        editTextAddition = view.findViewById(R.id.editTextAddition);
        ingredientsContainer = view.findViewById(R.id.ingredientsContainer);
        methodsContainer = view.findViewById(R.id.methodsContainer);
        addIngredientBtn = view.findViewById(R.id.addIngredientBtn);
        addStepsBtn = view.findViewById(R.id.AddMethodBtn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("recipes");

        publishBtn = view.findViewById(R.id.PublishBtn);
        publishBtn.setOnClickListener(v -> publish());

        selectImgBtn.setOnClickListener(v -> openGallery());
        selectVideoBtn.setOnClickListener(v -> openVideoGallery());

        int widthInDp = 325;
        float scale = getContext().getResources().getDisplayMetrics().density; // Get screen density
        int widthInPx = (int) (widthInDp * scale + 0.5f);

        // Add ingredient button listener to dynamically add new EditText fields
        addIngredientBtn.setOnClickListener(v -> {

            EditText newIngredientField = new EditText(getContext());
            newIngredientField.setHint("Type Ingredient");

            // Set layout parameters for the new EditText
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthInPx,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(15, 8, 15, 0);

            newIngredientField.setLayoutParams(layoutParams);

            newIngredientField.setBackgroundResource(R.drawable.rounded_background);

            newIngredientField.setTextSize(15);

            ingredientsContainer.addView(newIngredientField);

            ingredientFields.add(newIngredientField);
        });

        addStepsBtn.setOnClickListener(v -> {
            // Create a new EditText for the new method step
            EditText newMethodField = new EditText(getContext());
            newMethodField.setHint("Type The Next Step");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    widthInPx,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(15, 8, 15, 0);
            newMethodField.setLayoutParams(layoutParams);

            newMethodField.setBackgroundResource(R.drawable.rounded_background);

            newMethodField.setTextSize(15);

            methodsContainer.addView(newMethodField);

            methodFields.add(newMethodField);
        });

        return view;
    }

    private void openVideoGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        videoLauncher.launch(intent);
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
                    recipeImg.setImageURI(imageUri);
                }
            }
    );


    private final ActivityResultLauncher<Intent> videoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    videoUri = result.getData().getData();
                    recipeVideoView.setVideoURI(videoUri); // Display the selected video in VideoView
                    recipeVideoView.setVisibility(View.VISIBLE); // Make the VideoView visible
                    recipeVideoView.start(); // Start the video playback
                }
            }
    );

    private void publish() {
        // Reset imageUrl and videoUrl before uploading
        imageUrl = null;
        videoUrl = null;

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String servings = editTextServings.getText().toString();
        String duration = editTextDuration.getText().toString();

        // Validate required fields
        if (title.isEmpty() || description.isEmpty() || duration.isEmpty() || servings.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

//        int servings;
//        try {
//            servings = Integer.parseInt(editTextServings.getText().toString());
//        } catch (NumberFormatException e) {
//            Toast.makeText(getContext(), "Please enter a valid number for servings", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Validate image selection
        if (imageUri != null) {
            uploadImage();
        } else {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if a video is selected or not
        if (videoUri != null) {
            uploadVideo();
        } else {
            videoUrl = ""; // No video selected, so set the URL to an empty string
            checkUploadsComplete(); // Proceed to check uploads (only image in this case)
        }
    }

    // Method to clear the fields after publishing
    private void clearFields() {
        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextServings.setText("");
        editTextDuration.setText("");
        editTextIngredient.setText("");
        editTextMethod.setText("");
        editTextAddition.setText("");


        // Clear dynamically added ingredients and steps
        ingredientsContainer.removeAllViews();
        methodsContainer.removeAllViews();

        // Clear stored URIs
        imageUri = null;
        videoUri = null;
    }


    private void uploadVideo() {
        String videoName = UUID.randomUUID().toString() + ".mp4";
        StorageReference videoRef = storageReference.child("videos/" + videoName);

        videoRef.putFile(videoUri)
                .addOnSuccessListener(taskSnapshot -> videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    videoUrl = uri.toString();
                    checkUploadsComplete();  // Check if both image and video URLs are available
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Video upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void uploadImage() {
        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageReference.child("images/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    checkUploadsComplete();  // Check if both image and video URLs are available
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void checkUploadsComplete() {
        // Check if both imageUrl and videoUrl are not null
        if (imageUrl != null && (videoUri == null || videoUrl != null)) {
            addRecipe(imageUrl, videoUrl);

            clearFields();

        }
    }

    private void addRecipe(String imageUrl, String videoUrl) {
        String userId;
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String serving = editTextServings.getText().toString();
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

        Recipe recipe = new Recipe(userId ,title, description, serving, duration, ingredientsList.toString(), methodList.toString(), additionalMethod, imageUrl,videoUrl, Status,recipeId);

        databaseReference.push().setValue(recipe);
        Toast.makeText(getContext(), "Recipe added", Toast.LENGTH_SHORT).show();
    }
}
