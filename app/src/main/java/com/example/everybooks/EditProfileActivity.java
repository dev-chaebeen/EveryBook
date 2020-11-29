package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class EditProfileActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_user_nickname;
    TextView textView_user_email;

    LinearLayout linearLayout_edit_photo;
    LinearLayout linearLayout_edit_nickname;
    LinearLayout linearLayout_withdraw;

    View.OnClickListener click;

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
        // 유저 닉네임, 이메일 정보 가져오기
    }

    void editNickname()
    {
        // 닉네임 입력받을 EditText
        final EditText editText = new EditText(this);

        // 기존 닉네임을 보여준다.
        editText.setText("기존닉네임");

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

                    // test 바꾼 닉네임 출력
                    Toast.makeText(getApplicationContext(),editText.getText().toString() ,Toast.LENGTH_SHORT).show();
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
