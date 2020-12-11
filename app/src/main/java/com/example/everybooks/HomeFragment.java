package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;

import java.util.Random;

public class HomeFragment extends Fragment
{
    Intent intent;
    private View view;

    // 뷰 요소 선언
    Button button_to_read;
    Button button_reading;
    Button button_read;
    androidx.appcompat.widget.SearchView searchView_search;
    Spinner spinner_order;
    ImageView imageView_mic;

    final String TAG = "테스트";
    int randomNum;
    int length;

    Handler memoHandler;
    Thread thread;

    FragmentManager fragmentManager;

    // 랜덤 메모 스레드
    // 사용자가 작성한 메모를 일정한 시간 간격마다 랜덤으로 보여주기 위해서
    // 사용자가 작성한 메모의 수 이내의 랜덤 숫자를 3초마다 발생시키는 스레드이다.
    class MemoThread implements Runnable {
        boolean running = false;

        public void run() {
            running = true;
            while (running) {
                Message message = memoHandler.obtainMessage();

                Random random = new Random();
                randomNum = random.nextInt(length);

                message.arg1 = randomNum;

                memoHandler.sendMessage(message);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getChildFragmentManager().beginTransaction().add(R.id.home_frame, new ToReadFragment()).commit();

        // 뷰 요소 초기화
        button_to_read = view.findViewById(R.id.btn_to_read);
        button_reading =  view.findViewById(R.id.btn_reading);
        button_read = view.findViewById(R.id.btn_read);
        searchView_search = view.findViewById(R.id.search);
        spinner_order = view.findViewById(R.id.spinner_order);
        imageView_mic = view.findViewById(R.id.mic);

        fragmentManager = getChildFragmentManager();

        Log.d(TAG, "HomeFragment onCreateView()");

        // 랜덤 메모 스레드에서 생성된 랜덤 수를 랜덤 메모 프래그먼트로 전달하면서 프래그먼트를 생성하는 핸들러
        memoHandler = new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage (@NonNull Message msg){
                super.handleMessage(msg);

                int randomNum = msg.arg1;
                Log.d(TAG, "HomeFragment 핸들러 " + randomNum);

                // 랜덤 메모 프래그먼트를 교체한다.
                fragmentManager.beginTransaction().replace(R.id.random_memo_frame, new RandomMemoFragment(randomNum)).commitAllowingStateLoss();

                // 기존 코드 2. getChildFragmentManager().beginTransaction().add(R.id.random_memo_frame, new RandomMemoFragment(randomNum)).commitAllowingStateLoss();
                // 에러 발생 fragment-has-not-been-attached-yet
                // 원인 : 부모 프래그먼트가 생성되기 전에 자식 프래그먼트를 추가하려고 해서..?
                // 해결 : getChildFragmentManager() 를 전역변수로 둠..?
                // 출처 : https://stackoverflow.com/questions/43562394/fragment-has-not-been-attached-yet

                // 기존 코드 1. getChildFragmentManager().beginTransaction().add(R.id.random_memo_frame, new RandomMemoFragment(randomNum)).commit();
                // 에러발생  Fatal Exception: java.lang.illegalStateException Can not perform this action after onSaveInstanceState
                // 원인 : Fragment 를 생성할 때, commit() 메서드를 호출하는 시점은 Activity 가 상태를 저장하기 전에 이루어져야 하는데, Activity 의 상태 저장 후에 이루어졌기 때문
                // 해결 : Activity 가 상태를 저장하고 난 후에 commit()를 하기 위해서는 commitAllowingStateLoss() 메서드를 이용한다.
                // 출처: https://eso0609.tistory.com/69

            }
        };

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            // 랜덤으로 사용자가 작성한 메모를 보여주기 위한 랜덤 숫자를 생성하는 스레드에서
            // 생성할 숫자의 범위를 지정하기 위해서 저장되어 있는 메모의 개수를 알아야 한다.
            // 저장되어 있는 메모의 개수를 파악하기 위해 memoInfo 파일에 저장된 memoList 문자열을 가져온다.
            // 문자열의 형태로는 저장된 메모의 개수를 알 수 없기 때문에 JsonArray 형식으로 변환하여 길이를 구한다.
            SharedPreferences memoInfo = view.getContext().getSharedPreferences("memoInfo", Context.MODE_PRIVATE);
            String memoListString = memoInfo.getString("memoList", null);
            if(memoListString != null)
            {
                JSONArray jsonArray = new JSONArray(memoListString);
                length = jsonArray.length();

                Log.d(TAG, "HomeFragment 메모 length :" + length);
            }


            // 문제 :  if(thread == null && length > 0) 조건에서 다른 프래그먼트에서 HomeFragment 로 돌아왔을 때
            //        스레드에 의해서 랜덤메모 프래그먼트가 생성될때까지 화면에 나타나지 않는 문제 발생
            // 원인 : 스레드가 시작되어야만 핸들러에서 랜덤메모 프래그먼트를 생성하기 때문
            // 해결 : 저장된 메모가 존재하면 일단 랜덤메모 프래그먼트를 생성한 다음에 스레드를 시작
            if(length > 0)
            {
                // 저장된 메모가 1개 이상이면 랜덤메모 프래그먼트를 생성한다.
                fragmentManager.beginTransaction().replace(R.id.random_memo_frame, new RandomMemoFragment(randomNum)).commitAllowingStateLoss();

                if(thread == null)
                {
                    // 랜덤메모 스레드 시작
                    // 사용자가 작성한 메모를 랜덤으로 선정해 3초 간격으로 화면에 보여주기 위해서 랜덤 숫자를 생성하는 스레드이다.
                    MemoThread memoThread = new MemoThread();
                    thread = new Thread(memoThread);
                    thread.start();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // To Read / Reading / Read 버튼을 클릭하면 해당하는 리스트를 보여줄 수 있도록 클릭이벤트를 등록한다.
        button_to_read.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getChildFragmentManager().beginTransaction().replace(R.id.home_frame, new ToReadFragment()).commit();
            }
        });

        button_reading.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getChildFragmentManager().beginTransaction().replace(R.id.home_frame, new ReadingFragment()).commit();
            }
        });

        button_read.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getChildFragmentManager().beginTransaction().replace(R.id.home_frame, new ReadFragment()).commit();
            }
        });

        // 검색창에 키워드를 입력하고 검색 버튼을 누르면 검색 결과화면으로 이동한다.
        searchView_search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                intent = new Intent(getContext(), SearchBookActivity.class);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 프래그먼트가 화면에 안보일 때 스레드가 null 이 아니라면 스레드를 멈춘다.
        // 화면이 다시 보일 때 시작하는 스레드가 중복되어 실행되지 않도록 스레드를 null 로 초기환한다.
        if(thread !=null)
        {
            thread.interrupt();
            thread = null;
        }
    }
}

