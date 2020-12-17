package com.example.everybooks;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    ImageView imageView_mic;
    TextView textView_search_num;
    ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // test
    private List<String> list;                      // String 데이터를 담고있는 리스트
    boolean lastItemVisibleFlag = false; //  // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_search_book);

        // 뷰 요소 초기화
        imageView_mic = findViewById(R.id.mic);
        textView_search_num = findViewById(R.id.search_num);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.GONE);

        for (int i = 0; i < 20; i++) {
            // 임시로 데이터 추가
            ArrayList<Book> list = SearchBookAdapter.searchBookList;
            Book book = new Book();
            book.setTitle("검색한책"+i);
            book.setWriter("검색한책작가"+i);
            book.setPlot("줄거리"+i);
            list.add(book);
        }


        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.search_book_list);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchBookAdapter(SearchBookAdapter.searchBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

                // 리사이클러뷰의 마지막 도착 ! 다음 데이터 로드
                if (lastVisibleItemPosition == itemTotalCount)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    // 다음 데이터를 불러온다.
                    getItem();
                }

            }
        });
    }

    private void getItem()
    {

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        // 다음 20개의 데이터를 불러와서 리스트에 저장한다.
        for (int i = 0; i < 20; i++) {
            // 임시로 데이터 추가
            ArrayList<Book> list = SearchBookAdapter.searchBookList;
            Book book = new Book();
            book.setTitle("추가한책"+i);
            book.setWriter("추가한책작가"+i);
            book.setPlot("추가한줄거리"+i);
            list.add(book);
        }

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);

    }
}
