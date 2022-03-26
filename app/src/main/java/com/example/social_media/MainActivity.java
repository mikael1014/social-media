package com.example.social_media;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // variables
    private static int WAIT_TIME=3000;
    Animation topAimation, bottomAnimation;
    ImageView logo;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //animation
        topAimation = AnimationUtils.loadAnimation(this, R.anim.animation_top);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);

        logo.setAnimation(topAimation);
        slogan.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ConnexionActivity.class);
                startActivity(intent);
                finish();
            }
        }, WAIT_TIME);

    }
}