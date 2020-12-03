package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Memo;

public class ReadBookInfoActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;
    ListView listView;
    MemoAdapter memoAdapter;

    // 뷰 요소 선언
    TextView textView_edit;
    Button button_memo;
    Button button_delete;

    TextView textView_title;
    ImageView imageView_img;
    TextView textView_writer;
    TextView textView_publisher;
    TextView textView_publish_date;
    TextView textView_start_date;
    TextView textView_end_date;
    TextView textView_time;

    RatingBar ratingBar_rate;

    TextView textView_memo_text;
    TextView textView_memo_date;

    // 인텐트로 수신하는 데이터
    int position;
    int bookId;
    int starNum;
    String title;
    String writer;
    String publisher;
    String publishDate;
    String startDate;
    String endDate;
    String state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read_book_info);

        // 뷰 요소 초기화
        textView_edit = findViewById(R.id.edit);
        button_memo = findViewById(R.id.btn_memo);
        button_delete = findViewById(R.id.btn_delete);

        textView_title = findViewById(R.id.title);
        imageView_img = findViewById(R.id.img);
        textView_writer = findViewById(R.id.writer);
        textView_publisher = findViewById(R.id.publisher);
        textView_publish_date = findViewById(R.id.publish_date);
        textView_start_date = findViewById(R.id.start_date);
        textView_end_date = findViewById(R.id.end_date);
        textView_time = findViewById(R.id.time);
        ratingBar_rate = findViewById(R.id.rate);

        listView = findViewById(R.id.read_memo_list);
        textView_memo_text = findViewById(R.id.memo_text);
        textView_memo_date = findViewById(R.id.memo_date);

        // 메모 리스트뷰 어댑터 객체 생성
        memoAdapter = new MemoAdapter();

        // 데이터 받아오기
        position = getIntent().getIntExtra("position",-1);
        bookId = getIntent().getIntExtra("bookId", -1);
        title = getIntent().getStringExtra("title");
        writer = getIntent().getStringExtra("writer");
        publisher = getIntent().getStringExtra("publisher");
        publishDate = getIntent().getStringExtra("publishDate");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        starNum = getIntent().getIntExtra("starNum",-1);
        state = getIntent().getStringExtra("state");

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_memo:
                        // MEMO 버튼을 클릭하면 메모 작성 화면으로 전환한다
                        intent = new Intent(getApplicationContext(), CreateMemoActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.btn_delete:
                        // 책을 삭제하고 이전 화면으로 돌아간다.
                        // 책 삭제할 때 관련된 메모도 삭제해야한다.
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReadBookInfoActivity.this);
                        builder.setMessage("책을 삭제하시겠습니까?\n 메모도 함께 삭제됩니다. ");
                        builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // 확인 클릭했을 때 해당 책을 삭제한다.
                                    ReadBookAdapter adapter = new ReadBookAdapter();
                                    adapter.removeItem(position);

                                    dialog.dismiss();

                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                    finish();

                                }
                            });

                        builder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();
                                }
                            });

                        builder.show();
                        break;

                    case R.id.edit :
                        // Edit 을 클릭하면 책 정보를 담고 책 정보 수정화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), EditBookInfoActivity.class);

                        // img
                        intent.putExtra("title", title);
                        intent.putExtra("writer", writer);
                        intent.putExtra("publisher", publisher);
                        intent.putExtra("publishDate", publishDate);
                        intent.putExtra("position", position);
                        intent.putExtra("state", state);
                        startActivity(intent);
                        break;
                }
            }
        };

        // 각 요소가 클릭되면
        button_memo.setOnClickListener(click);
        button_delete.setOnClickListener(click);
        textView_edit.setOnClickListener(click);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 수신한 책 데이터 배치하기
        textView_title.setText(title);
        textView_writer.setText(writer);
        textView_publisher.setText(publisher);
        textView_publish_date.setText(publishDate);
        textView_start_date.setText(startDate);
        textView_end_date.setText(endDate);
        ratingBar_rate.setRating(starNum);
        listView.setAdapter(memoAdapter);

        // 각 메모를 클릭하면 책 제목, 메모아이디, 메모 내용 데이터를 인텐트에 담아서 메모 편집 화면으로 이동한다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                intent = new Intent(getApplicationContext(), EditMemoActivity.class);
                intent.putExtra("title", textView_title.getText());
                intent.putExtra("position", position);
                Memo memo = (Memo)memoAdapter.getItem(position);
                intent.putExtra("memoId", memo.getMemoId());
                intent.putExtra("memoText", memo.getMemoText());

                startActivity(intent);
            }
        });

        // 각 메모를 길게 클릭하면 삭제하겠냐고 확인하는 문구가 뜬다.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadBookInfoActivity.this);
                builder.setMessage("메모를 삭제하시겠습니까?.");
                builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //MemoAdapter.memoList.remove(position);
                        memoAdapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();

                return true; // 롱클릭 이벤트 이후 클릭이벤트 발생하지 않도록 true 반환
            }
        });
    }
}
