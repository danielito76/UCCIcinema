package com.example.cinemaprovafragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    Animation animation;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        image = findViewById(R.id.imageViewsplash);
        image.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}