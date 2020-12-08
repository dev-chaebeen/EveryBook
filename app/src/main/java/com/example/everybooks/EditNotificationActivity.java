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

public class EditNotificationActivity extends AppCompatActivity
{
    Intent intent;
    NotificationAdapter notificationAdapter;

    // 뷰 요소 선언
    TextView textView_time;
    EditText editText_notification_text;
    TextView textView_save;

    int notiId;
    int hour;
    int min;
    String text;
    // days

    final String TAG = "테스트";

    SharedPreferences notiInfo;
    String notiListString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 화면 구성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notification);

        // 요소 초기화
        textView_time = findViewById(R.id.time);
        editText_notification_text = findViewById(R.id.notification_text);
        textView_save = findViewById(R.id.save);

        // 인텐트로 전달받은 데이터 담기
        notiId = getIntent().getIntExtra("notiId", -1);
        Log.d(TAG, "EditNotificationActivity, 인텐트로 받은 notiId : " + notiId);
        notificationAdapter = new NotificationAdapter(getApplicationContext());

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:

                        // save 를 클릭하면 전달받은 notiId 에 해당하는 알림 객체를 찾아서 알림 내용을 변경해준다.

                        // notiInfo 파일에 문자열로 저장되어있는 알림리스트를 불러온다.
                        // jsonObject 형태로 접근하기 위해서 문자열을 JsonArray 의 형태로 바꿔준다.
                        // 전달받은 notiId 와 동일한 notiId 를 가지고 있는 jsonObject 의 값을 입력받은 값으로 바꿔준다.
                        // jsonArray 를 문자열의 형태로 바꿔서 "notiList" 키의 값으로 저장한다.
                        notiInfo = getSharedPreferences("notiInfo", MODE_PRIVATE);
                        notiListString = notiInfo.getString("notiList", null);

                        try
                        {
                            JSONArray jsonArray = new JSONArray(notiListString);
                            for (int i = 0; i < jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if(jsonObject.getInt("notiId") == notiId)
                                {
                                    jsonObject.put("text", editText_notification_text.getText().toString());
                                    jsonObject.put("hour", hour);
                                    jsonObject.put("minute", min);
                                    // days
                                }
                            }

                            // test ok
                            Log.d(TAG," 알림 수정 후 jsonArray.toString: " + jsonArray.toString());

                            SharedPreferences.Editor editor = notiInfo.edit();
                            editor.putString("notiList", jsonArray.toString());
                            editor.commit();

                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }

                        finish();
                        break;

                    case R.id.time :

                        // 시간을 클릭하면 시간을 입력받는 다이얼로그가 나타난다.
                        dialogTimePicker();
                }
            }
        };

        // 각 요소가 클릭되면
        textView_save.setOnClickListener(click);
        textView_time.setOnClickListener(click);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 전달받은 notiId 와 동일한 값을 가진 알림 정보를 보여준다.

        // notiInfo 파일에 문자열로 저장되어있는 알림리스트를 불러온다.
        // jsonObject 형태로 접근하기 위해서 문자열을 JsonArray 의 형태로 바꿔준다.
        // 전달받은 notiId 와 동일한 notiId 를 가지고 있는 jsonObject 의 값을 뷰 요소에 배치해서 보여준다.
        notiInfo = getSharedPreferences("notiInfo", MODE_PRIVATE);
        notiListString = notiInfo.getString("notiList", null);

        try
        {
            JSONArray jsonArray = new JSONArray(notiListString);
            for (int i = 0; i < jsonArray.length() ; i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                if(jsonObject.getInt("notiId") == notiId)
                {
                    Log.d(TAG, "EditNotificationActivity, 해당하는 알림 jsonObject : " + jsonObject.toString());
                    text = jsonObject.getString("text");
                    hour = jsonObject.getInt("hour");
                    min = jsonObject.getInt("minute");
                    // days
                    textView_time.setText(hour + ":" + min);
                    editText_notification_text.setText(text);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        Log.d(TAG, "EditNotification, 해당하는 알림의 시간 :" + hour +":"+ min );
    }

    private void dialogTimePicker()
    {
        TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        // 타임피커로 시간을 설정하면 뷰 요소에 설정된 시간을 보여주고
                        // 변수에 설정된 값을 담는다.
                        textView_time.setText(hourOfDay + ":" + minute);
                        hour = hourOfDay;
                        min = minute;
                    }
                };

        // 알림에 저장되어있던 시간으로 타임피커 초기값을 설정한다.
        TimePickerDialog alert = new TimePickerDialog(this,
                mTimeSetListener, hour , min , true);
        alert.show();

    }
}
