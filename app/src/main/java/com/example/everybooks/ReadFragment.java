package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReadFragment extends Fragment
{
    private View view;

    // 뷰 요소 선언
    LinearLayout readBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_read, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        readBook = view.findViewById(R.id.read_book);

        // 책 클릭하면
        readBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReadBookInfoActivity.class);

                startActivity(intent);
            }
        });


    }
}
