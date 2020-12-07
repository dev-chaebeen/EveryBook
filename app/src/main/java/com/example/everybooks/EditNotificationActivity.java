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
                        textView_time.setText(hourOfDay + ":" + minute);
                        hour = hourOfDay;
                        min = minute;
                    }
                };

        //전달받은 시간으로 타임피커 초기값을 설정한다.
        TimePickerDialog alert = new TimePickerDialog(this,
                mTimeSetListener, hour , min , true);
        alert.show();

    }
}
