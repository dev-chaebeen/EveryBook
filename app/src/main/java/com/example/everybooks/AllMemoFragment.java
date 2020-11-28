package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllMemoFragment extends Fragment
{
    Intent intent;
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // 뷰 요소 선언 
    CardView memo;
    TextView title;
    TextView memo_text;
    TextView memo_date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // 화면 생성
        view = inflater.inflate(R.layout.fragment_all_memo, container, false);

        return view;
    }// end onCreateView()

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        //memo = view.findViewById(R.id.memo);
        title = view.findViewById(R.id.title);
        memo_text = view.findViewById(R.id.memo_text);
        memo_date = view.findViewById(R.id.memo_date);

        // 리사이클러 뷰 생성
        recyclerView = view.findViewById(R.id.all_memo_list);

        recyclerView.setHasFixedSize(true);
        adapter = new AllMemoAdapter(MemoAdapter.memoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);



        // 한 메모를 클릭하면
        /*memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // 인텐트에 데이터를 담아서 메모 수정화면으로 이동한다
                // 나중에 수정 가능하면 각각의 reading/read book_info 화면으로 이동하도록 하기
                intent = new Intent(getActivity(), EditMemoActivity.class);
                intent.putExtra("title", title.getText().toString());           // 책 제목
                intent.putExtra("memo_text", memo_text.getText().toString());   // 메모 내용
                intent.putExtra("memo_date", memo_date.getText().toString());   // 메모 날짜
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.all_memo_list);

        recyclerView.setHasFixedSize(true);
        adapter = new AllMemoAdapter(MemoAdapter.memoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

    }
}
