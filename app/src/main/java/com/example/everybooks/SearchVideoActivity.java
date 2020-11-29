package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchVideoActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    SearchView searchView_search;
    TextView textView_search_num;

    ImageView imageView_img;
    TextView textView_title;
    TextView textView_channel;
    TextView textView_description;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_search_video);

        // 뷰 요소 초기화
        textView_search_num = findViewById(R.id.search_num);
        searchView_search = findViewById(R.id.search);
        imageView_img = findViewById(R.id.img);
        textView_title = findViewById(R.id.title);
        textView_channel = findViewById(R.id.channel);
        textView_description = findViewById(R.id.description);

        // 임시로 데이터 추가
        ArrayList<Video> list = SearchVideoAdapter.searchVideoList;
        Video video = new Video();
        video.setTitle("검색한비디오제목");
        video.setChannel("검색한비디오채널");
        video.setDescription("영상설명");
        list.add(video);

        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.search_video_list);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchVideoAdapter(SearchVideoAdapter.searchVideoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        // 검색창에 키워드를 입력하고 검색 버튼을 누르면 검색 결과화면을 보여준다.
        searchView_search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // 검색 키워드 제출하면 수행할 동작
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }
}
