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

public class ProfileLogoutFragment extends Fragment
{
    private View view;

    // 뷰 요소 선언
    LinearLayout linearLayout_edit_profile; // 프로필 편집
    LinearLayout linearLayout_login;        // 로그인
    LinearLayout linearLayout_feedback;     // 피드백 보내기

    Intent intent;
    View.OnClickListener click;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 화면 생성
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;

    }// end onCreate

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 뷰 요소 초기화
        linearLayout_edit_profile = view.findViewById(R.id.edit_profile);
        linearLayout_login = view.findViewById(R.id.login);
        linearLayout_feedback = view.findViewById(R.id.feedback);


        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.edit_profile :
                        Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.login :
                        // 로그인 화면으로
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.feedback :
                        // 로그인 화면으로
                        Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        linearLayout_edit_profile.setOnClickListener(click);
        linearLayout_login.setOnClickListener(click);
        linearLayout_feedback.setOnClickListener(click);

    }
}// end class













 /* // 기본 리스트뷰
        // 리스트뷰 지정
        final String[] LIST_MENU = {"Edit Profile", "Login", "Feedback"} ;

        // 리스트뷰 생성
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU);    // 어댑터를 생성해서
        ListView listView = (ListView) view.findViewById(R.id.profile_list) ;   // 지정된 리스트뷰에
        listView.setAdapter(adapter) ;  // 뿌려준다.

        // 상수 지정
        final int EDIT_PROFILE = 0;
        final int LOGIN = 1;
        final int FEEDBACK = 2;

        // 클릭한 값에 따라 화면 전환
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;

                switch(position)
                {
                    case EDIT_PROFILE :
                        intent = new Intent(getActivity(), EditProfileActivity.class);
                        startActivity(intent);
                        break;

                    case LOGIN :
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;

                    case FEEDBACK :
                        // 메일 보내기
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        String[] address = {"chxxbeen@gmail.com"};
                        intent.putExtra(Intent.EXTRA_EMAIL, address);                               // 받는 사람
                        intent.putExtra(Intent.EXTRA_SUBJECT, "everyBooks 피드백입니다.");     // 보내질 이메일 제목
                        intent.putExtra(Intent.EXTRA_TEXT, "피드백을 보내주세요.");             // 보내질 이메일 내용
                        startActivity(intent);

                        break;


                }
            }
        });*/