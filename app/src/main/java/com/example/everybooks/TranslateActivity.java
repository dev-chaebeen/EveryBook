package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TranslateActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    ImageView imageView_add_photo;  // 이미지 추가 아이콘
    Button button_translate;        // TRANSLATE 버튼
    EditText editText_result;       // 번역 결과

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 구성
        setContentView(R.layout.activity_translate);

        // 뷰 요소 초기화
        imageView_add_photo = findViewById(R.id.add_photo);
        button_translate = findViewById(R.id.btn_translate);
        editText_result = findViewById(R.id.result);
    }
}
