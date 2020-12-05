package com.example.everybooks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Memo;

import org.json.JSONArray;
import org.json.JSONObject;

public class EditMemoActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_cancel;
    TextView textView_title;
    EditText editText_memo_text;

    View.OnClickListener click;

    // 인텐트로 전달받는 데이터
    String title;
    String memoText;
    //int position;
    int memoId;
    final String TAG = "테스트";

    Memo memo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 구성
        setContentView(R.layout.activity_edit_memo);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_cancel = findViewById(R.id.cancel);
        textView_title = findViewById(R.id.title);
        editText_memo_text = findViewById(R.id.memo_text);

        // 인텐트로 전달받은 데이터 수신
        //position = getIntent().getIntExtra("position", -1);
        memoId = getIntent().getIntExtra("memoId", -1);
        title = getIntent().getStringExtra("title");
        memoText = getIntent().getStringExtra("memoText");

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.save:

                        // save 를 클릭하면 전달받은 memoId 에 해당하는 메모 객체를 찾아서 메모 내용을 변경해준다.
                        SharedPreferences memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                        String memoListString = memoInfo.getString("memoList", null);

                        try
                        {
                            JSONArray jsonArray = new JSONArray(memoListString);
                            for (int i = 0; i < jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if(jsonObject.getInt("memoId") == memoId)
                                {
                                    jsonObject.put("memoText", editText_memo_text.getText().toString());
                                }
                            }

                            // test ok
                            Log.d(TAG," 메모 수정 후 jsonArray.toString: " + jsonArray.toString());

                            SharedPreferences.Editor editor = memoInfo.edit();
                            editor.putString("memoList", jsonArray.toString());
                            editor.commit();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }

                        finish();


                        // 기존
                        // save를 클릭하면 인텐트로 전달받은 position 에 해당하는 아이디를 가진 메모 객체를 찾아서 메모 내용을 변경해준다
                        /*
                        MemoAdapter memoAdapter = new MemoAdapter();
                        memo = (Memo)memoAdapter.getItem(position);
                        memo.setMemoText(editText_memo_text.getText().toString());
                        memoAdapter.notifyDataSetChanged();

                        AllMemoAdapter allmemoAdapter = new AllMemoAdapter();
                        allmemoAdapter.notifyDataSetChanged();
                        */

                        break;

                    case R.id.cancel:
                        finish();
                        break;
                }
            }
        };

        // 각 요소 클릭하면 동작 실행
        textView_save.setOnClickListener(click);
        textView_cancel.setOnClickListener(click);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 배치하기
        textView_title.setText(title);
        editText_memo_text.setText(memoText);
    }

}
