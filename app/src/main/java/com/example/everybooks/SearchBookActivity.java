package com.example.everybooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchBookActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    EditText editText_search;       // 검색 텍스트
    ImageView imageView_mic;        // 마이크 이미지
    TextView textView_search_num;   // 검색 결과 수

    Intent intent;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // 인텐트로 수신하는 데이터
    String keyword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_search_book);

        // 뷰 요소 초기화
        editText_search = findViewById(R.id.search);
        imageView_mic = findViewById(R.id.mic);
        textView_search_num = findViewById(R.id.search_num);

        // 임시로 데이터 추가
        ArrayList<Book> list = SearchBookAdapter.searchBookList;
        Book book = new Book();
        book.setTitle("검색한책");
        book.setWriter("검색한책작가");
        book.setPlot("줄거리");
        list.add(book);



        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.search_book_list);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchBookAdapter(SearchBookAdapter.searchBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


       /* // 비디오 클릭하면 유튜브 재생
        cardView_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=gShDtRwTLXw"));
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView.setAdapter(adapter);


        // 인텐트로 수신한 데이터 보여주기
        //keyword = intent.getStringExtra("keyword");
        //editText_search.setText(keyword);


    }
}
