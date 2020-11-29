package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SelectBookActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // 뷰 요소 선언
    ImageView imageView_img;
    TextView textView_title;
    TextView textView_start_date;

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
        recyclerView =  findViewById(R.id.select_book_list);

        recyclerView.setHasFixedSize(true);
        adapter = new SelectBookAdapter(ReadingBookAdapter.readingBookList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        recyclerView.setAdapter(adapter);


    }

}
