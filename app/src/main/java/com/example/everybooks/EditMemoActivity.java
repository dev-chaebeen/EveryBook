package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Iterator;

public class EditMemoActivity extends AppCompatActivity
{

    // 뷰 요소 선언
    TextView textView_save;     // save
    TextView textView_cancel;   // cancel
    TextView textView_title;    // 책 제목
    EditText editText_memo_text;// 메모 내용

    Intent intent;
    View.OnClickListener click;

    // 인텐트로 전달받는 데이터
    String title;
    String memoText;
    int memoId;

    @Override
    protected void onStart() {
        super.onStart();

       /* if(HomeToReadActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 구성
        setContentView(R.layout.activity_edit_memo);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_cancel = findViewById(R.id.cancel);
        textView_title = findViewById(R.id.title);
        editText_memo_text = findViewById(R.id.memo_text);


        // 인텐트로 전달받은 데이터 수신
        memoId = getIntent().getIntExtra("memoId", 0);
        title = getIntent().getStringExtra("title");
        memoText = getIntent().getStringExtra("memoText");

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:

                        // save를 클릭하면 인텐트로 전달받은 memoId 가진 메모 객체 memoText 만 수정한다.
                     

                        //  해당하는 아이디를 가진 메모 객체를 찾아서 메모 내용을 변경해준다
                        Memo memo = MemoAdapter.memoList.get(memoId);
                        memo.setMemoText(editText_memo_text.getText().toString());

                        MemoAdapter memoAdapter = new MemoAdapter();
                        memoAdapter.notifyDataSetChanged();
                        AllMemoAdapter allmemoAdapter = new AllMemoAdapter();
                        allmemoAdapter.notifyDataSetChanged();// 어댑터에게 메모 내용이 변경된걸 알려준다.

                        finish(); // 현재 액티비티 종료


                        break;
                    case R.id.cancel:
                        // 취소하면
                        finish();

                        break;
                }
            }
        };

        // 각 요소 클릭하면 동작 실행
        textView_save.setOnClickListener(click);
        textView_cancel.setOnClickListener(click);

    }// end onCreate

    @Override
    protected void onResume() {
        super.onResume();

        // 배치하기
        textView_title.setText(title);
        editText_memo_text.setText(memoText);

    }// end onResume()
}
