package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditBookInfoActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;
    ToReadBookAdapter adapter;

    // 뷰 요소 선언
    TextView textView_edit;
    TextView textView_delete;

    ImageView ImageView_img;
    EditText editText_title;
    EditText editText_writer;
    EditText editText_publisher;
    EditText editText_publish_date;



    @Override
    protected void onStart() {
        super.onStart();

        /*if(MainActivity.isLogin == false)   // 로그아웃된 상태라면
        {
            // 안내메세지 보여주고 로그인 화면으로 전환한다.
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        // 뷰 생성
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_info);

        // 뷰 요소 초기화
        textView_edit = (TextView) findViewById(R.id.edit);
        textView_delete = findViewById(R.id.delete);

        ImageView_img = findViewById(R.id.img);
        editText_title = findViewById(R.id.title);
        editText_writer = findViewById(R.id.writer);
        editText_publisher = findViewById(R.id.publisher);
        editText_publish_date = findViewById(R.id.publish_date);


        // 처음 액티비티 생성될 때는 인텐트로 전달받은 데이터 보여주기
        ImageView_img.setImageResource(getIntent().getIntExtra("img",0));
        editText_title.setText(getIntent().getStringExtra("title"));
        editText_writer.setText(getIntent().getStringExtra("writer"));
        editText_publisher.setText(getIntent().getStringExtra("publisher"));
        editText_publish_date.setText(getIntent().getStringExtra("publishDate"));

        // 전달받은 아이템 아이디 저장
        int position = getIntent().getIntExtra("position",-1);

        adapter = new ToReadBookAdapter();

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.edit :

                        // todo 이미지 추가하기

                        // edit 클릭했을 때 입력받은 값들을 저장해서 그 값으로 수정해준다.

                        String title = editText_title.getText().toString();
                        String writer = editText_writer.getText().toString();
                        String publisher = editText_publisher.getText().toString();
                        String publishDate = editText_publish_date.getText().toString();

                        Book book = ToReadBookAdapter.toReadBookList.get(position);

                        book.setTitle(title);
                        book.setWriter(writer);
                        book.setPublisher(publisher);
                        book.setPublishDate(publishDate);

                        adapter.notifyDataSetChanged();

                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        finish();

                        break;

                    case R.id.delete :
                        // delete 클릭했을 때 수행할 동작
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditBookInfoActivity.this);
                        builder.setMessage("책을 삭제하시겠습니까?.");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // 확인 클릭했을 때 해당 책 삭제한다.
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
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        textView_edit.setOnClickListener(click);
        textView_delete.setOnClickListener(click);

    }



}
