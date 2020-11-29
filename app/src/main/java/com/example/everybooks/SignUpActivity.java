package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    ImageView imageView_add_photo;
    TextInputEditText textInputEditText_nickname;
    TextInputEditText textInputEditText_email;
    TextInputEditText textInputEditText_password;
    TextInputEditText textInputEditText_confirm_password;
    Button button_register;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_signup);

        // 뷰 요소 초기화
        imageView_add_photo = findViewById(R.id.add_photo);
        textInputEditText_nickname = findViewById(R.id.nickname);
        textInputEditText_email = findViewById(R.id.email);
        textInputEditText_password = findViewById(R.id.password);
        textInputEditText_confirm_password = findViewById(R.id.confirm_password);
        button_register = findViewById(R.id.btn_register);

        // register 버튼 클릭 시 이메일 정보를 인텐트에 담아서 로그인 화면으로 이동한다
        button_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("email",textInputEditText_email.getText().toString());
                startActivity(intent);
            }
        });
    }
}
