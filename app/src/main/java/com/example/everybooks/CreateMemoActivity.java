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
import com.example.everybooks.data.Util;

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

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, second);

                        Log.d(TAG, "TimeRecordActivity, 형식 바꿀 때 시간 " + hour + ":" + minute + ":" + second);

                        Util util = new Util();
                        String today =  year + "." + month + "." + date + " " + util.stringFromCalendar(calendar);

                        try
                        {
                            // JsonArray 에 추가하기 위해서
                            // 입력받은 메모 정보를 jsonObject 에 저장한다.
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("memoId", memoId);
                            jsonObject.put("bookId", bookId);
                            jsonObject.put("memoText", editText_memo_text.getText().toString());
                            jsonObject.put("memoDate", today);

                            // 메모를 구분하기 위해 저장된 메모의 memoId 가 겹치지 않도록 memoInfo 에 저장된 memoId의 값을 1 증가시킨다.
                            SharedPreferences.Editor editor = memoInfo.edit();
                            editor.putInt("memoId", memoId + 1);
                            editor.commit();

                            //Log.d(TAG, "1증가시키고 저장해둔 memoId" + memoInfo.getInt("memoId",0));

                            // 기존에 저장된 jsonArray 에 저장하기 위해서
                            // SharedPreference bookInfo 파일에서 "memoList" 키로 저장된 String 값을 불러온다.
                            memoListString = memoInfo.getString("memoList", null);

                            // 저장된 문자열이 있을 때는 jsonArray 형태로 변환한다.
                            if(memoListString != null)
                            {
                                jsonArray = new JSONArray(memoListString);
                                //Log.d(TAG, "저장되어 있던 JsonArray 길이 : " + jsonArray.length());
                                //Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());
                            }
                            else
                            {
                                // 저장된 문자열이 없을 때는 JsonArray 객체를 생성한다.
                                jsonArray = new JSONArray();
                                //Log.d(TAG, "하나 추가한 뒤 JsonArray 길이 : " + jsonArray.length());
                            }

                            // JsonArray 에 jsonObject 를 추가하고 문자열 형태로 바꾼 뒤
                            // "memoList" 키를 이용해서 값을 저장한다.
                            jsonArray.put(jsonObject);
                            memoListString = jsonArray.toString();
                            editor.putString("memoList", memoListString);
                            editor.commit();

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
