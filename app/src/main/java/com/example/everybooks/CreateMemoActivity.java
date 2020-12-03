package com.example.everybooks;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateMemoActivity extends AppCompatActivity {
    
    // 뷰 요소 선언
    TextView textView_save;
    TextView textView_cancel;
    TextView textView_title;
    EditText editText_memo_text;

    View.OnClickListener click;

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

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.save:
                        // Save 을 클릭하면 작성된 메모 데이터를 담아서 메모 리스트에 추가한다.
                        int bookId = getIntent().getIntExtra("bookId",-1);
                        MemoAdapter memoAdapter = new MemoAdapter();
                        memoAdapter.addItem(bookId, editText_memo_text.getText().toString());
                        memoAdapter.notifyDataSetChanged();
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
