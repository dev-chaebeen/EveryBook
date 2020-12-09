package com.example.everybooks;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.everybooks.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeRecordActivity extends AppCompatActivity
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

    private static Handler timeHandler;
    Thread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_time_record);

        // 뷰 요소 초기화
        textView_title = findViewById(R.id.title);
        imageView_img = findViewById(R.id.img);
        textView_writer = findViewById(R.id.writer);
        textView_publisher = findViewById(R.id.publisher);
        textView_publish_date = findViewById(R.id.publish_date);
        textView_time = findViewById(R.id.time);
        button_start = findViewById(R.id.btn_start);
        button_stop = findViewById(R.id.btn_stop);

        // 인텐트로 전달받은 bookId 데이터를 담는다
        // bookId 는 각각의 책을 구분하기 위해서 책이 가지고 있는 고유한 정수값이다.
        bookId = getIntent().getIntExtra("bookId", -1);

        // 한번 start 버튼을 클릭한 상태에서는 다시 클릭해도 독서시간을 1초 씩 증가시키는 스레드를 새로 생성하지 않도록 하기 위해서
        // boolean 변수에 start 버튼 클릭 가능 여부를 저장한다.
        isStart = true;

        // 인텐트로 전달받은 bookId 의 책 정보를 화면에서 보여주기 위해서
        // 저장되어있는 책 리스트를 불러오고 인텐트로 전달받은 bookId 와 동일한 bookId 를 가지고 있는 책을 찾아 정보를 가져온다.
        // 가져오는 정보는 책 제목, 표지, 작가, 출판사, 출판일, 독서 시간이다.
        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
        String bookListString = bookInfo.getString("bookList", null);

        try
        {
            JSONArray jsonArray = new JSONArray(bookListString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (bookId == jsonObject.getInt("bookId"))
                {
                    title = jsonObject.getString("title");
                    img = jsonObject.getString("img");
                    writer = jsonObject.getString("writer");
                    publisher = jsonObject.getString("publisher");
                    publishDate = jsonObject.getString("publishDate");
                    readTime = jsonObject.getString("readTime");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_start:
                        // start 버튼을 클릭하면 저장되어있는 독서시간이 1초씩 증가한다.
                        // 독서시간은 사용자가 책을 읽을 때 걸리는 시간을 기록해둔 값이다.

                        // 문자열의 형태로 저장되어있는 readTime 의 형식을 시, 분, 초로 나눈다.
                        // 24시간이 넘어도 일 단위로 증가시키지 않기 위해서 Calendar 사용하지 않음??
                        // 스레드를 사용해 1초당 1씩 초가 증가하도록 하고
                        // 60초가 채워지면 분 + 1 , 60분이 채워지면 시 + 1 한다.

                        if(isStart)
                        {
                            String[] hourMinuteSecond = readTime.split(":");
                            hour = Integer.parseInt(hourMinuteSecond[0]);
                            minute = Integer.parseInt(hourMinuteSecond[1]);
                            second = Integer.parseInt(hourMinuteSecond[2]);

                            Log.d(TAG, "TimeRecordActivity, readTime 에서 시분초 int로 가져오기 : " + hour + minute + second);

                            // 스레드 클래스 생성
                            class TimeRecordThread implements Runnable {

                                boolean running = false;
                                public void run()
                                {
                                    running = true;
                                    while(running)
                                    {
                                        second += 1;
                                        Message message = timeHandler.obtainMessage();
                                        message.arg1 = second;
                                        timeHandler.sendMessage(message);

                                        try
                                        {
                                            Thread.sleep(1000);
                                        }
                                        catch (Exception e)
                                        {
                                            return;
                                        }
                                    }
                                }
                            }

                            Log.d(TAG, "TimeRecordActivity, 스레드 실행 전 isStart : " + isStart);

                            TimeRecordThread timeRecordThread = new TimeRecordThread();
                            thread = new Thread(timeRecordThread);
                            thread.start();
                            isStart = false;
                            Log.d(TAG, "TimeRecordActivity, 스레드 실행 후 isStart : " + isStart);

                        }

                        break;

                    case R.id.btn_stop:

                        // stop 버튼 클릭하면 1초씩 증가하는 스레드를 멈추고 현재 독서 시간을 저장한다.
                        thread.interrupt();

                        // 다시 start 버튼을 클릭할 수 있도록 isStart 값을 true 로 바꾼다.
                        isStart = true;

                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                        String bookListString = bookInfo.getString("bookList", null);

                        try
                        {
                            JSONArray jsonArray = new JSONArray(bookListString);
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if (bookId == jsonObject.getInt("bookId"))
                                {
                                   jsonObject.put("readTime", readTime);
                                    Log.d(TAG, "TimeRecordActivity, stop 버튼 클릭 후 변경한 jsonObject : " + jsonObject.toString());
                                }
                            }

                            SharedPreferences.Editor editor = bookInfo.edit();
                            editor.putString("bookList", jsonArray.toString());
                            editor.commit();



                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }

                        break;
                }
            }
        };

        // 각 요소가 클릭되면
        button_start.setOnClickListener(click);
        button_stop.setOnClickListener(click);

        // 핸들러 클래스
        timeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                second = msg.arg1;

                // 여기서 HH:MM:SS 형식으로 바꿔서 보여준다.

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);

                Log.d(TAG, "TimeRecordActivity, 형식 바꿀 때 시간 " + hour + ":" + minute + ":" + second);

                Util util = new Util();
                readTime= util.stringFromCalendar(calendar);
                textView_time.setText(readTime);
            }
        };





    }





    @Override
    protected void onResume()
    {
        super.onResume();

        // 인텐트로 전달받은 bookId 의 책 정보를 화면에서 보여주기 위해서
        // 저장되어있는 책 리스트를 불러오고 인텐트로 전달받은 bookId 와 동일한 bookId 를 가지고 있는 책을 찾아 정보를 가져온다.
        // 가져오는 정보는 책 제목, 표지, 작가, 출판사, 출판일, 독서 시간이다.
        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
        String bookListString = bookInfo.getString("bookList", null);

        try
        {
            JSONArray jsonArray = new JSONArray(bookListString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (bookId == jsonObject.getInt("bookId"))
                {
                    title = jsonObject.getString("title");
                    img = jsonObject.getString("img");
                    writer = jsonObject.getString("writer");
                    publisher = jsonObject.getString("publisher");
                    publishDate = jsonObject.getString("publishDate");
                    readTime = jsonObject.getString("readTime");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        // 책 정보를 뷰 요소에 배치해서 보여준다.
        // 이미지의 경우 문자열의 형태로 저장되어있으므로 비트맵으로 변환해서 배치한다.
        // Util 클래스는 문자열을 비트맵 형식으로, 비트맵을 문자열로 바꿔주는 메소드를 포함한 클래스이다.
        textView_title.setText(title);

        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(img);

        imageView_img.setImageBitmap(bitmap);
        textView_writer.setText(writer);
        textView_publisher.setText(publisher);
        textView_publish_date.setText(publishDate);
        textView_time.setText(readTime);

    }



}
