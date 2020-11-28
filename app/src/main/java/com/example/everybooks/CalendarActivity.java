package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class CalendarActivity extends AppCompatActivity
{
    Intent intent;
    ListView listView;
    CalendarAdapter adapter;

    // 뷰 요소 선언
    TextView textView_title;        // 책 제목
    TextView textView_memo_text;    // 메모 내용
    CardView cardView_memo;         // 메모

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

        // 화면 생성
        setContentView(R.layout.activity_calendar);

        // 뷰 요소 초기화
      /*  textView_title = findViewById(R.id.title);
        textView_memo_text = findViewById(R.id.memo_text);
        cardView_memo = findViewById(R.id.memo);*/

        // 어댑터 객체 생성
        adapter = new CalendarAdapter();
        listView =  findViewById(R.id.calendar_book_list);

        //리스트뷰에 어댑터를 붙여서 사용자에게 내용이 보이도록 한다.
        listView.setAdapter(adapter);

    }
}
