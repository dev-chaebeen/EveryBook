package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class RecordFragment extends Fragment
{
    private View view;
    Intent intent;

    // 뷰 요소 선언
    Button button_memo; // MEMO 버튼
    Button button_chart;// CHART 버튼

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
      /*  if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

        }*/

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_record, container, false);

        // 처음 childFragment 지정
        getChildFragmentManager().beginTransaction().add(R.id.record_frame, new AllMemoFragment()).commit();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  뷰 요소 초기화
        button_memo =  view.findViewById(R.id.btn_memo);
        button_chart = view.findViewById(R.id.btn_chart);


        // btn_memo 클릭 이벤트
        button_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.record_frame, new AllMemoFragment()).commit();
            }
        });

        // btn_chart 클릭 이벤트
        button_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.record_frame, new ChartFragment()).commit();
            }
        });


    }
}
