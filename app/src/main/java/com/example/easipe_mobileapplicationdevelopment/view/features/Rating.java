package com.example.easipe_mobileapplicationdevelopment.view.features;

import android.util.Log;
import android.widget.RatingBar;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rating {

    // Method to calculate average rating and set it on the RatingBar
    public static void calculateAverageRating(String recipeId, RatingBar ratingBar) {
        DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId).child("ratings");

        ratingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float totalRating = 0;
                int ratingCount = 0;

                // Iterate through ratings to calculate total and count
                for (DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                    Float rating = ratingSnapshot.getValue(Float.class);
                    if (rating != null) {
                        totalRating += rating;
                        ratingCount++;
                    }
                }

                // Calculate and set the average rating
                float averageRating = (ratingCount > 0) ? (totalRating / ratingCount) : 0;
                ratingBar.setRating(averageRating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Rating", "Failed to read ratings", databaseError.toException());
            }
        });
    }
}
