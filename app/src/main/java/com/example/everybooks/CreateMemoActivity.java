package com.example.everybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateMemoActivity extends AppCompatActivity {
    
    // 뷰 요소 선언
    TextView textView_save;     // save
    TextView textView_cancel;   // cancel
    TextView textView_title;    // 책 제목
    EditText editText_memo_text;// 메모 내용

    View.OnClickListener click;

    Intent intent;

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
        // 뷰 생성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);

        // 뷰 요소 초기화
        textView_save = findViewById(R.id.save);
        textView_cancel = findViewById(R.id.cancel);
        textView_title = findViewById(R.id.title);
        editText_memo_text = findViewById(R.id.memo_text);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.save:
                        // 작성 버튼 눌렀을 때 bookId 를 수신해서 메모 리스트에 추가한다.
                        int bookId = getIntent().getIntExtra("bookId",-1);

                        MemoAdapter memoAdapter = new MemoAdapter();
                        memoAdapter.addItem(bookId, editText_memo_text.getText().toString());
                        memoAdapter.notifyDataSetChanged(); // 변경되었음을 어댑터에 알려준다.

                        finish();

                        break;

                    case R.id.cancel:
                        // 작성하던 내용을 지우고 이전 화면으로 돌아간다.
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
