package com.example.everybooks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.everybooks.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeRecordService extends Service
{
    // 뷰 요소 선언
    TextView textView_title;
    ImageView imageView_img;
    TextView textView_writer;
    TextView textView_publisher;
    TextView textView_publish_date;
    TextView textView_time;
    Button button_start;
    Button button_stop;
    CircleImageView circleImageView_book_gif;

    int bookId;
    String title;
    String img;
    String writer;
    String publisher;
    String publishDate;
    String readTime;

    final String TAG = "테스트";

    int hour;
    int minute;
    int second;
    boolean isStart;

    int timeInSeconds;
    Thread timeThread;
    Thread aniThread;

    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.d(TAG, "TimeRecordService, onCreate()");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "TimeRecordService, onDestroy");

        // Stop
        // 스레드가 생성되지 않은 상황에서 stop 버튼 눌러도 동작하지 않도록 하기 위해서
        // thread 가 null 이 아닐 때만 코드 동작하도록 한다.
        if (timeThread != null) {
            // stop 버튼 클릭하면 1초씩 증가하는 스레드를 멈추고 현재 독서 시간을 저장한다.
            timeThread.interrupt();
            aniThread.interrupt();

            // 다시 start 버튼을 클릭할 수 있도록 isStart 값을 true 로 바꾼다.
            isStart = true;

            SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
            String bookListString = bookInfo.getString("bookList", null);

            try {
                JSONArray jsonArray = new JSONArray(bookListString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (bookId == jsonObject.getInt("bookId")) {
                        jsonObject.put("readTime", readTime);
                        Log.d(TAG, "TimeRecordActivity, onDestroy 후 저장될 jsonObject : " + jsonObject.getString("readTime"));
                    }
                }

                SharedPreferences.Editor editor = bookInfo.edit();
                editor.putString("bookList", jsonArray.toString());
                editor.commit();

            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }

        this.stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "TimeRecordService, onStartCommand");


        // Start

        // 문자열의 형태로 저장되어있는 readTime 의 형식을 시, 분, 초로 나눈다.
        // 스레드를 사용해 1초당 1씩 초가 증가하도록 한다.
        String[] hourMinuteSecond = readTime.split(":");
        hour = Integer.parseInt(hourMinuteSecond[0]);
        minute = Integer.parseInt(hourMinuteSecond[1]);
        second = Integer.parseInt(hourMinuteSecond[2]);

        Log.d(TAG, "TimeRecordService, readTime 에서 시분초 int로 가져오기 : " + hour + minute + second);

        Log.d(TAG, "TimeRecordActivity, 스레드 실행 전 isStart : " + isStart);

        // 초 단위로 바꾸기
        timeInSeconds = hour * 60 * 60 + minute * 60 + second;

        Log.d(TAG, " 저장되어있던 초단위 시간 : " + timeInSeconds);

        // 1초 씩 증가하는 스레드
        TimeRecordThread timeRecordThread = new TimeRecordThread();
        timeThread = new Thread(timeRecordThread);
        timeThread.start();

        Log.d(TAG, "TimeRecordActivity, 스레드 실행 후 isStart : " + isStart);

        // 책장 넘어가는 스레드 실행
        GifThread gifThread = new GifThread();
        aniThread = new Thread(gifThread);
        aniThread.start();

        isStart = false;



        return super.onStartCommand(intent, flags, startId);
    }

    // 타임핸들러 클래스
    Handler timeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            timeInSeconds = msg.arg1;

            // 여기서 HH:MM:SS 형식으로 바꿔서 보여준다.

            Log.d(TAG, "TimeRecordActivity, 형식 바꿀 때 시간 " + hour + ":" + minute + ":" + second);

            int secs = (int) (timeInSeconds/ 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int hours = mins / 60;
            mins = mins % 60;
            readTime = "" + String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);

            textView_time.setText(readTime);
        }
    };


    // 이미지 핸들러
    Handler gifHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            updateThread();
        }
    };



        // 타임 스레드 클래스 생성
        class TimeRecordThread implements Runnable {

            boolean running = false;

            public void run() {
                running = true;
                while (running) {
                    timeInSeconds += 1;
                    Message message = timeHandler.obtainMessage();
                    message.arg1 =  timeInSeconds;
                    timeHandler.sendMessage(message);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }

        // 이미지 스레드 클래스 생성
        class GifThread implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        gifHandler.sendMessage(gifHandler.obtainMessage());
                        Thread.sleep(150);

                    } catch (Throwable t) {
                        System.out.println(t.toString());
                        return;
                    }
                }
            }
        }

        private int i = 0;

        private void updateThread()
        {
            int mod = i % 9;

            switch (mod) {
                case 0:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_1);
                    break;

                case 1:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_2);
                    break;

                case 2:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_3);
                    break;

                case 3:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_4);
                    break;

                case 4:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_5);
                    break;
                case 5:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_6);
                    break;

                case 6:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_7);
                    break;
                case 7:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_8);
                    break;
                case 8:
                    i++;
                    circleImageView_book_gif.setImageResource(R.drawable.book_gif_9);
                    break;
            }
        }


}
