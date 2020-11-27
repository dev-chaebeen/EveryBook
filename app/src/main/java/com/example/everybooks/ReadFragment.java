package com.example.everybooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReadFragment extends Fragment
{
    private View view;

    // 뷰 요소 선언
    LinearLayout readBook;
    
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_read_book, container, false);

        // 리사이클러뷰 생성
        recyclerView = (RecyclerView) view.findViewById(R.id.read_book_list);

        // 임시로 리스트에 데이터 넣기
        ArrayList<Book> bookList = new ArrayList<>();

        Book book1 = new Book();
        book1.setTitle("읽은 책1");
        book1.setWriter("작가1");
        book1.setStarNum(1);


        Book book2 = new Book();
        book2.setTitle("읽은 책2");
        book2.setWriter("작가2");
        book2.setStarNum(2);

        Book book3 = new Book();
        book3.setTitle("읽은 책3");
        book3.setWriter("작가3");
        book3.setStarNum(3);


        Book book4 = new Book();
        book4.setTitle("읽은 책4");
        book4.setWriter("작가4");
        book4.setStarNum(4);

        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);


        recyclerView.setHasFixedSize(true);
        adapter = new ReadBookAdapter(bookList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        readBook = view.findViewById(R.id.read_book);

        /*// 책 클릭하면
        readBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReadBookInfoActivity.class);

                startActivity(intent);
            }
        });*/


    }
}
