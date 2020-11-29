package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SelectBookActivity extends AppCompatActivity
{
    Intent intent;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // 뷰 요소 선언
    ImageView imageView_img;        // 책 표지
    TextView textView_title;        // 책 제목
    TextView textView_start_date;   // 독서 시작일

    @Override
    protected void onStart() {
        super.onStart();

       /* if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_book);

        // 뷰 요소 초기화
        imageView_img = findViewById(R.id.img);
        textView_title = findViewById(R.id.title);
        textView_start_date = findViewById(R.id.start_date);

        // 리사이클러뷰 생성
        recyclerView = (RecyclerView) findViewById(R.id.select_book_list);

        recyclerView.setHasFixedSize(true);
        adapter = new SelectBookAdapter(ReadingBookAdapter.readingBookList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        recyclerView.setAdapter(adapter);


    }

}
