package com.example.everybooks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
                        // 로그아웃을 클릭하면
                        // 로그인 여부를 false 로 초기화하고 sharedPreference (autoLogin) 에 저장되어있는
                        // 모든 정보(로그인했던 아이디와 비밀번호)를 지우고 로그인 화면으로 전환한다.
                        MainActivity.isLogin = false;
                        SharedPreferences autoLogin = v.getContext().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = autoLogin.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(v.getContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();

                        intent = new Intent(v.getContext(), LoginActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.feedback :

                        // feedback 을 클릭하면 메일을 보낼 수 있는 앱을 선택할 수 있다.
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
