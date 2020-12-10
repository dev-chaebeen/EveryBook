package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.everybooks.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

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

    ImageView imageView_img;
    TextView textView_title;
    TextView textView_memo_text;

    final String TAG = "테스트";
    String memoText;
    int bookId;
    String title;
    String img;
    int randomNum;

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
        imageView_img = view.findViewById(R.id.img);
        textView_memo_text = view.findViewById(R.id.memo_text);
        textView_title = view.findViewById(R.id.title);


        Log.d(TAG, "HomeFragment");


        // MainActivity 에서 보낸 랜덤 메모 데이터 받기
        Bundle bundle = this.getArguments();

        if (bundle != null)
        {
            randomNum = getArguments().getInt("randomNum");
            Log.d(TAG, "HomeFragment 전달받은 랜덤 : " + randomNum);
        }


        SharedPreferences memoInfo = view.getContext().getSharedPreferences("memoInfo", Context.MODE_PRIVATE);
        String memoListString = memoInfo.getString("memoList", null);
        try
        {
            JSONArray jsonArray = new JSONArray(memoListString);

            JSONObject jsonObject = (JSONObject) jsonArray.get(randomNum);
            memoText = jsonObject.getString("memoText");
            bookId = jsonObject.getInt("bookId");
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        // 메모에 저장되어 있는 bookId 에 해당하는 title, img 가져온다.
        SharedPreferences bookInfo = view.getContext().getSharedPreferences("bookInfo", Context.MODE_PRIVATE);
        String bookListString = bookInfo.getString("bookList", null);

        try
        {
            JSONArray jsonArray = new JSONArray(bookListString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(bookId == jsonObject.getInt("bookId"))
                {
                    img = jsonObject.getString("img");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        textView_memo_text.setText(memoText);
        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(img);
        imageView_img.setImageBitmap(bitmap);
        Log.d(TAG, "HomeFragment onCreateView ");

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // test 최초 한번만 나온다..
        /*// MainActivity 에서 보낸 랜덤 메모 데이터 받기
        Bundle bundle = this.getArguments();

        if (bundle != null)
        {
            randomNum = getArguments().getInt("randomNum");
            Log.d(TAG, "HomeFragment 전달받은 랜덤 : " + randomNum);
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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

}