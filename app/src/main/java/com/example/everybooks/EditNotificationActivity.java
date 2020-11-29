package com.example.everybooks;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditNotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_time;
    EditText editText_notification_text;
    TextView textView_save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        // 화면 구성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notification);

        // 요소 초기화
        textView_time = findViewById(R.id.time);
        editText_notification_text = findViewById(R.id.notification_text);
        textView_save = findViewById(R.id.save);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 전달받은 데이터 저장
        String time = getIntent().getStringExtra("time");
        String notification_text = getIntent().getStringExtra("notification_text");

        // 뷰 요소에 배치
        textView_time.setText(time);
        editText_notification_text.setText(notification_text);
    }
}
