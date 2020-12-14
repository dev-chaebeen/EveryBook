package com.example.everybooks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SetAlarmActivity  extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_time;
    EditText editText_notification_text;
    Button button_cancel;

    final String TAG = "테스트";

    int hour;
    int min;
    String time;
    String alarmText;

    SharedPreferences notiInfo;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_time = findViewById(R.id.time);
        editText_notification_text = findViewById(R.id.notification_text);
        button_cancel = findViewById(R.id.btn_cancel);

        notiInfo = getSharedPreferences("notiInfo", MODE_PRIVATE);
        editor = notiInfo.edit();

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:
                        // save 클릭하면 SharedPreferences notiInfo 에 저장
                        editor.putInt("alarmHour", hour);
                        editor.putInt("alarmMinute", min);
                        editor.putString("alarmText", editText_notification_text.getText().toString());
                        editor.commit();

                        Log.d(TAG, "저장된 알람 정보 : " + hour + ":" + min + ":" + editText_notification_text.getText().toString());
                        
                        // 알람 설정
                        setAlarm();
                                
                        finish();
                        break;

                    case R.id.time:
                        dialogTimePicker();

                }
            }
        };

        // 각 요소가 클릭되면
        textView_save.setOnClickListener(click);
        textView_time.setOnClickListener(click);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 저장되어 있던 시간과 문구로 초기화
       alarmText = notiInfo.getString("alarmText", "");
       hour = notiInfo.getInt("alarmHour", -1);
       min = notiInfo.getInt("alarmMin", -1);

        Calendar calendar = Calendar.getInstance();

        if(hour == -1 || min == -1)
        {
            // 초기 시간을 현재 시간으로 보여준다.
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);

        }

        time = "" + String.format("%02d", hour) + ":" + String.format("%02d", min) ;

        textView_time.setText(time);
        editText_notification_text.setText(alarmText);

    }

    private void dialogTimePicker(){
        TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        time = "" + String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) ;
                        textView_time.setText(time);
                        hour = hourOfDay;
                        min = minute;
                    }
                };

        //현재 시간으로 타임피커 초기값을 설정한다.
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog alert = new TimePickerDialog(this,
                mTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        alert.show();
    }

    // test
    AlarmManager alarmManager;

    void setAlarm() {

        Log.d(TAG, "알람 설정");
        // 저장되어 있던 시간을 알람 시간으로 설정
        //int hour = notiInfo.getInt("alarmHour",-1);
        //int min = notiInfo.getInt("alarmMin", -1);

        if(hour != -1 && min != -1)
        {
            // TimePickerDialog 에서 설정한 시간을 알람 시간으로 설정
            Calendar alarmCalendar = Calendar.getInstance();
            alarmCalendar.setTimeInMillis(System.currentTimeMillis());
            alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
            alarmCalendar.set(Calendar.MINUTE, min);
            alarmCalendar.set(Calendar.SECOND, 0);

            if (alarmCalendar.before(Calendar.getInstance())) alarmCalendar.add(Calendar.DATE, 1);
            // 알람 시간이 현재시간보다 빠를 때 하루 뒤로 맞춤
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            alarmIntent.setAction(AlarmReceiver.ACTION_RESTART_SERVICE);
            PendingIntent alarmCallPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
            }

            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
            }
        }
    }

    public void unsetAlarm(View view) {
        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pIntent);
    }// unregist()..


}
