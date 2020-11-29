package com.example.everybooks;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartFragment extends Fragment
{
    private View view;

    // 뷰 요소 선언
    BarChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // 화면 생성
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        // 뷰 요소 초기화
        chart = view.findViewById(R.id.barchart);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // x 축 구성할 리스트
        ArrayList month = new ArrayList();

        month.add("1월");
        month.add("2월");
        month.add("3월");
        month.add("4월");
        month.add("5월");
        month.add("6월");
        month.add("7월");
        month.add("8월");
        month.add("9월");
        month.add("10월");
        month.add("11월");
        month.add("12월");

        // 차트에 출력할 독서 권수
        ArrayList NumOfBooks = new ArrayList();

        // 월별 독서량
        NumOfBooks.add(new BarEntry(1, 0));
        NumOfBooks.add(new BarEntry(2, 1));
        NumOfBooks.add(new BarEntry(3, 2));
        NumOfBooks.add(new BarEntry(4, 3));
        NumOfBooks.add(new BarEntry(5, 4));
        NumOfBooks.add(new BarEntry(6, 5));
        NumOfBooks.add(new BarEntry(1, 6));
        NumOfBooks.add(new BarEntry(4, 7));
        NumOfBooks.add(new BarEntry(5, 8));
        NumOfBooks.add(new BarEntry(6, 9));
        NumOfBooks.add(new BarEntry(1, 10));
        NumOfBooks.add(new BarEntry(29, 11));

        // 데이터 담기
        BarDataSet bardataset = new BarDataSet(NumOfBooks, "Number Of Books");
        chart.animateY(3000);
        BarData data = new BarData(month, bardataset);

        // 차트 색상 지정
        // Color.rgb(189,189,189); // 회색
        // Color.rgb(127,140,127); // green
        // Color.rgb(227,205,184); // green
        bardataset.setColors(ColorTemplate.createColors(new int[]{Color.rgb(227,205,184)}));

        // 텍스트 크기
        bardataset.setValueTextSize(11);
        chart.getXAxis().setTextSize(13);

        chart.setPinchZoom(false);  // 차트 확대 안되도록 함
        chart.setDescription (null);// description 제거

        // 범례 제거
        Legend leg = chart.getLegend();
        leg.setEnabled(false);

        // 차트에 데이터 넣기
        chart.setData(data);
    }
}
