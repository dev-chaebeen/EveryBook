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

        // 뷰 요소 초기화
        imageView_img = view.findViewById(R.id.img);
        textView_memo_text = view.findViewById(R.id.memo_text);

        // 이 프래그먼트는 HomeFragment 에서 전달받은 수의 위치에 있는 메모 데이터를 뷰 요소에 배치하는 프래그먼트이다.
        // 배치하는 데이터는 메모가 적힌 책의 표지 이미지와 메모 내용이다.

        // 메모 데이터를 가져오기 위해서 memoInfo 파일의 memoList 문자열을 가져온다.
        // 특정한 위치에 있는 메모의 데이터를 가져오기 위해서 memoList 문자열을 JsonArray 형식으로 변환한다.
        // 전달받은 위치에 있는 jsonObject 를 가져온다.

        // 해당하는 jsonObject 에서 bookId 와 memoText 를 얻는다.
        // bookId 는 해당하는 책의 표지 이미지를 얻기 위해서 필요하고 각 메모는 어떤 책에 대한 메모인지 구분하기 위해 bookId 값을 가지고 있다.
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

        // 위에서 얻어낸 bookId 에 해당하는 책 표지 이미지를 얻기 위해서 bookInfo 파일의 bookList 문자열을 가져온다.
        // 문자열의 형태로는 객체에 접근할 수 없기 때문에 JsonArray 형태로 변환한다.
        // 해당하는 bookId 를 가진 jsonObject 에서 책의 표지 이미지 문자열을 얻어낸다.
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

        // 이미지의 경우 문자열로 저장되어 있으므로 비트맵 형식으로 바꿔준다.
        // Util 클래스는 문자열을 비트맵으로, 비트맵을 문자열로 변환해주는 메소드를 가진 클래스이다.
        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(img);

        // 얻어낸 데이터를 뷰 요소에 배치한다.
        textView_memo_text.setText(memoText);
        imageView_img.setImageBitmap(bitmap);

        return view;
    }
}
