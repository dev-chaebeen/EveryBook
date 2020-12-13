package com.example.everybooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MemoSettingActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_order_random;
    TextView textView_order_latest;
    TextView textView_order_create;

    LinearLayout linearLayout_order_random;
    LinearLayout linearLayout_order_latest;
    LinearLayout linearLayout_order_create;
    EditText editText_time_interval;

    String memoOrder;
    String memoInterval;

    SharedPreferences memoInfo;
    SharedPreferences.Editor editor;

    final String TAG = "테스트";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_memo_setting);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_order_random = findViewById(R.id.text_order_random);
        textView_order_latest = findViewById(R.id.text_order_latest);
        textView_order_create = findViewById(R.id.text_order_create);

        linearLayout_order_random = findViewById(R.id.order_random);
        linearLayout_order_latest = findViewById(R.id.order_latest);
        linearLayout_order_create = findViewById(R.id.order_create);

        editText_time_interval = findViewById(R.id.time_interval);

        memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
        editor = memoInfo.edit();

        // 클릭 이벤트 등록
        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:
                        // save 버튼을 클릭하면 입력받은 메모 변경 순서와 초를 SharedPreferences memoInfo 파일에 저장한다.
                        editor.putString("memoOrder", memoOrder);
                        editor.putString("memoInterval", editText_time_interval.getText().toString());
                        editor.commit();

                        finish();
                        Log.d(TAG, "MemoSettingActivity, 저장하는 값 :" + memoOrder + memoInterval);
                        break;

                    case R.id.order_random:

                        // 랜덤순을 클릭하면 색상을 바꾼다.
                        memoOrder = "random";
                        textView_order_random.setTextColor(Color.parseColor("#414D41"));
                        textView_order_random.setTypeface(null, Typeface.BOLD);
                        textView_order_latest.setTextColor(Color.parseColor("#BDBDBD"));
                        textView_order_latest.setTypeface(null, Typeface.NORMAL);
                        textView_order_create.setTextColor(Color.parseColor("#BDBDBD"));
                        textView_order_create.setTypeface(null, Typeface.NORMAL);
                        break;

                    case R.id.order_latest:
                        memoOrder = "latest";
                        textView_order_random.setTextColor(Color.parseColor("#BDBDBD"));
                        textView_order_random.setTypeface(null, Typeface.NORMAL);
                        textView_order_latest.setTextColor(Color.parseColor("#414D41"));
                        textView_order_latest.setTypeface(null, Typeface.BOLD);
                        textView_order_create.setTextColor(Color.parseColor("#BDBDBD"));
                        textView_order_create.setTypeface(null, Typeface.NORMAL);
                        break;

                    case R.id.order_create:
                        memoOrder = "create";
                        textView_order_random.setTextColor(Color.parseColor("#BDBDBD"));
                        textView_order_random.setTypeface(null, Typeface.NORMAL);
                        textView_order_latest.setTextColor(Color.parseColor("#BDBDBD"));
                        textView_order_latest.setTypeface(null, Typeface.NORMAL);
                        textView_order_create.setTextColor(Color.parseColor("#414D41"));
                        textView_order_create.setTypeface(null, Typeface.BOLD);
                        break;
                }
            }
        };

        // 각 요소가 클릭되면
        textView_save.setOnClickListener(click);
        linearLayout_order_random.setOnClickListener(click);
        linearLayout_order_latest.setOnClickListener(click);
        linearLayout_order_create.setOnClickListener(click);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 액티비티가 전면에 보일 때마다 저장되어있는 정보를 보여준다.

        // SharedPreference 에 저장되어 있는 정보를 가져온다.
        // 가져오는 정보는 변경되는 메모 순서와 시간 간격이다.
        memoOrder = memoInfo.getString("memoOrder", "random");
        memoInterval = memoInfo.getString("memoInterval", "3");

        Log.d(TAG, "화면 구성 시 가져온 값 " + memoOrder + memoInterval);
        switch(memoOrder){
          case "random":
              textView_order_random.setTextColor(Color.parseColor("#414D41"));
              textView_order_random.setTypeface(null, Typeface.BOLD);
              textView_order_latest.setTextColor(Color.parseColor("#BDBDBD"));
              textView_order_create.setTextColor(Color.parseColor("#BDBDBD"));
            break;

            case "latest":
                textView_order_random.setTextColor(Color.parseColor("#BDBDBD"));
                textView_order_latest.setTextColor(Color.parseColor("#414D41"));
                textView_order_latest.setTypeface(null, Typeface.BOLD);
                textView_order_create.setTextColor(Color.parseColor("#BDBDBD"));
                break;

            case "create":
                textView_order_random.setTextColor(Color.parseColor("#BDBDBD"));
                textView_order_latest.setTextColor(Color.parseColor("#BDBDBD"));
                textView_order_create.setTextColor(Color.parseColor("#414D41"));
                textView_order_create.setTypeface(null, Typeface.BOLD);
                break;
        }

        editText_time_interval.setText(memoInterval);
    }
}
