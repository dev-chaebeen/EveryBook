package com.example.everybooks;
import java.util.Random;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.util.GmailSender;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

// 비밀번호 찾기
// 사용자에게 이메일과 닉네임을 입력받은 뒤 일치하는 정보가 있다면
// 입력받은 이메일로 임시 비밀번호를 발급해준다.
public class FindPasswordActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextInputEditText textInputEditText_nickname;
    TextInputEditText textInputEditText_email;
    Button button_send;

    String inputNickname;
    String inputEmail;
    String nickname;

    JSONObject jsonObject;

    final String TAG = "테스트";

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

        // 이메일 보내기 위한 준비
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        // send 버튼 누르면 수행할 동작
        button_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    // 사용자에게 입력받은 데이터 가져오기
                    inputNickname = textInputEditText_nickname.getText().toString();
                    inputEmail = textInputEditText_email.getText().toString();

                    // 입력받은 이메일에 해당하는 유저 정보 가져오기
                    SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
                    String userInfoString = userInfo.getString(inputEmail, "false");

                    Log.d(TAG, "입력한 이메일 : " + inputEmail + "입력한 닉네임 : " + inputNickname);
                    // 입력받은 이메일이 저장되어 있지 않다면
                    // 존재하지 않는 이메일이라고 안내해준다.
                    if(userInfoString.equals("false"))
                    {
                        Toast.makeText(getApplicationContext(), "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show();
                    }
                    // 입력받은 이메일이 저장되어 있다면 이메일을 키 값으로 저장되어있는 문자열 형태의 회원정보 값을 가져온다.
                    // 저장되어 있는 닉네임과 입력받은 닉네임을 비교해야하기 때문에 JSONObject 의 형태로 변환해서 nickname 값을 얻는다.
                    else
                    {
                        jsonObject = new JSONObject(userInfoString);
                        nickname = jsonObject.getString("nickname");

                        // 닉네임이 일치한다면 랜덤 문자열로 임시 비밀번호를 생성해서 이메일을 발송한다.
                        // 비밀번호를 임시 비밀번호로 변경한다.
                        if(nickname.equals(inputNickname))
                        {
                            // 랜덤 문자열 생성
                            Random rd = new Random();
                            StringBuffer newPassword = new StringBuffer();
                            for (int i = 0; i < 8; i++)
                            {
                                /*
                                아스키코드에서 영어 대문자는 65~90이고, 영어 소문자는 97~122 이다.
                                0~25까지의 난수에 + 65 를 하면 대문자를 얻을 수있고
                                동일한 범위의 난수에 + 97을 하면 소문자를 얻을 수 있다.
                                */

                                final int RANDOM_SMALL_LETTER = 1;
                                final int RANDOM_CAPITAL_LETTER = 2;
                                final int RANDOM_NUM = 3;

                                int rdNum = rd.nextInt(3) + 1; // 1~3 범위의 난수 반환해서 rdNum 변수에 담는다.
                                switch (rdNum)
                                {
                                    case RANDOM_SMALL_LETTER : // 랜덤 소문자 생성
                                        newPassword.append((char) (rd.nextInt(26) + 65));
                                        break;
                                    case RANDOM_CAPITAL_LETTER: // 랜덤 대문자 생성
                                        newPassword.append((char) (rd.nextInt(26) + 97));
                                        break;
                                    case RANDOM_NUM:    // 한 자리의 랜덤 숫자 생성
                                        newPassword.append(rd.nextInt(10));
                                        break;
                                }
                            }

                            try {
                                GmailSender gmailSender = new GmailSender(getString(R.string.gmailEmail), getString(R.string.gmailPassword));
                                //GMailSender.sendMail(제목, 본문내용, 받는사람);
                                gmailSender.sendMail("EveryBook 임시 비밀번호 발급", "임시 비밀번호는 '" +  newPassword + "' 입니다."
                                        , inputEmail);

                                Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();

                            } catch (SendFailedException e) {
                                Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            } catch (MessagingException e) {
                                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // 임시 비밀번호를 비밀번호로 변경한다.
                            jsonObject.put("password", newPassword);
                            SharedPreferences.Editor editor = userInfo.edit();
                            editor.putString(inputEmail, jsonObject.toString());
                            editor.commit();


                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("email", inputEmail);
                            startActivity(intent);
                            finish();

                            Log.d(TAG, "비밀번호 변경 후 회원 정보 : " + jsonObject.toString());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "닉네임이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (JSONException e){
                    System.out.println(e.toString());
                }

            }
        });
    }


}
