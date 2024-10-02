package com.example.easipe_mobileapplicationdevelopment.view.features;


import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.easipe_mobileapplicationdevelopment.R;

public class RecipeContentActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    ExoPlayer player = new ExoPlayer.Builder(RecipeContentActivity.this).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);



    }
}




