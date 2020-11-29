package com.example.everybooks;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity
{
    ListView listView;
    CalendarAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_calendar);

        // 어댑터 객체 생성
        adapter = new CalendarAdapter();
        listView =  findViewById(R.id.calendar_book_list);

        //리스트뷰에 어댑터를 붙여서 사용자에게 내용이 보이도록 한다.
        listView.setAdapter(adapter);
    }
}
