package com.example.everybooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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

        Log.d("테스트 LoadingActivity", " onCreate()");
        lottieAnimationView_loading_img = findViewById(R.id.loading_img);
        lottieAnimationView_loading_img.setAnimation("bookmark.json");
        lottieAnimationView_loading_img.playAnimation();
        startLoading();

    }

    private void startLoading(){
        Log.d("테스트 startLoading()", "시작");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                // 로그인 화면으로 전환
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3500);
    }


}
