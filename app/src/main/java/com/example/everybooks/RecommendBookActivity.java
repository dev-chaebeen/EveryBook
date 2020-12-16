package com.example.everybooks;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;

import java.util.ArrayList;

public class RecommendBookActivity extends AppCompatActivity
{
    // 뷰 요소 초기화
    TextView textView_total_num;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_book);


        // 임시로 데이터 추가
        ArrayList<Book> list = RecommendBookAdapter.recommendBookList;
        Book book = new Book();
        book.setTitle("추천책");
        book.setWriter("추천책작가");
        book.setPlot("추천책 줄거리");
        list.add(book);

        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.recommend_book_list);
        recyclerView.setHasFixedSize(true);
        adapter = new RecommendBookAdapter(RecommendBookAdapter.recommendBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }
}
