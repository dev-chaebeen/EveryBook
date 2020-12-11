package com.example.everybooks;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.everybooks.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

public class RandomMemoFragment extends Fragment
{
    // 뷰 요소 선언
    ImageView imageView_img;
    TextView textView_memo_text;

    String img;
    String memoText;
    int bookId;
    int randomNum;

    final String TAG = "테스트";

    private View view;

    public RandomMemoFragment(){}

    public RandomMemoFragment(int randomNum)
    {
        this.randomNum = randomNum;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_random_memo, container, false);

        Log.d(TAG,"RandomMemoFragment onCreateView, 생성자로 받은 randomNum: " + randomNum);

        // 뷰 요소 초기화
        imageView_img = view.findViewById(R.id.img);
        textView_memo_text = view.findViewById(R.id.memo_text);

        // 랜덤메모 데이터 가져오기
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


        return view;
    }
}
