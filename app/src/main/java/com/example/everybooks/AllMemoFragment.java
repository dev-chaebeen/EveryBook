package com.example.everybooks;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import java.util.Locale;

public class AllMemoFragment extends Fragment
{
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    TextToSpeech tts;

    // 뷰 요소 선언 
    TextView title;
    TextView memo_text;
    TextView memo_date;
    TextView explain;

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
        explain = view.findViewById(R.id.explain);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //음성출력 생성, 리스너 초기화
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=android.speech.tts.TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if(AllMemoAdapter.allMemoList.size() == 0)
        {
            explain.setText(" 여기엔 작성한 모든 메모가 보여져요 ! ");
        }

        // 리사이클러뷰 생성 및 어댑터 연결
        recyclerView = view.findViewById(R.id.all_memo_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AllMemoAdapter(getContext(), tts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }

    }
}
