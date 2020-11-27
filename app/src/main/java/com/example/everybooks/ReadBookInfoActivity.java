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
import androidx.cardview.widget.CardView;

public class ReadBookInfoActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;
    ListView listView;
    MemoAdapter memoAdapter;

    // 뷰 요소 선언
    TextView textView_edit; // Edit
    Button button_memo;     // MEMO 버튼 
    Button button_delete;   // DELETE 버튼 

    TextView textView_title;        // 책 제목
    ImageView imageView_img;        // 책 표지
    TextView textView_writer;       // 작가
    TextView textView_publisher;    // 출판사
    TextView textView_publish_date; // 출판일
    TextView textView_start_date;   // 독서 시작일 
    TextView textView_end_date;     // 독서 마감일 
    TextView textView_time;         // 독서 시간 

    RatingBar ratingBar_rate;       // 별점 

    TextView textView_memo_text;    // 메모 내용
    TextView textView_memo_date;    // 메모 날짜

    // 인텐트로 수신하는 데이터
    int position;

    @Override
    protected void onStart() {
        super.onStart();

        /*if(HomeToReadActivity.isLogin == false)   // 로그아웃된 상태라면
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
        position = getIntent().getIntExtra("position",-1);

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_memo:
                        // MEMO 버튼을 클릭하면 메모 작성 화면으로 전환한다
                        intent = new Intent(getApplicationContext(), CreateMemoActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.btn_delete:
                        // 책 삭제하고 이전 화면으로 돌아가기(현재 액티비티 finish())
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
                                        // 취소 클릭했을 때
                                        Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();

                                        finish();
                                    }
                                });

                        builder.show();


                        break;

                    case R.id.edit :
                        // Edit 을 클릭하면 책 정보 수정화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), EditBookInfoActivity.class);

                        // 데이터를 담고

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

    }// onCreate()

    @Override
    protected void onResume() {
        super.onResume();

        //리스트뷰에 어댑터를 붙여서 사용자에게 내용이 보이도록 한다.
        listView.setAdapter(memoAdapter);

        // 각 메모를 클릭하면 책 제목(추후 bookId 로 대체), 메모아이디, 메모 내용  데이터를 담아서
        // 메모 편집 화면으로 이동한다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                intent = new Intent(getApplicationContext(), EditMemoActivity.class);

                // todo 추후에 bookId 로 해당하는 책 제목 가져오도록 고치기
                intent.putExtra("title", textView_title.getText());
                intent.putExtra("memoId", position);  // 선택한 아이템의 아이디 얻어오기

                Memo memo = (Memo)memoAdapter.getItem(position);
                intent.putExtra("memoText", memo.getMemoText());

                startActivity(intent);
            }
        });

        // 각 메모를 길게 클릭하면 삭제하겠냐고 확인하는 문구가 뜬다.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadBookInfoActivity.this);
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

                return true; // 롱클릭 이벤트 이후 클릭이벤트 발생 xx
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();



        // 당일 날짜와 시간 받아오기
        //  SimpleDateFormat date = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss");
        // String dateStr = date.format(date);

    }
}
