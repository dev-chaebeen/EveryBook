package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// 읽을 책 목록
public class ToReadFragment extends Fragment
{
    private View view;
    Intent intent;
    final String TAG = "테스트";

    // 뷰 요소 선언
    FloatingActionButton floatingActionButton_add_book;
    LinearLayout linearLayout_to_read_book;
    ImageView imageView_img;
    TextView textView_title;
    TextView textView_date;
    TextView textView_explain;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_to_read_book, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 요소 초기화
        floatingActionButton_add_book = view.findViewById(R.id.add_book);
        linearLayout_to_read_book = view.findViewById(R.id.to_read_book);
        imageView_img = view.findViewById(R.id.img);
        textView_title = view.findViewById(R.id.title);
        textView_date = view.findViewById(R.id.content);
        textView_explain = view.findViewById(R.id.explain);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.add_book:
                        // 책 추가 클릭하면 책 정보 추가화면으로 전환
                        intent = new Intent(getActivity(), CreateBookInfoActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        floatingActionButton_add_book.setOnClickListener(click);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // 기존
        //showItemList();

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "ToReadFragment, toReadBookList.size : " + ToReadBookAdapter.toReadBookList.size() );

        showItemList();

        if(ToReadBookAdapter.toReadBookList.size() == 0)
        {
            textView_explain.setText("여기는 읽을 책을 보관하는 곳이에요. \n 하단의 + 버튼을 클릭하거나\n " +
                    "검색을 통해서 책을 추가할 수 있어요. \n\n ⚡ Reading 버튼을 클릭해보세요 ⚡ ");
        }
    }

    public void showItemList()
    {
        // 리사이클러뷰 생성
        recyclerView = view.findViewById(R.id.to_read_book_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new ToReadBookAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

}
