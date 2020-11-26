package com.example.everybooks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

// 읽을 책 목록
public class ToReadFragment extends Fragment
{
    private View view;
    Intent intent;
    
    // 뷰 요소 선언
    FloatingActionButton floatingActionButton_add_book; // 책 추가 버튼
    LinearLayout linearLayout_to_read_book;             // 책
    ImageView imageView_img;  // 표지
    TextView textView_title; // 제목
    TextView textView_date;  // 등록일



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_to_read, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        floatingActionButton_add_book = view.findViewById(R.id.add_book);
        linearLayout_to_read_book = view.findViewById(R.id.to_read_book);
        imageView_img = view.findViewById(R.id.img);
        textView_title = view.findViewById(R.id.title);
        textView_date = view.findViewById(R.id.date);


        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.to_read_book:
                        // 책 클릭하면 책 정보 수정화면으로 전환
                        intent = new Intent(getActivity(), EditBookInfoActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.add_book:
                        // 책 추가 클릭하면 책 정보 추가화면으로 전환
                        intent = new Intent(getActivity(), CreateBookInfoActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        // 각 요소가 클릭되면
        linearLayout_to_read_book.setOnClickListener(click);
        floatingActionButton_add_book.setOnClickListener(click);

    }
}
