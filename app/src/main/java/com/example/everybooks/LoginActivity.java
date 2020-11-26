package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;

    // 뷰 요소 선언
    TextInputEditText textInputEditText_email;      // 이메일
    TextInputEditText textInputEditText_password;   // 비밀번호
    Button button_login;                            // 로그인 버튼
    Button button_google_login;                     // 구글로그인 버튼
    TextView textView_find_password;                // find password
    TextView textView_sign_up;                      // sign up


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면생성
        setContentView(R.layout.activity_login);

        // 뷰 요소 초기화
        textInputEditText_email = findViewById(R.id.email);
        textInputEditText_password =findViewById(R.id.password);
        button_login = findViewById(R.id.login);
        button_google_login = findViewById(R.id.google_login);
        textView_find_password = findViewById(R.id.find_password);
        textView_sign_up = findViewById(R.id.sign_up);

        // 각 요소를 클릭하면 수행할 동작 지정
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.find_password:
                            // find password 를 클릭하면 비밀번호 찾기 화면으로 전환
                            intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                            startActivity(intent);
                        break;

                    case R.id.sign_up:
                            // sign_up 을 클릭하면 회원가입 화면으로 전환
                            intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(intent);
                        break;

                    case R.id.login:
                        // 로그인 버튼 클릭하면 메인 화면으로 이동
                        // 임시 test
                        MainActivity.isLogin = true;
                        Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        finish();// 현재 액티비티 종료

                        break;

                    case R.id.google_login:
                        // 구글 로그인 버튼 클릭하면
                        break;


                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        textView_find_password.setOnClickListener(click);
        textView_sign_up.setOnClickListener(click);
        button_login.setOnClickListener(click);
        button_google_login.setOnClickListener(click);



    }// end onCreate()

    @Override
    protected void onResume() {
        super.onResume();

        // 인텐트에서 메일 데이터를 받아서 로그인 이메일 작성 창에 보여준다.
        String email = getIntent().getStringExtra("email");
        textInputEditText_email.setText(email);

    }
}
