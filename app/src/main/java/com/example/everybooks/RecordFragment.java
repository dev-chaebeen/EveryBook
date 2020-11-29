package com.example.everybooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecordFragment extends Fragment
{
    private View view;

    // 뷰 요소 선언
    Button button_memo;
    Button button_chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // 화면 생성
        view = inflater.inflate(R.layout.fragment_record, container, false);
        getChildFragmentManager().beginTransaction().add(R.id.record_frame, new AllMemoFragment()).commit();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //  뷰 요소 초기화
        button_memo =  view.findViewById(R.id.btn_memo);
        button_chart = view.findViewById(R.id.btn_chart);

        // btn_memo 클릭 이벤트 등록
        // 작성한 모든 메모를 볼 수 있는 화면으로 전환한다.
        button_memo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getChildFragmentManager().beginTransaction().replace(R.id.record_frame, new AllMemoFragment()).commit();
            }
        });

        // btn_chart 클릭 이벤트 등록
        // 월별 독서량을 볼 수 있는 화면으로 전환한다.
        button_chart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getChildFragmentManager().beginTransaction().replace(R.id.record_frame, new ChartFragment()).commit();
            }
        });
    }
}
