package com.example.everybooks;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class LoadingActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    LottieAnimationView lottieAnimationView_loading_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        lottieAnimationView_loading_img = findViewById(R.id.loading_img);
        lottieAnimationView_loading_img.setAnimation("bookmark.json");
        lottieAnimationView_loading_img.loop(true);
        lottieAnimationView_loading_img.playAnimation();
    }


}
