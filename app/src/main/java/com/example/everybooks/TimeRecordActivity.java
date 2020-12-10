package com.example.everybooks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import com.example.everybooks.data.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

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

    Thread timeThread;
    Thread aniThread;

    int timeInSeconds;
    Intent intent;

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
        circleImageView_book_gif = findViewById(R.id.book_gif);
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
                        // start 버튼을 클릭하면 저장되어있는 독서시간이 1초씩 증가하고 책장이 넘어가는 애니메이션이 실행된다.
                        // 독서시간은 사용자가 책을 읽을 때 걸리는 시간을 누적해서 기록해둔 값이다.

                        if(isStart)
                        {
                            // 문자열의 형태로 저장되어있는 readTime 의 형식을 시, 분, 초로 나눈다.
                            // 스레드를 사용해 1초당 1씩 초가 증가하도록 한다.
                            String[] hourMinuteSecond = readTime.split(":");
                            hour = Integer.parseInt(hourMinuteSecond[0]);
                            minute = Integer.parseInt(hourMinuteSecond[1]);
                            second = Integer.parseInt(hourMinuteSecond[2]);

                            Log.d(TAG, "TimeRecordActivity, readTime 에서 시분초 int로 가져오기 : " + hour + minute + second);

                            Log.d(TAG, "TimeRecordActivity, 스레드 실행 전 isStart : " + isStart);

                            // 초 단위로 바꾸기
                            long timeInSeconds = hour * 60 * 60 + minute * 60 + second;

                            Log.d(TAG, " 저장되어있던 초단위 시간 : " + timeInSeconds);

                            TimeRecordThread timeRecordThread = new TimeRecordThread();
                            timeThread = new Thread(timeRecordThread);
                            timeThread.start();

                            Log.d(TAG, "TimeRecordActivity, 스레드 실행 후 isStart : " + isStart);

                            // 책장 넘어가는 스레드 실행
                            GifThread gifThread = new GifThread();
                            aniThread = new Thread(gifThread);
                            aniThread.start();

                            isStart = false;

                        }

                        showNoti();


                        break;

                    case R.id.btn_stop:

                        // 스레드가 생성되지 않은 상황에서 stop 버튼 눌러도 동작하지 않도록 하기 위해서
                        // thread 가 null 이 아닐 때만 코드 동작하도록 한다.
                        if(timeThread != null)
                        {
                            // stop 버튼 클릭하면 1초씩 증가하는 스레드를 멈추고 현재 독서 시간을 저장한다.
                            timeThread.interrupt();
                            aniThread.interrupt();

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
                                        Log.d(TAG, "TimeRecordActivity, stop 버튼 클릭 후 변경한 jsonObject : " + jsonObject.getString("readTime"));
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
            }
        };

        // 각 요소가 클릭되면
        button_start.setOnClickListener(click);
        button_stop.setOnClickListener(click);


    }

    // 타임핸들러 클래스
    Handler timeHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            timeInSeconds = msg.arg1;

            // 여기서 HH:MM:SS 형식으로 바꿔서 보여준다.

          /*  // 24시간이 되면 일 단위가 1 증가하면서 시간 단위는 0이 되므로 분기해서 처리해준다.??
            // 그러면 calendar 쓰는 이유가 있니ㅏ...
            if(hour<24)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                Util util = new Util();
                readTime= util.stringFromCalendar(calendar);
            }
            else
            {

            }*/
            int secs = timeInSeconds;
            int mins = secs / 60;
            secs = secs % 60;
            int hours = mins / 60;
            mins = mins % 60;

            Log.d(TAG, " 형식 변환한 뒤 : " + hours + ":" + mins + ":" + secs);

            readTime = "" + String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
            textView_time.setText(readTime);
        }
    };


    // 이미지 핸들러
    Handler gifHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            super.handleMessage(msg);
            updateThread();
        }
    };

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
    class GifThread implements Runnable
    {
        @Override
        public void run() {
            while(true)
            {
                try
                {
                    gifHandler.sendMessage(gifHandler.obtainMessage());
                    Thread.sleep(150);

                }catch (Throwable t)
                {
                    System.out.println(t.toString());
                    return;
                }
            }
        }
    }


    NotificationManager manager;
    NotificationCompat.Builder builder;
    String CHANNEL_ID = "channel1";
    String CHANEL_NAME = "Channel1";

    public void showNoti()
    {
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            //하위 버전일 경우
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        
        // 시간 측정 멈춰있음 서비스 아니라서 그런가 
        intent = new Intent(this, TimeRecordActivity.class);
        intent.putExtra("bookId", bookId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //알림창 제목
        builder.setContentTitle("Every Book");
        //알림창 메시지
        builder.setContentText("독서시간을 기록중입니다.");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_etc_black_24dp);
        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(true);

        //pendingIntent를 builder에 설정 해줍니다.
        // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        //알림창 실행
        manager.notify(1,notification);

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
