package com.example.everybooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchVideoActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    //EditText editText_search;       // 검색 텍스트
    ImageView imageView_mic;        // 마이크 이미지
    TextView textView_search_num;   // 검색 결과 수

   // CardView cardView_video;        // 영상
    ImageView imageView_img;        // 영상 썸네일
    TextView textView_title;        // 영상 제목
    TextView textView_channel;      // 채널 이름
    TextView textView_description;  // 영상 설명 

    Intent intent;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    
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

        // 화면 생성
        setContentView(R.layout.activity_search_video);

        // 뷰 요소 초기화
       // editText_search = findViewById(R.id.search);
        imageView_mic = findViewById(R.id.mic);
        textView_search_num = findViewById(R.id.search_num);

        //cardView_video = findViewById(R.id.video);
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

        
/*        // 비디오 클릭하면 유튜브 재생
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
}
