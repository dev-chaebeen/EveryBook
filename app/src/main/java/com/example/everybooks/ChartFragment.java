 package com.example.everybooks;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartFragment extends Fragment
{
    private View view;

    // 뷰 요소 선언
    BarChart chart;

    int January;
    int February;
    int March;
    int April;
    int May;
    int June;
    int July;
    int August;
    int September;
    int October;
    int November;
    int December;

    final String TAG = "ChartFragment";

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

        // 저장되어있는 데이터 불러오기
        try
        {
            SharedPreferences bookInfo = view.getContext().getSharedPreferences("bookInfo", Context.MODE_PRIVATE);
            String bookListString = bookInfo.getString("bookList", null);

            //Log.d(TAG, "bookListString : " + bookListString);
            if(bookListString != null)
            {
                JSONArray jsonArray = new JSONArray(bookListString);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    if(jsonObject.getString("state").equals("read"))
                    {
                        String endDate = jsonObject.getString("endDate");

                        Log.d(TAG, "endDate : " + endDate);

                        String[] yearMonthDate = endDate.split("\\.");

                        if (yearMonthDate[0].equals("2020")) {
                            switch (yearMonthDate[1]) {
                                case "01":
                                    January++;
                                    break;
                                case "02":
                                    February++;
                                    break;
                                case "03":
                                    March++;
                                    break;
                                case "04":
                                    April++;
                                    break;
                                case "05":
                                    May++;
                                    break;
                                case "06":
                                    June++;
                                    break;
                                case "07":
                                    July++;
                                    break;
                                case "08":
                                    August++;
                                    break;
                                case "09":
                                    September++;
                                    break;
                                case "10":
                                    October++;
                                    break;
                                case "11":
                                    November++;
                                    break;
                                case "12":
                                    December++;
                                    break;

                            }
                        }

                    }
                }
            }
        }
        catch (JSONException e)
        {
            System.out.println(e.toString());
        }




        // 차트에 출력할 독서 권수
        ArrayList NumOfBooks = new ArrayList();

        // 월별 독서량
        NumOfBooks.add(new BarEntry(January, 0));
        NumOfBooks.add(new BarEntry(February, 1));
        NumOfBooks.add(new BarEntry(March, 2));
        NumOfBooks.add(new BarEntry(April, 3));
        NumOfBooks.add(new BarEntry(May, 4));
        NumOfBooks.add(new BarEntry(June, 5));
        NumOfBooks.add(new BarEntry(July, 6));
        NumOfBooks.add(new BarEntry(August, 7));
        NumOfBooks.add(new BarEntry(September, 8));
        NumOfBooks.add(new BarEntry(October, 9));
        NumOfBooks.add(new BarEntry(November, 10));
        NumOfBooks.add(new BarEntry(December, 11));

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

        Log.d(TAG, "12월에 읽은 책 수 : " + December );
    }
}
