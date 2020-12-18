 package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

 public class ChartFragment extends Fragment
{
    private View view;

    // 뷰 요소 선언
    BarChart chart;
    Spinner spinner;

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

    int year;
    int selectYear;

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
        spinner = view.findViewById(R.id.spinner_year);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        selectYear = year;

        // spinner 리스트
        Integer[] years = {year, year-1, year-2, year-3};

        //using ArrayAdapter
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, years);
        spinner.setAdapter(spinnerAdapter);

        //event listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                January = 0;
                February = 0;
                March = 0;
                April = 0;
                May = 0;
                June = 0;
                July = 0;
                August = 0;
                September = 0;
                October = 0;
                November = 0;
                December = 0;

                //Toast.makeText(getContext(),"선택된 아이템 : "+ spinner.getSelectedItem(),Toast.LENGTH_SHORT).show();
                selectYear = (Integer) spinner.getSelectedItem();
                drawChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void drawChart()
    {

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


                        Log.d(TAG, "selectYear : " + selectYear);
                        if (yearMonthDate[0].equals(Integer.toString(selectYear)))
                        {
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
        //chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setStartAtZero(true); //setAxisMinimum(0f); // start at zero

        chart.setPinchZoom(false);  // 차트 확대 안되도록 함
        chart.setTouchEnabled(false); // 그래프 터치해도 아무 변화없게 막음
        chart.setDescription (null);// description 제거

        // 범례 제거
        Legend leg = chart.getLegend();
        leg.setEnabled(false);

        // 차트에 데이터 넣기
        chart.setData(data);
    }
}
