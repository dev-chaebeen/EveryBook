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
    LinearLayout linearLayout_edit_profile;
    LinearLayout linearLayout_login;
    LinearLayout linearLayout_feedback;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
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
}






