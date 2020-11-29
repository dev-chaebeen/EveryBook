package com.example.everybooks;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddNotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_time;
    EditText notification_text;

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

        // notification List 에 추가되도록 하기
        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:
                        // Save 클릭하면 사용자에게 입력받은 데이터를 알림 리스트에 저장한다.
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

    }

    private void dialogTimePicker(){
        TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textView_time.setText(hourOfDay +":"+minute);
                    }
                };
        TimePickerDialog alert = new TimePickerDialog(this,
                mTimeSetListener, 0, 0, true);
        alert.show();
    }
}
