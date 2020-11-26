package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditNotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_time;                 // 알림 시간
    EditText editText_notification_text;    // 알림 문구
    TextView textView_save;                 // save

    Intent intent;

    @Override
    protected void onStart() {
        super.onStart();

        /*if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        // 요소에 배치
        textView_time.setText(time);
        editText_notification_text.setText(notification_text);
    }
}
