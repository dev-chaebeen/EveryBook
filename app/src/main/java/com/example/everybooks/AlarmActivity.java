package com.example.everybooks;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    Calendar calendar;

    SwipeButton swipeButton;
    TextView timeText;
    TextView textView_alarm_text;

    MediaPlayer mediaPlayer;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        calendar = Calendar.getInstance();
        swipeButton = (SwipeButton) findViewById(R.id.swipe_btn);
        timeText = (TextView) findViewById(R.id.time);
        textView_alarm_text = findViewById(R.id.alarm_text);

        Log.d("테스트","알람 액티비티");


        // 잠금 화면 위로 activity 띄워줌
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);


        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.beep);   // 소리를 재생할 MediaPlayer
        mediaPlayer.setLooping(true);   // 무한반복
        mediaPlayer.start();

        textView_alarm_text.setText(getIntent().getStringExtra("alarmText"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag == true) {
                    try {
                        calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.HOUR_OF_DAY) > 0 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                            timeText.setText("AM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
                            timeText.setText("PM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) > 12 && calendar.get(Calendar.HOUR_OF_DAY) < 24) {
                            timeText.setText("PM " + (calendar.get(Calendar.HOUR_OF_DAY) - 12) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                            timeText.setText("AM 0시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {}
                }
            }
        }).start(); // 실시간으로 시계 출력

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                mediaPlayer.stop();
                flag=false;
                Toast.makeText(getApplicationContext(), "종료하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }); // Swipe Button 밀어서 해제
    }
}