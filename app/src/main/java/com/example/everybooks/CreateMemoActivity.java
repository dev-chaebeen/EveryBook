package com.example.everybooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Book;
import com.example.everybooks.data.Memo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class CreateMemoActivity extends AppCompatActivity {
    
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_cancel;
    TextView textView_title;
    EditText editText_memo_text;

    View.OnClickListener click;

    int bookId;
    int memoId;
    final String TAG = "테스트";
    String memoListString;
    JSONArray jsonArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        // 뷰 생성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_cancel = findViewById(R.id.cancel);
        textView_title = findViewById(R.id.title);
        editText_memo_text = findViewById(R.id.memo_text);

        // 인텐트로 전달받은 bookId 를 담는다
        bookId = getIntent().getIntExtra("bookId",-1);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.save:
                        // Save 을 클릭하면 작성된 메모 데이터를 담아서 메모 리스트에 추가한다.
                        // bookInfo 라는 SharedPreferences 파일에서 memoId 를 가져온다.
                        // 저장된 값이 존재하지 않는다면 0을 가져온다.
                        SharedPreferences memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                        memoId = memoInfo.getInt("memoId", 0);

                        //현재 연도, 월, 일을 메모 등록일에 저장한다.
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get ( cal.YEAR );
                        int month = cal.get ( cal.MONTH ) + 1 ;
                        int date = cal.get ( cal.DATE ) ;
                        int hour = cal.get(cal.HOUR_OF_DAY);
                        int minute = cal.get(cal.MINUTE);
                        int second = cal.get(cal.SECOND);

                        String today = year + "." + month + "." + date + " " +  hour + ":"+ minute + ":" + second;

                        // 입력받은 정보를 Memo 객체에 저장한다.
                        Memo memo = new Memo();
                        memo.setMemoId(memoId);
                        memo.setBookId(bookId);
                        memo.setMemoText(editText_memo_text.getText().toString());
                        memo.setMemoDate(today);

                        try
                        {
                            // json 객체에 입력받은 정보를 저장한다.
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("memoId", memo.getMemoId());
                            jsonObject.put("bookId", memo.getBookId());
                            jsonObject.put("memoText", memo.getMemoText());
                            jsonObject.put("memoDate", memo.getMemoDate());

                            // 메모를 구분하기 위해 저장된 메모의 memoId 가 겹치지 않도록 memoInfo 에 저장된 memoId의 값을 1 증가시킨다.
                            SharedPreferences.Editor editor = memoInfo.edit();
                            editor.putInt("memoId", memoId + 1);
                            editor.commit();

                            Log.d(TAG, "1증가시키고 저장해둔 memoId" + memoInfo.getInt("memoId",0));

                            // 기존에 저장된 jsonArray에 저장하기 위해서
                            // SharedPreference bookInfo 파일에서 "toReadBookLIst" 키로 저장된 String 값을 불러온다.
                            memoListString = memoInfo.getString("memoList", null);

                            // 저장된 값이 있을 때
                            if(memoListString != null)
                            {
                                jsonArray = new JSONArray(memoListString);
                                Log.d(TAG, "저장되어 있던 JsonArray 길이 : " + jsonArray.length());

                                jsonArray.put(jsonObject);

                                memoListString = jsonArray.toString();

                                editor.putString("memoList", memoListString);
                                editor.commit();

                                //Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());

                            }
                            else
                            {
                                // 처음 저장할 때
                                jsonArray = new JSONArray();
                                jsonArray.put(jsonObject);

                                memoListString = jsonArray.toString();
                                editor.putString("memoList", memoListString);
                                editor.commit();
                                //Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());
                            }

                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }

                        Log.d(TAG, "CreateMemoActivity , 모든 메모리스트  : " + memoListString);


                        finish();

                        break;

                    case R.id.cancel:
                        // Cancel 을 클릭하면 작성하던 내용을 지우고 이전 화면으로 돌아간다.
                        editText_memo_text.setText("");
                        finish();

                        break;
                }
            }
        };

        // 각 요소가 클릭되면 수행
        textView_save.setOnClickListener(click);
        textView_cancel.setOnClickListener(click);
    }


}
