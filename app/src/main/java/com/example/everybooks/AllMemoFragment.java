package com.example.everybooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllMemoFragment extends Fragment
{
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        //memo = view.findViewById(R.id.memo);
        title = view.findViewById(R.id.title);
        memo_text = view.findViewById(R.id.memo_text);
        memo_date = view.findViewById(R.id.memo_date);

        // 리사이클러뷰 생성 및 어댑터 연결
        recyclerView = view.findViewById(R.id.all_memo_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AllMemoAdapter(MemoAdapter.memoList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        recyclerView.setAdapter(adapter);
    }
}
