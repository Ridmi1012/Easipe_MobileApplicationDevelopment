package com.example.easipe_mobileapplicationdevelopment.view.navbar;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easipe_mobileapplicationdevelopment.R;
import com.example.easipe_mobileapplicationdevelopment.view.features.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

//2024-10-02  Author - Ridmi Silva StudentID - IM/2021/052

public class AddFragment extends Fragment {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ImageView recipeImg;
    private Uri imageUri;
    private EditText editTextTitle, editTextDescription, editTextServings, editTextDuration, editTextMethod, editTextIngredient, editTextAddition;
    private Button publishBtn, selectImgBtn;


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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("recipes");

        publishBtn = view.findViewById(R.id.PublishBtn);
        publishBtn.setOnClickListener(v -> publish());

        selectImgBtn.setOnClickListener(v -> openGallery());

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
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        float rate = Float.parseFloat(editTextServings.getText().toString());
        String duration = editTextDuration.getText().toString();
        String ingredient = editTextIngredient.getText().toString();
        String method = editTextMethod.getText().toString();
        String additionalMethod = editTextAddition.getText().toString();

        Recipe recipe = new Recipe(title, description, rate, duration, ingredient, method, additionalMethod, imageUrl);

        databaseReference.push().setValue(recipe);
        Toast.makeText(getContext(), "Recipe added", Toast.LENGTH_SHORT).show();
    }
}

//Finished 2024-10-02  Author - Ridmi Silva StudentID - IM/2021/052