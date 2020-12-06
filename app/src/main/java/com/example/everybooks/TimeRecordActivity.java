package com.example.everybooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
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

        // 인텐트로 전달받은 데이터 담기
        bookId = getIntent().getIntExtra("bookId", -1);

        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
        String bookListString = bookInfo.getString("bookList", null);

        try
        {
            JSONArray jsonArray = new JSONArray(bookListString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(bookId == jsonObject.getInt("bookId"))
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


    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 책 정보 보여주기
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
