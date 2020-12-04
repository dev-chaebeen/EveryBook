package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Memo;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadingBookInfoActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;
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
    TextView textView_time;

    ListView listView;
    TextView textView_memo_text;
    TextView textView_memo_date;

    String title;
    String writer;
    String publisher;
    String publishDate;
    String startDate;
    int position;
    int bookId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_reading_book_info);

        // 메모 리스트뷰 어댑터 객체 생성
        memoAdapter = new MemoAdapter();

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
        textView_time = findViewById(R.id.time);

        listView = findViewById(R.id.reading_memo_list);
        textView_memo_text = findViewById(R.id.memo_text);
        textView_memo_date = findViewById(R.id.memo_date);

        // 데이터 받아오기
        bookId = getIntent().getIntExtra("bookId",-1);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_memo:
                        // 메모 버튼 클릭하면 어떤 책에 해당하는 메모인지 식별하기 위해서 bookId 데이터를 인텐트에 담아서 메모 생성 화면으로 전환한다.
                        int bookId = getIntent().getIntExtra("bookId",-1);
                        intent = new Intent( getApplicationContext(), CreateMemoActivity.class);
                        intent.putExtra("bookId", bookId);
                        startActivity(intent);
                        break;

                    case R.id.btn_delete:
                        // Delete 를 클릭하면 책을 삭제하고 이전 화면으로 돌아가기
                        // 책 삭제할 때 관련된 메모도 삭제해야한다.
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReadingBookInfoActivity.this);
                        builder.setMessage("책을 삭제하시겠습니까?\n 메모도 함께 삭제됩니다. ");
                        builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // 확인 클릭했을 때 해당 책을 삭제한다.
                                    ReadingBookAdapter adapter = new ReadingBookAdapter();
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
                                    // 취소 클릭했을 때
                                    Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();
                                }
                            });

                        builder.show();
                        break;

                    case R.id.edit :
                        // Edit 을 클릭하면 책 정보를 담고 책 편집 화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), EditBookInfoActivity.class);
                        // img
                        intent.putExtra("title", title);
                        intent.putExtra("writer", writer);
                        intent.putExtra("publisher", publisher);
                        intent.putExtra("publishDate", publishDate);

                        // 책 상태 구분하기 위해서 데이터를 담아간다
                        intent.putExtra("position", position);
                        intent.putExtra("state", "reading");

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

        // 인텐트로 전달받은 bookId 에 해당하는 데이터를 가져와서 사용자에게 보여주기 위해서
        // 저장되어있는 bookList 문자열을 받아와서 JsonArray 형태로 변환한다.
        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
        String bookLsitString =bookInfo.getString("bookList", null);

        if(bookLsitString != null)
        {
            try
            {
                JSONArray jsonArray = new JSONArray(bookLsitString);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    if(bookId == jsonObject.getInt("bookId"))
                    {
                        title = jsonObject.getString("title");
                        writer = jsonObject.getString("writer");
                        publisher = jsonObject.getString("publisher");
                        publishDate = jsonObject.getString("publishDate");
                        startDate = jsonObject.getString("startDate");
                    }
                }
            }
            catch (Exception e)
            {

            }
        }

        textView_title.setText(title);
        textView_writer.setText(writer);
        textView_publisher.setText(publisher);
        textView_publish_date.setText(publishDate);
        textView_start_date.setText(startDate);

        //리스트뷰에 어댑터를 붙여서 사용자에게 메모가 보이도록 한다.
        listView.setAdapter(memoAdapter);

        // 각 메모를 클릭하면 책 제목(추후 bookId 로 대체), 메모아이디, 메모 내용  데이터를 담아서
        // 메모 편집 화면으로 이동한다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                intent = new Intent(getApplicationContext(), EditMemoActivity.class);

                Memo memo = (Memo)memoAdapter.getItem(position);
                intent.putExtra("title", textView_title.getText());
                intent.putExtra("memoId", memo.getMemoId());  // 선택한 아이템의 아이디 얻어오기
                intent.putExtra("position", position);
                intent.putExtra("memoText", memo.getMemoText());

                startActivity(intent);
            }
        });

        // 각 메모를 길게 클릭하면 삭제하겠냐고 확인하는 문구가 뜬다.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadingBookInfoActivity.this);
                builder.setMessage("메모를 삭제하시겠습니까?.");
                builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // 확인 클릭했을 때 해당 메모 삭제한다.
                            MemoAdapter.memoList.remove(position);

                            // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                            memoAdapter.notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });
                builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // 취소 클릭했을 때
                            Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();
                        }
                    });

                builder.show();
                return true; // 롱클릭 이벤트 이후 클릭이벤트 발생하지 않도록 true 반환 
            }
        });
    }
}
