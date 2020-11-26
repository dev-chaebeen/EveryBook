package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    LinearLayout linearLayout_notification; // 알림
    TextView textView_add;                  // Add

    TextView textView_time;                 // 알림 시간
    TextView textView_notification_text;    // 알림 문구
    
    // 인텐트
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
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // test 로그아웃 풀리도록
        MainActivity.isLogin = false;

        // 요소 초기화
        linearLayout_notification = findViewById(R.id.notification);
        textView_add = (TextView) findViewById(R.id.add);

        textView_time = (TextView) findViewById(R.id.time);
        textView_notification_text = findViewById(R.id.notification_text);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.add :
                        // Add 클릭했을 때 알림 추가하는 화면으로 전환 
                        intent = new Intent(getApplicationContext(), AddNotificationActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.notification :
                        // notification 클릭했을 때 알림 시간과 알림문구 데이터를 담아서 알림 편집 화면으로 전환
                        intent = new Intent(getApplicationContext(), EditNotificationActivity.class);
                        intent.putExtra("time", textView_time.getText().toString());
                        intent.putExtra("notification_text", textView_notification_text.getText().toString());
                        startActivity(intent);
                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        textView_add.setOnClickListener(click);
        linearLayout_notification.setOnClickListener(click);


    }
}
