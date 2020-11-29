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
    TextView textView_title;
    ImageView imageView_img;
    TextView textView_writer;
    TextView textView_publisher;
    TextView textView_publish_date;
    TextView textView_time;
    Button button_start;
    Button button_stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
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
