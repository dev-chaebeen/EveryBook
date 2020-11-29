package com.example.everybooks;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddNotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_time;
    EditText notification_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        // 화면 생성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_time = findViewById(R.id.time);
        notification_text = findViewById(R.id.notification_text);

        // notification List 에 추가되도록 하기

    }
}
