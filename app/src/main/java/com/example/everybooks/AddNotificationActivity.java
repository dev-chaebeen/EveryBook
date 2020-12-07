package com.example.everybooks;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Notification;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AddNotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_time;
    EditText notification_text;

    int notiId;
    int hour;
    int min;
    String[] days;

    ArrayList<Notification> notiList;
    String notiListString;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences notiInfo;
    SharedPreferences.Editor editor;
    Calendar calendar;

    final String TAG = "테스트";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        // 화면 생성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_time = findViewById(R.id.time);
        notification_text = findViewById(R.id.notification_text);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:

                        // Save 클릭하면 사용자에게 입력받은 데이터를 알림 리스트에 저장한다.

                        // SharedPreferences notiInfo 에 존재하는 데이터 : notiId, notiList
                        // notiId   : 알림을 식별하기 위해 각 알림마다 부여되는 정수 데이터, 겹치지 않도록 하나의 알림이 추가되면 1 증가시켜서 sharedPreferences 에 저장한다.
                        // notiList : JsonArray 형식의 데이터. 알림 데이터를 저장한다. 저장하는 데이터 : notiId, text, hour, minute , (days)

                        notiInfo = getSharedPreferences("notiInfo", MODE_PRIVATE);
                        editor = notiInfo.edit();
                        notiId = notiInfo.getInt("notiId", 0);

                        // 사용자에게 입력받은 데이터를 Notification 객체에 저장하고
                        // JsonArray 에 추가하기 위해서 JsonObject 형식으로 변환한다.
                        Notification noti = new Notification();
                        noti.setNotiId(notiId);
                        noti.setHour(hour);
                        noti.setMinute(min);
                        noti.setText(notification_text.getText().toString());
                        jsonObject = noti.toJSON();

                        // 저장되어있는 알림 리스트를 받아온다.
                        notiListString  = notiInfo.getString("notiList", null);

                        try
                        {
                            // 저장된 알림리스트가 있다면 저장되어있는 알림 리스트를 JsonArray 형식으로 변환해서
                            // 사용자가 입력한 데이터를 담고 있는 jsonObject 를 추가한다.
                            if(notiListString != null)
                            {
                                jsonArray = new JSONArray(notiListString);
                                jsonArray.put(jsonObject);

                                notiListString = jsonArray.toString();
                                editor.putString("notiList", notiListString);
                                editor.commit();

                                //Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());

                            }
                            else
                            {
                                // 저장된 알림리스트가 없다면 JsonArray 를 생성해서
                                // 사용자가 입력한 데이터를 담고 있는 jsonObject 를 추가한다.
                                jsonArray = new JSONArray();
                                jsonArray.put(jsonObject);

                                notiListString = jsonArray.toString();
                                editor.putString("notiList", notiListString);
                                editor.commit();

                                //Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());
                            }

                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }

                        // 알림을 구분하기 위해 저장된 알림의 notiId 가 겹치지 않도록 notiInfo 에 저장된 notiId의 값을 1 증가시킨다.
                        SharedPreferences.Editor editor = notiInfo.edit();
                        editor.putInt("notiId", notiId + 1);
                        editor.commit();

                        //Log.d(TAG, "하나 추가한 뒤 notiList.size() : " + notiList.size());

                        finish();

                        break;
                    case R.id.time :
                        // 시간 클릭하면 타임피커 다이얼로그가 등장해서 설정할 시간을 입력받는다.
                        dialogTimePicker();
                }
            }
        };

        // 각 요소가 클릭되면
        textView_save.setOnClickListener(click);
        textView_time.setOnClickListener(click);

        // 초기 시간을 현재 시간으로 보여준다.
        hour = calendar.HOUR_OF_DAY;
        min = calendar.MINUTE;

        textView_time.setText(hour + ":" + min);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 현재 시간으로 초기화
        calendar = Calendar.getInstance();
    }

    private void dialogTimePicker(){
        TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textView_time.setText(hourOfDay +":"+minute);
                        hour = hourOfDay;
                        min = minute;
                    }
                };

        //현재 시간으로 타임피커 초기값을 설정한다.
        TimePickerDialog alert = new TimePickerDialog(this,
                mTimeSetListener, calendar.HOUR_OF_DAY, calendar.MINUTE, true);
        alert.show();
    }


}
