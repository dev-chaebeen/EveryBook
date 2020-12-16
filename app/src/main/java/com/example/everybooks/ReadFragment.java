package com.example.everybooks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReadFragment extends Fragment
{
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // 뷰 요소 선언
    ImageView imageView_img;
    TextView textView_title;
    TextView textView_writer;
    RatingBar ratingBar_rate;
    TextView textView_explain;

    final String TAG = "테스트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // 화면 생성
        view = inflater.inflate(R.layout.fragment_read_book, container, false);

        // 뷰 요소 초기화
        imageView_img = view.findViewById(R.id.img);
        textView_title = view.findViewById(R.id.title);
        ratingBar_rate = view.findViewById(R.id.rate);
        textView_writer = view.findViewById(R.id.writer);
        textView_explain = view.findViewById(R.id.explain);

        if(ReadBookAdapter.readBookList.size()== 0)
        {
            textView_explain.setText("여기는 읽은 책을 보관하는 곳이에요 ! \n 책을 클릭해서 메모를 남길 수 있어요.");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "ReadFragment, readBookList.size : " + ReadBookAdapter.readBookList.size() );

        if(ReadBookAdapter.readBookList.size()== 0)
        {
            textView_explain.setText("여기는 읽은 책을 보관하는 곳이에요 ! \n 책을 클릭해서 메모를 남길 수 있어요.");
        }

        showItemList();
    }

    public void showItemList()
    {
        // 리사이클러뷰 생성
        recyclerView = (RecyclerView) view.findViewById(R.id.read_book_list);
        recyclerView.setHasFixedSize(true);
        adapter = new ReadBookAdapter(getContext(), ReadBookAdapter.readBookList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
    }

}
