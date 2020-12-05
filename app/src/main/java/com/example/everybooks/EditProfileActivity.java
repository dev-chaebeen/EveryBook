package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_user_nickname;
    TextView textView_user_email;

    LinearLayout linearLayout_edit_photo;
    LinearLayout linearLayout_edit_nickname;
    LinearLayout linearLayout_withdraw;

    View.OnClickListener click;

    String nickname;

    String loginUser;
    String[] emailPassword;
    String loginEmail;
    String userInfoString;
    JSONObject jsonObject;

    final String TAG = "테스트";

    SharedPreferences userInfo;
    SharedPreferences autoLogin;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 화면 구성
        setContentView(R.layout.activity_edit_profile);

        // 뷰 요소 초기화
        textView_user_nickname = findViewById(R.id.user_nickname);
        textView_user_email = findViewById(R.id.user_email);
        linearLayout_edit_photo = findViewById(R.id.edit_photo);
        linearLayout_edit_nickname = findViewById(R.id.edit_nickname);
        linearLayout_withdraw = findViewById(R.id.withdraw);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.edit_photo :
                        // edit_photo 클릭했을 때 수행할 동작

                        break;

                    case R.id.edit_nickname :
                        // edit_nickname 클릭했을 때 수행할 동작
                        editNickname();

                        break;

                    case R.id.withdraw :
                        // withdraw 클릭했을 때 수행할 동작
                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        linearLayout_edit_photo.setOnClickListener(click);
        linearLayout_edit_nickname.setOnClickListener(click);
        linearLayout_withdraw.setOnClickListener(click);

    }// onCreate()

    @Override
    protected void onResume()
    {
        super.onResume();

        // 현재 로그인 되어 있는 이메일 정보를 가져온다.
        // email,password 의 문자열로 저장되어있으므로 , 을 기준으로 문자열을 나눠서 문자열 배열에 저장한다.
        // 배열의 0번째 칸에 email ,  1번째 칸에 password 정보가 담긴다.
        autoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE);
        loginUser = autoLogin.getString("loginUser", null);
        emailPassword = loginUser.split(",");
        loginEmail = emailPassword[0];

        // 해당 이메일을 키로 저장되어있는 유저의 정보를 가져온다.
        userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        userInfoString = userInfo.getString(loginEmail, null);

        try
        {
            // 기존의 닉네임과 이메일을 가져온다.
            jsonObject = new JSONObject(userInfoString);
            //img
            nickname = jsonObject.getString("nickname");
            loginEmail = jsonObject.getString("email");

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        // img, 유저 닉네임, 이메일 정보 가져오기
        //img
        textView_user_nickname.setText(nickname);
        textView_user_email.setText(loginEmail);


    }

    void editNickname()
    {
        // 닉네임 입력받을 EditText
        final EditText editText = new EditText(this);

        // 기존 닉네임을 보여준다.
        editText.setText(nickname);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("닉네임 변경");                    // 다이얼로그 제목
        builder.setMessage("변경할 닉네임을 입력해주세요.");  // 다이얼로그 내용
        builder.setView(editText);
        builder.setPositiveButton("입력",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    // 입력 클릭했을 때
                    // 변경한 닉네임 저장

                    try
                    {
                        SharedPreferences.Editor editor = userInfo.edit();
                        jsonObject.put("nickname", editText.getText().toString());
                        editor.putString(loginEmail, jsonObject.toString());
                        editor.commit();

                        Log.d(TAG, " 닉네임 바꾼 뒤 유저 정보 : " + jsonObject.toString());
                    }
                    catch (Exception e)
                    {
                           System.out.println(e.toString());
                    }
                    // test 바꾼 닉네임 출력
                    Toast.makeText(getApplicationContext(), "닉네임이 변경되었습니다." ,Toast.LENGTH_SHORT).show();

                    // 변경된 닉네임을 반영하기 위해 액티비티 다시 호출
                    intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                    startActivity(intent);

                    finish();
                }
            });
        builder.setNegativeButton("취소",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    // 취소 클릭했을 때
                    Toast.makeText( getApplicationContext(), "변경 취소" ,Toast.LENGTH_SHORT).show();
                }
            });

        builder.show();
    }



}
