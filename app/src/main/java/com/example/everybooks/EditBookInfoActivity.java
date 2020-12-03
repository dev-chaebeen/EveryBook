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

                        // 읽을 책 리스트를 얻어와서 전달받은 bookId 와 일치하는 book 객체의 데이터를 입력받은 값으로 바꿔준다.

                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                        String bookListString= bookInfo.getString("bookList", null);

                        try
                        {
                            JSONArray jsonArray = new JSONArray(bookListString);
                            for (int i = 0; i < jsonArray.length() ; i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if(jsonObject.getInt("bookId") == bookId)
                                {
                                    jsonObject.put("title", title);
                                    jsonObject.put("writer", writer);
                                    jsonObject.put("publisher", publisher);
                                    jsonObject.put("publishDate", publishDate);
                                }
                            }

                            // test ok
                            Log.d(TAG," jsonArray.toString: " + jsonArray.toString());

                            SharedPreferences.Editor editor = bookInfo.edit();
                            editor.putString("bookList", jsonArray.toString());
                            editor.commit();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
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
                                        // 저장되어있는 읽을 책 리스트를 불러온다.
                                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                                        String bookListString = bookInfo.getString("bookList", null);
                                        ArrayList<Book> bookArrayList = new ArrayList<>();

                                        if (bookListString != null)
                                        {
                                            try
                                            {
                                                JSONArray jsonArray = new JSONArray(bookListString);

                                                // 가져온 jsonArray의 길이만큼 반복해서 jsonObject 를 가져오고, Book 객체에 담은 뒤 ArrayList<Book> 에 담는다.
                                                for (int i = 0; i < jsonArray.length(); i++)
                                                {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                    int bookId = jsonObject.getInt("bookId");
                                                    //String img = jsonObject.getString("img");
                                                    String title = jsonObject.getString("title");
                                                    String writer = jsonObject.getString("writer");
                                                    String publisher = jsonObject.getString("publisher");
                                                    String publishDate = jsonObject.getString("publishDate");
                                                    String insertDate = jsonObject.getString("insertDate");
                                                    String state = jsonObject.getString("state");

                                                    Book book = new Book();
                                                    book.setBookId(bookId);
                                                    //book.setImg(img);
                                                    book.setTitle(title);
                                                    book.setWriter(writer);
                                                    book.setPublisher(publisher);
                                                    book.setPublishDate(publishDate);
                                                    book.setInsertDate(insertDate);
                                                    book.setState(state);
                                                    bookArrayList.add(0, book);

                                                }

                                                Log.d(TAG, "저장되어있는 bookList : " + bookArrayList.size());

                                            }
                                            catch (Exception e)
                                            {
                                                System.out.println(e.toString());
                                            }

                                            for (int j = 0; j < bookArrayList.size() ; j++)
                                            {
                                                Book book = bookArrayList.get(j);
                                                if(bookId == book.getBookId())
                                                {
                                                    bookArrayList.remove(j);
                                                    Log.d(TAG, "삭제할 북아이디, 인덱스 :" + book.getBookId() + "," + j);
                                                }
                                            }

                                            Log.d(TAG, "삭제한 뒤 bookArrayList.size : " + bookArrayList.size());

                                            // test
                                            ToReadBookAdapter adapter = new ToReadBookAdapter(bookArrayList);


                                            /// JSONArray 로 변환해서 다시 저장하기
                                            JSONArray jsonArray = new JSONArray();

                                            for (int i = 0; i < bookArrayList.size(); i++)
                                            {
                                                Book book = bookArrayList.get(i);

                                                // json 객체에 입력받은 값을 저장한다.
                                                try
                                                {
                                                    JSONObject bookJson = new JSONObject();
                                                    bookJson.put("bookId", book.getBookId());
                                                    //bookJson.put("img", img);
                                                    bookJson.put("title", book.getTitle());
                                                    bookJson.put("writer", book.getWriter());
                                                    bookJson.put("publisher", book.getPublisher());
                                                    bookJson.put("publishDate", book.getPublishDate());
                                                    bookJson.put("state", book.getState());
                                                    bookJson.put("insertDate", book.getInsertDate());
                                                    jsonArray.put(bookJson);
                                                }
                                                catch (Exception e)
                                                {
                                                    System.out.println(e.toString());
                                                }
                                            }

                                            bookListString = jsonArray.toString();

                                            Log.d(TAG, "set메소드 : " + bookListString);

                                            bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = bookInfo.edit();
                                            editor.putString("bookList", bookListString);
                                            editor.commit();

                                            Log.d(TAG, "삭제한 뒤 저장되어있는 bookListString : " + bookListString);


                                        }




                                        /*
                                        SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                                        String toReadBookListString= bookInfo.getString("toReadBookList", null);

                                        try
                                        {
                                            JSONArray jsonArray = new JSONArray(toReadBookListString);
                                            for (int i = 0; i < jsonArray.length() ; i++)
                                            {
                                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                                // 전달받은 bookId 와 jsonObject의 BookId 값이 같다면
                                                if(bookId == jsonObject.getInt("bookId"))
                                                {
                                                    //Log.d(TAG,"삭제할 책의 bookId : " + bookId);
                                                    jsonObject.remove("bookId");
                                                    jsonObject.remove("title");
                                                    jsonObject.remove("writer");
                                                    jsonObject.remove("publisher");
                                                    jsonObject.remove("publishDate");
                                                    jsonObject.remove("insertDate");
                                                    jsonObject.remove("state");

                                                }
                                            }

                                            // test ok
                                            Log.d(TAG," 삭제하고 jsonArray.toString: " + jsonArray.toString());

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
                                        */

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
