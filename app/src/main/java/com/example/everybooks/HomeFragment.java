package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    int returnNum;
    int length;
    int stopNum;

    Handler memoHandler;
    Thread thread;

    FragmentManager fragmentManager;

    String memoOrder;
    int memoInterval;

    SharedPreferences memoInfo;
    SharedPreferences.Editor editor;


    // 랜덤 메모 스레드
    // 사용자가 작성한 메모를 일정한 시간 간격마다 랜덤으로 보여주기 위해서
    // 사용자가 작성한 메모 개수 이내의 랜덤 숫자를 n 초마다 발생시키는 스레드이다.
    class RandomMemoThread implements Runnable {
        boolean running = false;

        public void run() {
            running = true;
            while (running)
            {

                Message message = memoHandler.obtainMessage();

                    Random random = new Random();
                    returnNum = random.nextInt(length);
                    message.arg1 = returnNum;
                    memoHandler.sendMessage(message);

                try {
                    Thread.sleep(memoInterval*1000);
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    // 최신 순 메모 스레드
    // 사용자가 작성한 메모를 일정한 시간 간격마다 최신순으로 보여주기 위해서
    // 사용자가 작성한 메모의 개수부터 1씩 작아지는 수를 n 초마다 발생시키는 스레드이다.
    class LatestMemoThread implements Runnable {
        boolean running = false;

        public void run() {
            running = true;

            if(stopNum == -1)
            {
                returnNum = length;
            }
            else
            {
                returnNum = stopNum;
            }

            while (running)
            {
                Message message = memoHandler.obtainMessage();

                    message.arg1 = returnNum;
                    memoHandler.sendMessage(message);

                    if(returnNum == 0)
                    {
                        returnNum = length;
                    }
                    returnNum--;

                try {
                    Thread.sleep(memoInterval*1000);
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    // 작성순 메모 스레드
    // 사용자가 작성한 메모를 일정한 시간 간격마다 작성한 순서대로 보여주기 위해서
    // 0부터 사용자가 작성한 메모의 개수 - 1 까지 n 초마다 발생시키는 스레드이다.
    // -1 하는 이유는 사용자가 작성한 첫번째 메모는 0번째에 위치하기 때문에
    // 마지막 메모를 가져오려면 개수 -1 로 접근해야한다.

    class CreateMemoThread implements Runnable {
        boolean running = false;

        public void run() {
            running = true;


            if(stopNum == -1)
            {
                returnNum = 0;
            }
            else
            {
                returnNum = stopNum;
            }

            while (running)
            {
                Message message = memoHandler.obtainMessage();


                if(returnNum == length)
                {
                    returnNum = 0;
                }

                message.arg1 = returnNum;
                memoHandler.sendMessage(message);

                returnNum++;

                try {
                    Thread.sleep(memoInterval*1000);
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

        memoInfo = view.getContext().getSharedPreferences("memoInfo", Context.MODE_PRIVATE);
        memoOrder = memoInfo.getString("memoOrder", "random");
        memoInterval = Integer.parseInt(memoInfo.getString("memoInterval", "3"));

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
            // 랜덤으로 사용자가 작성한 메모를 보여주기 위한 숫자를 생성하는 스레드에서
            // 생성할 숫자의 범위를 지정하기 위해서 저장되어 있는 메모의 개수를 알아야 한다.
            // 저장되어 있는 메모의 개수를 파악하기 위해 memoInfo 파일에 저장된 memoList 문자열을 가져온다.
            // 문자열의 형태로는 저장된 메모의 개수를 알 수 없기 때문에 JsonArray 형식으로 변환하여 길이를 구한다.
            memoInfo = view.getContext().getSharedPreferences("memoInfo", Context.MODE_PRIVATE);
            editor = memoInfo.edit();

            String memoListString = memoInfo.getString("memoList", null);
            if(memoListString != null)
            {
                JSONArray jsonArray = new JSONArray(memoListString);
                length = jsonArray.length();

                editor.putInt("memoLength", length);
                editor.commit();

                Log.d(TAG, "HomeFragment 메모 length :" + length);
            }


            // 다시 페이지에 돌아왔을 때 이전에 보여주던 메모에서 이어서 보여주기 위해 stopNum 에 저장해둔 수를 가져온다.
            stopNum = memoInfo.getInt("stopNum", -1);

            // 문제 :  if(thread == null && length > 0) 조건에서 다른 프래그먼트에서 HomeFragment 로 돌아왔을 때
            //        스레드에 의해서 랜덤메모 프래그먼트가 생성될때까지 화면에 나타나지 않는 문제 발생
            // 원인 : 스레드가 시작되어야만 핸들러에서 랜덤메모 프래그먼트를 생성하기 때문
            // 해결 : 저장된 메모가 존재하면 일단 랜덤메모 프래그먼트를 생성한 다음에 스레드를 시작
            if(length > 0)
            {
                // 저장된 메모가 1개 이상이면 랜덤메모 프래그먼트를 생성한다.
                fragmentManager.beginTransaction().replace(R.id.random_memo_frame, new RandomMemoFragment(returnNum)).commitAllowingStateLoss();

                if(thread == null && length >1)
                {
                    // thread 가 null 이고 저장된 메모가 2개일 때부터 랜덤메모 스레드 시작
                    // 저장된 메모가 1개라면 스레드를 이용해 바꿔줄 필요가 없기 때문이다.
                    // 사용자가 작성한 메모를 랜덤으로 선정해 3초 간격으로 화면에 보여주기 위해서 랜덤 숫자를 생성하는 스레드이다.

                    if(memoOrder.equals("random"))
                    {
                        RandomMemoThread randomMemoThread = new RandomMemoThread();
                        thread = new Thread(randomMemoThread);
                        thread.start();
                    }
                    else if(memoOrder.equals("latest"))
                    {
                        LatestMemoThread latestMemoThread = new LatestMemoThread();
                        thread = new Thread(latestMemoThread);
                        thread.start();
                    }
                    else if(memoOrder.equals("create"))
                    {
                        CreateMemoThread createMemoThread = new CreateMemoThread();
                        thread = new Thread(createMemoThread);
                        thread.start();
                    }
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

    @Override
    public void onPause()
    {
        super.onPause();
        // 프래그먼트가 화면에 안보일 때 스레드가 null 이 아니라면 스레드를 멈춘다.
        // 화면이 다시 보일 때 시작하는 스레드가 중복되어 실행되지 않도록 스레드를 null 로 초기환한다.
        if(thread !=null)
        {
            thread.interrupt();
            thread = null;
        }

        // 멈췄을 때 보여준 메모의 위치를 저장한다. 다시 화면에 돌아왔을 때 그 이후의 메모부터 보여주기 위해서
        stopNum = returnNum;
        Log.d(TAG, "onPause() stopNum : " + stopNum);

        editor.putInt("stopNum", stopNum);
        editor.commit();

    }
}

