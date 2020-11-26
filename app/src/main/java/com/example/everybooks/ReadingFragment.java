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

// 읽고 있는 책 목록
public class ReadingFragment extends Fragment
{
    private View view;

    //  뷰 요소 선언
    LinearLayout linearLayout_reading_book;
    ImageView imageView_img;        // 책 표지
    TextView textView_title;        // 책 제목
    TextView textView_start_date;   // 독서 시작일

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_reading, container, false);

        return view;

    }// end onCreate();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 뷰 요소 초기화
        linearLayout_reading_book = view.findViewById(R.id.reading_book);
        imageView_img = view.findViewById(R.id.img);
        textView_title = view.findViewById(R.id.title);
        textView_start_date = view.findViewById(R.id.start_date);

        // 책 클릭하면
        linearLayout_reading_book.setOnClickListener(new View.OnClickListener() {
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
        });
    }
}
