package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddNotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;     // save 
    TextView textView_time;     // 알림시간 
    EditText notification_text; // 알림문구

    Intent intent;

    @Override
    protected void onStart() {
        super.onStart();

     /*   if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

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

    }
}
