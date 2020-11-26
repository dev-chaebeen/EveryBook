package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment
{
    Intent intent;

    // 뷰 요소 선언
    private View view;

    Button button_to_read;   // to_read 버튼
    Button button_reading;   // reading 버튼
    Button button_read;      // read 버튼
    EditText editText_search;// 검색 텍스트
    Spinner spinner_order;   // 정렬 순서
    ImageView imageView_mic; // 마이크 아이콘


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
       /* if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }*/

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // 처음 childFragment 지정
        getChildFragmentManager().beginTransaction().add(R.id.home_frame, new ToReadFragment()).commit();

        return view;
    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 뷰 요소 초기화
        button_to_read = view.findViewById(R.id.btn_to_read);
        button_reading =  view.findViewById(R.id.btn_reading);
        button_read = view.findViewById(R.id.btn_read);
        editText_search = view.findViewById(R.id.search);
        spinner_order = view.findViewById(R.id.spinner_order);
        imageView_mic = view.findViewById(R.id.mic);

        // to_read 클릭 이벤트
        button_to_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.home_frame, new ToReadFragment()).commit();
            }
        });

        // reading 클릭 이벤트
        button_reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.home_frame, new ReadingFragment()).commit();
            }
        });

        // read 클릭 이벤트
        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.home_frame, new ReadFragment()).commit();
            }
        });
    }
}