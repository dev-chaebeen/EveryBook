package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    //  뷰 요소 선언
    ImageView imageView_img;
    TextView textView_title;
    TextView textView_start_date;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_reading, container, false);

        // 리사이클러뷰 생성
        recyclerView = (RecyclerView) view.findViewById(R.id.to_read_book_list);

        // 임시로 리스트에 데이터 넣기
        ArrayList<Book> bookList = new ArrayList<>();

        bookList.add(new Book("읽는책1","2020.11.26"));
        bookList.add(new Book("읽는책2","2020.11.27"));
        bookList.add(new Book("읽는책3","2020.11.28"));
        bookList.add(new Book("읽는책4","2020.11.29"));

        recyclerView.setHasFixedSize(true);
        adapter = new ReadingBookAdapter(bookList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        return view;

    }// end onCreate();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        imageView_img = view.findViewById(R.id.img);
        textView_title = view.findViewById(R.id.title);
        textView_start_date = view.findViewById(R.id.start_date);

        // 책 클릭하면
/*        linearLayout_reading_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인텐트 생성해서
                Intent intent = new Intent(getActivity(), ReadingBookInfoActivity.class);

                // 데이터 담기
                intent.putExtra("title", textView_title.getText().toString());          // 책 제목
                intent.putExtra("start_date", textView_start_date.getText().toString());// 독서 시작일

                // 인텐트 담아서 보내기
                startActivity(intent);

            }
        });*/
    }
}
