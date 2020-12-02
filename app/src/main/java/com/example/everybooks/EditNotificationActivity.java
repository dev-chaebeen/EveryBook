package com.example.everybooks;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Notification;

public class EditNotificationActivity extends AppCompatActivity
{
    Intent intent;

    // 뷰 요소 선언
    TextView textView_time;
    EditText editText_notification_text;
    TextView textView_save;

    // 인텐트로 전달받는 데이터 선언
    int position;
    int hour;
    int min;
    String text;

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
        position = getIntent().getIntExtra("position", -1);
        hour = getIntent().getIntExtra("hour", -1);
        min = getIntent().getIntExtra("minute", -1);
        text = getIntent().getStringExtra("text");


        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:
                        // save 클릭하면 알림 데이터를 수정한다.
                        Notification noti = NotificationAdapter.notiList.get(position);
                        noti.setText(editText_notification_text.getText().toString());
                        noti.setHour(hour);
                        noti.setMinute(min);

                        NotificationAdapter adapter = new NotificationAdapter();
                        adapter.notifyDataSetChanged();

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

        // 뷰 요소에 배치
        textView_time.setText(hour + ":" + min);
        editText_notification_text.setText(text);
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

        //전달받은 시간으로 타임피커 초기값을 설정한다.
        TimePickerDialog alert = new TimePickerDialog(this,
                mTimeSetListener, hour, min , true);
        alert.show();
    }
}
