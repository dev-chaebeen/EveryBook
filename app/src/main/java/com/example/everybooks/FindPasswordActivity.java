package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class FindPasswordActivity extends AppCompatActivity
{
    // 뷰 요소 구성
    TextInputEditText textInputEditText_nickname;   // 닉네임
    TextInputEditText textInputEditText_email;      // 이메일
    Button button_send;                             // send 버튼
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 구성
        setContentView(R.layout.activity_find_password);

        // 뷰 요소 초기화
        textInputEditText_nickname = findViewById(R.id.nickname);
        textInputEditText_email = findViewById(R.id.email);
        button_send = findViewById(R.id.send);

        // send 버튼 누르면 수행할 동작
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
