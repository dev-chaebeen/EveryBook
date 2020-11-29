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

public class ProfileFragment extends Fragment
{
    View view;

    // 뷰 요소 선언
    LinearLayout linearLayout_edit_profile;
    LinearLayout linearLayout_logout;
    LinearLayout linearLayout_feedback;

    Intent intent;
    View.OnClickListener click;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_profile_with_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        linearLayout_edit_profile = view.findViewById(R.id.edit_profile);
        linearLayout_logout = view.findViewById(R.id.logout);
        linearLayout_feedback = view.findViewById(R.id.feedback);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {

                    case R.id.edit_profile :
                        // Edit Profile 클릭하면 프로필 편집 화면으로 전환
                        intent = new Intent(getActivity(), EditProfileActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.logout :
                        // 로그아웃 시키고

                        break;

                    case R.id.feedback :

                        // 메일 보내기
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        String[] address = {"chxxbeen@gmail.com"};
                        intent.putExtra(Intent.EXTRA_EMAIL, address);                               // 받는 사람
                        intent.putExtra(Intent.EXTRA_SUBJECT, "everyBooks 피드백입니다.");     // 보내질 이메일 제목
                        intent.putExtra(Intent.EXTRA_TEXT, "피드백을 작성해주세요.");           // 보내질 이메일 내용
                        startActivity(intent);

                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        linearLayout_edit_profile.setOnClickListener(click);
        linearLayout_logout.setOnClickListener(click);
        linearLayout_feedback.setOnClickListener(click);
    }
}
