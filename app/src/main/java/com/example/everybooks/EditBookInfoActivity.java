package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditBookInfoActivity extends AppCompatActivity
{
    Intent intent;
    View.OnClickListener click;

    // 뷰 요소 선언
    TextView textView_edit;
    TextView textView_delete;

    ImageView ImageView_img;
    EditText editText_title;
    EditText editText_writer;
    EditText editText_publisher;
    EditText editText_publish_date;

    // 전달받는 데이터 선언
    int position;
    int bookId;
    String state;

    final String TAG = "테스트";

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

        // 처음 액티비티 생성될 때는 인텐트로 전달받은 책 정보를 보여준다.
        ImageView_img.setImageResource(getIntent().getIntExtra("img",0));
        editText_title.setText(getIntent().getStringExtra("title"));
        editText_writer.setText(getIntent().getStringExtra("writer"));
        editText_publisher.setText(getIntent().getStringExtra("publisher"));
        editText_publish_date.setText(getIntent().getStringExtra("publishDate"));

        // 전달받은 데이터 저장
        position = getIntent().getIntExtra("position",-1);
        bookId = getIntent().getIntExtra("bookId", -1);
        state = getIntent().getStringExtra("state");

        click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.edit :

                        // edit 을 클릭하면 사용자에게 입력받은 값을 저장해서 그 값으로 책의 정보를 수정한다.
                        // 전달받은 책의 상태에 적합한 리스트에 책이 저장되도록 한다.
                        String title = editText_title.getText().toString();
                        String writer = editText_writer.getText().toString();
                        String publisher = editText_publisher.getText().toString();
                        String publishDate = editText_publish_date.getText().toString();

                        Log.d(TAG, "바꾸려는 제목 : " + title );

                        if(state.equals("toRead"))
                        {
                            // 읽을 책 리스트를 얻어와서 전달받은 bookId 와 일치하는 book 객체의 데이터를 입력받은 값으로 바꿔준다.

                            SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                            String toReadBookListString= bookInfo.getString("toReadBookList", null);

                            try
                            {
                                JSONArray jsonArray = new JSONArray(toReadBookListString);
                                for (int i = 0; i < jsonArray.length() ; i++)
                                {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    jsonObject.put("title", title);
                                    jsonObject.put("writer", writer);
                                    jsonObject.put("publisher", publisher);
                                    jsonObject.put("publishDate", publishDate);
                                }

                                // test ok
                                Log.d(TAG," jsonArray.toString: " + jsonArray.toString());

                                SharedPreferences.Editor editor = bookInfo.edit();
                                editor.putString("toReadBookList", jsonArray.toString());
                                editor.commit();
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.toString());
                            }

                            MainActivity mainActivity = new MainActivity();
                            ArrayList<Book> arrayList = mainActivity.getToReadBookList();
                            // 그리고 그 arrayList 를 어댑터에 보내준다.
                            //어댑터에 보내고
                            ToReadBookAdapter adapter = new ToReadBookAdapter(arrayList);


                        }
                        else if(state.equals("reading"))
                        {
                            ReadingBookAdapter adapter = new ReadingBookAdapter();
                            Book book = ReadingBookAdapter.readingBookList.get(position);

                            book.setTitle(title);
                            book.setWriter(writer);
                            book.setPublisher(publisher);
                            book.setPublishDate(publishDate);

                            adapter.notifyDataSetChanged();
                        }
                        else if(state.equals("read"))
                        {
                            ReadBookAdapter adapter = new ReadBookAdapter();
                            Book book = ReadBookAdapter.readBookList.get(position);

                            book.setTitle(title);
                            book.setWriter(writer);
                            book.setPublisher(publisher);
                            book.setPublishDate(publishDate);

                            adapter.notifyDataSetChanged();
                        }

                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                        break;

                    case R.id.delete :

                        // delete 를 클릭하면 책을 삭제한다.
                        // 책의 상태에 따라 알맞은 리스트에서 삭제되도록 한다.
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditBookInfoActivity.this);
                        builder.setMessage("책을 삭제하시겠습니까?");
                        builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    if(state.equals("toRead"))
                                    {
                                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                                        String toReadBookListString= bookInfo.getString("toReadBookList", null);

                                        try
                                        {
                                            JSONArray jsonArray = new JSONArray(toReadBookListString);
                                            for (int i = 0; i < jsonArray.length() ; i++)
                                            {
                                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                                if(bookId == jsonObject.getInt("bookId"))
                                                {
                                                    jsonObject.remove("bookId");
                                                }
                                            }

                                            // test ok
                                            Log.d(TAG," jsonArray.toString: " + jsonArray.toString());

                                            SharedPreferences.Editor editor = bookInfo.edit();
                                            editor.putString("toReadBookList", jsonArray.toString());
                                            editor.commit();
                                        }
                                        catch (Exception e)
                                        {
                                            System.out.println(e.toString());
                                        }

                                        MainActivity mainActivity = new MainActivity();
                                        ArrayList<Book> arrayList = mainActivity.getToReadBookList();
                                        // 그리고 그 arrayList 를 어댑터에 보내준다.
                                        //어댑터에 보내고
                                        ToReadBookAdapter adapter = new ToReadBookAdapter(arrayList);
                                        // 기존
                                        //ToReadBookAdapter adapter = new ToReadBookAdapter();
                                        //adapter.removeItem(position);
                                    }
                                    else if(state.equals("reading"))
                                    {
                                        ReadingBookAdapter adapter = new ReadingBookAdapter();
                                        adapter.removeItem(position);
                                    }
                                    else if(state.equals("read"))
                                    {
                                        ReadBookAdapter adapter = new ReadBookAdapter();
                                        adapter.removeItem(position);
                                    }

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
