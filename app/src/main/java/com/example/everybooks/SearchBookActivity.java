package com.example.everybooks;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;

import java.util.ArrayList;

public class SearchBookActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    ImageView imageView_mic;
    TextView textView_search_num;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_search_book);

        // 뷰 요소 초기화
        imageView_mic = findViewById(R.id.mic);
        textView_search_num = findViewById(R.id.search_num);

        // 임시로 데이터 추가
        ArrayList<Book> list = SearchBookAdapter.searchBookList;
        Book book = new Book();
        book.setTitle("검색한책");
        book.setWriter("검색한책작가");
        //book.setPlot("줄거리");
        list.add(book);

        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.search_book_list);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchBookAdapter(SearchBookAdapter.searchBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

}
