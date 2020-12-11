package com.example.everybooks;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.everybooks.data.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeRecordActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_title;
    ImageView imageView_img;
    TextView textView_writer;
    TextView textView_publisher;
    TextView textView_start_date;
    TextView textView_time;
    Button button_start;
    Button button_stop;
    CircleImageView circleImageView_book_gif;

    int bookId;
    String title;
    String img;
    String writer;
    String publisher;
    String startDate;
    String readTime;

    boolean isStart;
    Thread aniThread;

    Intent intent;
    final String TAG = "테스트";

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
        textView_start_date = findViewById(R.id.start_date);
        textView_time = findViewById(R.id.time);
        circleImageView_book_gif = findViewById(R.id.book_gif);
        button_start = findViewById(R.id.btn_start);
        button_stop = findViewById(R.id.btn_stop);

        // 인텐트로 전달받은 bookId 데이터를 담는다
        // bookId 는 각각의 책을 구분하기 위해서 책이 가지고 있는 고유한 정수값이다.
        bookId = getIntent().getIntExtra("bookId", -1);

        // 한번 start 버튼을 클릭한 상태에서는 다시 클릭해도 독서시간을 1초 씩 증가시키는 스레드를 새로 생성하지 않도록 하기 위해서
        // boolean 변수에 start 버튼 클릭 가능 여부를 저장한다.
        isStart = false;

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
                    startDate = jsonObject.getString("startDate");
                    readTime = jsonObject.getString("readTime");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        Log.d(TAG, "TimeRecordActivity, onCreate() 저장되어있던 readTime" + readTime);

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
                        // 독서시간은 사용자가 특정한 한 권의 책을 읽는 시간을 누적한 값이다.

                        // start 버튼을 클릭하면 isStart 값은 true 가 되고
                        // stop 버튼을 클릭하면 isStart  값이 false 가 된다.

                        // start 버튼을 클릭한 상태에서 다시 클릭하면 스레드가 중복되어 실행되기 때문에
                        // isStart 값이 false 일 때만 코드가 동작하도록 했다.
                        if(!isStart)
                        {
                            isStart = true;

                            // 시간을 측정하는 동안은 계속 TimeRecordActivity 화면으로 전환하기 때문에
                            // 어플의 다른 기능을 이용할 수 없다고 안내한다.
                            AlertDialog.Builder builder = new AlertDialog.Builder(TimeRecordActivity.this);
                            builder.setMessage("독서 시간을 기록하는 동안은\n 다른 기능을 사용할 수 없어요!\n 기록을 시작할까요? ");
                            builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                        // 책장 넘어가는 이미지 스레드 실행
                                        GifThread gifThread = new GifThread();
                                        aniThread = new Thread(gifThread);
                                        aniThread.start();


                                        // TimeRecordService 실행
                                        Intent intent = new Intent(getApplicationContext(), TimeRecordService.class);
                                        intent.putExtra("readTime", readTime);
                                        intent.putExtra("bookId", bookId);
                                        startService(intent);




                                        dialog.dismiss();
                                    }
                                });

                            builder.setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            builder.show();
                        }

                        // 독서 시간을 기록 중이라는 알림을 상단창에 띄운다.
                        showNoti();
                        break;

                    case R.id.btn_stop:

                        // stop 버튼을 클릭하면 서비스를 종료한다.
                        intent = new Intent(getApplicationContext(), TimeRecordService.class);
                        stopService(intent);

                        // 책장 넘어가는 이미지 스레드를 멈춘다.
                        if(aniThread != null)
                        {
                            aniThread.interrupt();
                        }

                        // start 버튼을 다시 클릭할 수 있도록 isStart 값을 false 로 바꾼다.
                        isStart = false;

                        break;
                }
            }
        };

        // 각 요소가 클릭되면
        button_start.setOnClickListener(click);
        button_stop.setOnClickListener(click);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 책 정보를 뷰 요소에 배치해서 보여준다.
        // 이미지의 경우 문자열의 형태로 저장되어있으므로 비트맵으로 변환해서 배치한다.
        // Util 클래스는 문자열을 비트맵 형식으로, 비트맵을 문자열로 바꿔주는 메소드를 포함한 클래스이다.
        textView_title.setText(title);

        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(img);

        imageView_img.setImageBitmap(bitmap);
        textView_writer.setText(writer);
        textView_publisher.setText(publisher);
        textView_start_date.setText(startDate);
        textView_time.setText(readTime);

    }

    // onNewIntent()
    // 실행한 Activity 가 foreground 인 상태에서 Intent 에 데이터를 담아서 다시 호출되면
    // onCreate() 대신 onNewIntent() 가 호출된다.
    @Override
    protected void onNewIntent(Intent intent)
    {
        if(intent != null){

            Log.d(TAG, "onNewIntent");

            bookId = intent.getIntExtra("bookId", -1);
            readTime  = intent.getStringExtra("readTime");
            Log.d(TAG, "서비스에서 전달받은 readTime" + readTime);

            // 책 정보를 뷰 요소에 배치해서 보여준다.
            // 이미지의 경우 문자열의 형태로 저장되어있으므로 비트맵으로 변환해서 배치한다.
            // Util 클래스는 문자열을 비트맵 형식으로, 비트맵을 문자열로 바꿔주는 메소드를 포함한 클래스이다.
            Util util = new Util();
            Bitmap bitmap = util.stringToBitmap(img);

            textView_title.setText(title);
            imageView_img.setImageBitmap(bitmap);
            textView_writer.setText(writer);
            textView_publisher.setText(publisher);
            textView_start_date.setText(startDate);
            textView_time.setText(readTime);
        }

        super.onNewIntent(intent);
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


        //알림창 제목
        builder.setContentTitle("Every Book");
        //알림창 메시지
        builder.setContentText("독서시간을 기록중입니다.");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_etc_black_24dp);
        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(true);

        // 알림창을 클릭하면 bookId 데이터를 인텐트에 담고 TimeRecordActivity 로 화면을 전환한다.
        // bookId 를 전달하는 이유는 해당하는 책의 정보를 보여주기 위함이다.
        intent = new Intent(this, TimeRecordActivity.class);
        intent.putExtra("bookId", bookId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        //알림창 실행
        manager.notify(1,notification);

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
