package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TimeRecordActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_title;        // 책 제목
    ImageView imageView_img;        // 책 표지
    TextView textView_writer;       // 작가
    TextView textView_publisher;    // 출판사
    TextView textView_publish_date; // 출판일
    TextView textView_time;         // 독서 시간
    Button button_start;            // START 버튼
    Button button_stop;             // STOP 버튼

    Intent intent;

    @Override
    protected void onStart() {
        super.onStart();

       /* if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_time_record);

        // 뷰 요소 초기화
        textView_title = findViewById(R.id.title);
        imageView_img = findViewById(R.id.img);
        textView_writer = findViewById(R.id.writer);
        textView_publisher = findViewById(R.id.publisher);
        textView_publish_date = findViewById(R.id.publish_date);
        textView_time = findViewById(R.id.time);
        button_start = findViewById(R.id.btn_start);
        button_stop = findViewById(R.id.btn_stop);

    }
}
