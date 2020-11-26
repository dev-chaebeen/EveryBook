package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ReadBookInfoActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;

    // 뷰 요소 선언
    TextView textView_edit; // Edit
    Button button_memo;     // MEMO 버튼 
    Button button_delete;   // DELETE 버튼 

    TextView textView_title;        // 책 제목
    ImageView imageView_img;        // 책 표지
    TextView textView_writer;       // 작가
    TextView textView_publisher;    // 출판사
    TextView textView_publish_date; // 출판일
    TextView textView_start_date;   // 독서 시작일 
    TextView textView_end_date;     // 독서 마감일 
    TextView textView_time;         // 독서 시간 

    RatingBar ratingBar_rate;       // 별점 

    CardView cardView_memo;         // 메모 
    TextView textView_memo_text;    // 메모 내용
    TextView textView_memo_date;    // 메모 날짜 

    @Override
    protected void onStart() {
        super.onStart();

        /*if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 구성
        setContentView(R.layout.activity_read_book_info);

        // 뷰 요소 초기화
        textView_edit = findViewById(R.id.edit);
        button_memo = findViewById(R.id.btn_memo);
        button_delete = findViewById(R.id.btn_delete);

        textView_title = findViewById(R.id.title);
        imageView_img = findViewById(R.id.img);
        textView_writer = findViewById(R.id.writer);
        textView_publisher = findViewById(R.id.publisher);
        textView_publish_date = findViewById(R.id.publish_date);
        textView_start_date = findViewById(R.id.start_date);
        textView_end_date = findViewById(R.id.end_date);
        textView_time = findViewById(R.id.time);

        ratingBar_rate = findViewById(R.id.rate);

        cardView_memo = findViewById(R.id.memo);
        textView_memo_text = findViewById(R.id.memo_text);
        textView_memo_date = findViewById(R.id.memo_date);


        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_memo:
                        // MEMO 버튼을 클릭하면 메모 작성 화면으로 전환한다
                        intent = new Intent(getApplicationContext(), CreateMemoActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.btn_delete:
                        // 삭제하고 이전 화면으로 돌아가기(현재 액티비티 finish())

                        break;

                    case R.id.edit :
                        // Edit 을 클릭하면 책 정보 수정화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), EditBookInfoActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.memo :
                        // 메모를 클릭하면 책 제목과 메모 내용을 담아서 메모 편집 화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), EditMemoActivity.class);
                        intent.putExtra("title", getIntent().getStringExtra("title"));
                        intent.putExtra("memo_text", textView_memo_text.getText().toString());
                        startActivity(intent);
                }
            }
        };

        // 각 요소가 클릭되면
        button_memo.setOnClickListener(click);
        button_delete.setOnClickListener(click);
        textView_edit.setOnClickListener(click);
        cardView_memo.setOnClickListener(click);

    }// onCreate()



}
