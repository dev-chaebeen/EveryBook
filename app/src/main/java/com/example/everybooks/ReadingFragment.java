package com.example.everybooks;

import android.os.Bundle;
import android.util.Log;
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
    TextView textView_explain;
    
    final String TAG = "테스트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_reading_book, container, false);
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
        textView_explain = view.findViewById(R.id.explain);

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "ReadingFragment, readingBookList.size : " + ReadingBookAdapter.readingBookList.size() );
        if(ReadingBookAdapter.readingBookList.size()>0)
        {
            showItemList();
        }
        else if(ReadingBookAdapter.readingBookList.size()== 0)
        {
            textView_explain.setText("여기는 읽고 있는 책을 보관하는 곳이에요 ! \n 책을 클릭해서 메모를 남겨보세요.\n " +
                    "책을 다 읽으면 길게 클릭해보세요 ! ");
        }
    }

    public void showItemList()
    {
        // 리사이클러뷰 생성
        recyclerView = view.findViewById(R.id.reading_book_list);
        recyclerView.setHasFixedSize(true);
        adapter = new ReadingBookAdapter(getContext(), ReadingBookAdapter.readingBookList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
    }


}
