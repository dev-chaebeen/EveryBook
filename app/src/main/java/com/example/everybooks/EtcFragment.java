package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class EtcFragment extends Fragment
{
    // 뷰 요소 선언
    LinearLayout linearLayout_notification; // 알림
    LinearLayout linearLayout_time_record;  // 시간 기록
    LinearLayout linearLayout_calendar;     // 캘린더
    LinearLayout linearLayout_asmr_video;   // asmr 비디오
    LinearLayout linearLayout_translate;    // 번역

    View.OnClickListener click;
    Intent intent;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // 화면 생성 
        view = inflater.inflate(R.layout.fragment_etc, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        linearLayout_notification = view.findViewById(R.id.notification);
        linearLayout_time_record = view.findViewById(R.id.time_record);
        linearLayout_calendar = view.findViewById(R.id.calendar);
        linearLayout_asmr_video = view.findViewById(R.id.asmr_video);
        linearLayout_translate = view.findViewById(R.id.translate);


        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.notification :
                        intent = new Intent(getActivity(), NotificationActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.time_record :
                        intent = new Intent(getActivity(), SelectBookActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.calendar :
                        intent = new Intent(getActivity(), CalendarActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.asmr_video :
                        intent = new Intent(getActivity(), SearchVideoActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.translate :
                        intent = new Intent(getActivity(), TranslateActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        linearLayout_notification.setOnClickListener(click);
        linearLayout_time_record.setOnClickListener(click);
        linearLayout_calendar.setOnClickListener(click);
        linearLayout_asmr_video.setOnClickListener(click);
        linearLayout_translate.setOnClickListener(click);
    }
}




































        /*// 상수 지정
        final int NOTIFICATION = 0;
        final int TIME_RECORD = 1;
        final int CALENDAR = 2;
        final int ASMR_VIDEO = 3;
        final int TRANSLATE = 4;

        // 리스트뷰 지정
        final String[] LIST_MENU = {"Notification", "Time Record", "Calendar", "ASMR Video", "Translate"} ;

        // 리스트뷰 생성
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU);    // 어댑터를 생성해서
        ListView listView = view.findViewById(R.id.etc_list) ;   // 지정된 리스트뷰에
        listView.setAdapter(adapter) ;  // 뿌려준다.

        // 클릭한 값에 따라 화면 전환
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;

                switch(position)
                {
                    case NOTIFICATION :
                        intent = new Intent(getActivity(), NotificationActivity.class);
                        startActivity(intent);
                        break;

                    case TIME_RECORD :
                        intent = new Intent(getActivity(), SelectBookActivity.class);
                        startActivity(intent);
                        break;

                    case CALENDAR :
                        intent = new Intent(getActivity(), CalendarActivity.class);
                        startActivity(intent);
                        break;

                    case ASMR_VIDEO :
                        intent = new Intent(getActivity(), SearchVideoActivity.class);
                        startActivity(intent);
                        break;

                    case TRANSLATE :
                        intent = new Intent(getActivity(), TranslateActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });*/
