package com.example.everybooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;

    // 뷰 요소 선언
    TextInputEditText textInputEditText_email;
    TextInputEditText textInputEditText_password;
    Button button_login;
    Button button_google_login;
    TextView textView_find_password;
    TextView textView_sign_up;

    String loginEmail;
    String loginPassword;
    String loginUser;

    SharedPreferences autoLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 로딩화면
     /*   intent = new Intent(getApplicationContext(), LoadingActivity.class);
        startActivity(intent);*/

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
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.find_password:
                            // find password 를 클릭하면 비밀번호 찾기 화면으로 전환한다.
                            intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                            startActivity(intent);
                        break;

                    case R.id.sign_up:
                            // sign_up 을 클릭하면 회원가입 화면으로 전환한다.
                            intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(intent);
                        break;

                    case R.id.login:

                        // ! 거시적 → 미시적으로 설명해야한다.

                        // 로그인 기능 동작
                        // 1. 사용자에게 이메일과 패스워드를 입력받는다.
                        // 2. 입력받은 이메일이 저장된 회원정보에 존재하는지 확인한다.
                        // 3. 등록된 이메일이라면 이메일을 키값으로 저장되어있는 비밀번호와 사용자에게 입력받은 비밀번호가 일치하는지 확인한다.
                        // 4. 일치한다면 로그인 성공, 자동로그인 값으로 입력받은 이메일과 비밀번호를 저장한다.

                        // userInfo 파일을 불러와서 입력받은 이메일이 존재하는지 확인한다.
                        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
                        String userInfoString = userInfo.getString(textInputEditText_email.getText().toString(), "false");

                        //  입력받은 이메일이 존재하지 않는다면 안내 메세지로 알려준다.
                        if(userInfoString.equals("false"))
                        {
                            Toast.makeText(getApplicationContext(), "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            try {

                                // 입력받은 이메일이 존재한다면 해당하는 패스워드 정보를 가져온다.
                                JSONObject jsonObject = new JSONObject(userInfoString);
                                String password = jsonObject.getString("password");

                                // 입력받은 패스워드와 저장되어있는 패스워드가 일치하면 로그인 시킨 뒤
                                // 자동로그인이 가능하도록 autoLogin 파일에 입력받은 이메일과 패스워드를 저장하고 메인 화면으로 전환한다.
                                if(textInputEditText_password.getText().toString().equals(password))
                                {
                                    MainActivity.isLogin = true;
                                    SharedPreferences autoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = autoLogin.edit();
                                    editor.putString("loginUser", textInputEditText_email.getText().toString() +","+ textInputEditText_password.getText().toString());
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
        button_google_login.setOnClickListener(click);
        button_login.setOnClickListener(click);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 인텐트에서 이메일 데이터를 받아서 로그인 이메일 작성 창에 보여준다.
        String email = getIntent().getStringExtra("email");
        textInputEditText_email.setText(email);

        // 최초 로그인 이후로는 자동로그인을 하기 위해서 autoLogin 파일에 저장해둔 loginUser 값을 받아온다.
        SharedPreferences autoLogin = getSharedPreferences("autoLogin",MODE_PRIVATE);
        loginUser = autoLogin.getString("loginUser", null);

        // SharedPreference autoLogin 에 저장된 loginUser 가 null 값이 아니라면
        if(loginUser != null)
        {
            // "email, password" 의 형태로 저장했기 때문에 "," 을 기준으로 문자열을 나눠서 String 배열에 값을 담는다.
            // loginEmailPassword[0] 에 email , loginEmailPassword[1]에 password 값이 담긴다.
            String[] loginEmailPassword = loginUser.split(",");

            // autoLogin 에 저장되어있는 이메일 값이 userInfo 에 존재하는지 확인한다.
            SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
            String userInfoString = userInfo.getString(loginEmailPassword[0], "false");

            // 존재하지 않는다면
            if(userInfoString.equals("false"))
            {
                // 로그인 화면으로 전환한다.
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                try
                {
                    // autoLogin 에 저장되어있는 이메일 값이 userInfo 에 존재한다면 해당하는 패스워드 정보를 가져온다.
                    JSONObject jsonObject = new JSONObject(userInfoString);
                    String password = jsonObject.getString("password");

                    // autoLogin 에 저장되어있는 패스워드와 userInfo 에 저장되어있는 패스워드가 일치하면 로그인 시킨다.
                    if(loginEmailPassword[1].equals(password))
                    {
                        MainActivity.isLogin = true;
                        Toast.makeText(LoginActivity.this, "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        // 일치하지 않는다면 로그인 화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
