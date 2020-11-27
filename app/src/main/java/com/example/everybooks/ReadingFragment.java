package com.example.everybooks;

import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// 읽고 있는 책 목록
public class ReadingFragment extends Fragment
{
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    //  뷰 요소 선언
    ImageView imageView_img;
    TextView textView_title;
    TextView textView_start_date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // 화면 생성
        view = inflater.inflate(R.layout.fragment_reading, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        imageView_img = view.findViewById(R.id.img);
        textView_title = view.findViewById(R.id.title);
        textView_start_date = view.findViewById(R.id.start_date);

        // 리사이클러뷰 생성
        recyclerView = (RecyclerView) view.findViewById(R.id.reading_book_list);

        recyclerView.setHasFixedSize(true);
        adapter = new ReadingBookAdapter(ReadingBookAdapter.readingBookList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // test 없어도 되는지
        //recyclerView.setAdapter(adapter);

    }


}
