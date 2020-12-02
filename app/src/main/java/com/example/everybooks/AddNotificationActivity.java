package com.example.everybooks;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Notification;

import java.util.Calendar;

public class AddNotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_time;
    EditText notification_text;

    int hour;
    int min;

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
                        Notification noti = new Notification();
                        noti.setHour(hour);
                        noti.setMinute(min);
                        noti.setText(notification_text.getText().toString());

                        NotificationAdapter notificationAdapter = new NotificationAdapter();
                        notificationAdapter.addItem(noti);

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
        Calendar cal = Calendar.getInstance();
        hour = cal.HOUR_OF_DAY;
        min = cal.MINUTE;

        textView_time.setText(hour + ":" + min);

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
        Calendar cal = Calendar.getInstance();
        TimePickerDialog alert = new TimePickerDialog(this,
                mTimeSetListener, cal.HOUR_OF_DAY, cal.MINUTE, true);
        alert.show();
    }
}
