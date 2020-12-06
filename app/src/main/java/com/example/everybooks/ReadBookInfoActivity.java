package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import com.example.everybooks.data.Book;
import com.example.everybooks.data.Memo;
import com.example.everybooks.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    int bookId;
    int starNum;
    String title;
    String writer;
    String publisher;
    String publishDate;
    String startDate;
    String endDate;
    String state;
    String readTime;
    String img;

    final String TAG = "테스트";

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

        // 데이터 받아오기
        bookId = getIntent().getIntExtra("bookId", -1);


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
                        intent = new Intent( getApplicationContext(), CreateMemoActivity.class);
                        intent.putExtra("bookId", bookId);
                        startActivity(intent);
                        break;

                    case R.id.btn_delete:
                        // delete 를 클릭하면 책을 삭제한다.
                        // 책의 상태에 따라 알맞은 리스트에서 삭제되도록 한다.
                        // 관련된 메모도 삭제되도록 한다.
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReadBookInfoActivity.this);
                        builder.setMessage("책을 삭제하시겠습니까?");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                        // 저장되어있는 책 리스트를 불러온다.
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
                                                    String img = jsonObject.getString("img");
                                                    String title = jsonObject.getString("title");
                                                    String writer = jsonObject.getString("writer");
                                                    String publisher = jsonObject.getString("publisher");
                                                    String publishDate = jsonObject.getString("publishDate");
                                                    String insertDate = jsonObject.getString("insertDate");
                                                    String startDate = jsonObject.getString("startDate");
                                                    String endDate = jsonObject.getString("endDate");
                                                    String readTime = jsonObject.getString("readTime");
                                                    String state = jsonObject.getString("state");
                                                    int starNum = jsonObject.getInt("starNum");

                                                    Book book = new Book();
                                                    book.setBookId(bookId);
                                                    book.setImg(img);
                                                    book.setTitle(title);
                                                    book.setWriter(writer);
                                                    book.setPublisher(publisher);
                                                    book.setPublishDate(publishDate);
                                                    book.setInsertDate(insertDate);
                                                    book.setStartDate(startDate);
                                                    book.setEndDate(endDate);
                                                    book.setReadTime(readTime);
                                                    book.setState(state);
                                                    book.setStarNum(starNum);
                                                    bookArrayList.add(0, book);

                                                }

                                                Log.d(TAG, "저장되어있는 bookArrayList.size : " + bookArrayList.size());

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
                                                }
                                            }

                                            Log.d(TAG, "삭제한 뒤 bookArrayList.size : " + bookArrayList.size());

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
                                                    bookJson.put("img", book.getImg());
                                                    bookJson.put("title", book.getTitle());
                                                    bookJson.put("writer", book.getWriter());
                                                    bookJson.put("publisher", book.getPublisher());
                                                    bookJson.put("publishDate", book.getPublishDate());
                                                    bookJson.put("state", book.getState());
                                                    bookJson.put("insertDate", book.getInsertDate());
                                                    bookJson.put("startDate", book.getStartDate());
                                                    bookJson.put("endDate", book.getEndDate());
                                                    bookJson.put("readTime", book.getReadTime());
                                                    bookJson.put("starNum", book.getStarNum());

                                                    jsonArray.put(bookJson);
                                                }
                                                catch (Exception e)
                                                {
                                                    System.out.println(e.toString());
                                                }
                                            }

                                            bookListString = jsonArray.toString();

                                            bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = bookInfo.edit();
                                            editor.putString("bookList", bookListString);
                                            editor.commit();

                                            Log.d(TAG, "삭제한 뒤 저장되어있는 bookListString : " + bookListString);

                                        }

                                        // 관련된 메모 삭제
                                        // 저장되어있는 메모 리스트를 불러온다.
                                        SharedPreferences memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                                        String memoListString = memoInfo.getString("memoList", null);
                                        ArrayList<Memo> allMemoList = new ArrayList<>();

                                        if (memoListString != null)
                                        {
                                            try
                                            {
                                                JSONArray jsonArray = new JSONArray(memoListString);

                                                // JsonArray 형태로는 객체를 삭제할 수 없기 때문에
                                                // jsonArray 의 길이만큼 반복해서 jsonObject 를 가져오고,
                                                // 삭제할 bookId 와 일치하지 않는 jsonObject 만 Memo 객체에 담은 뒤 ArrayList<Memo> 에 담는다.
                                                for (int i = 0; i < jsonArray.length(); i++)
                                                {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                    if(jsonObject.getInt("bookId") != bookId)
                                                    {
                                                        Memo memo = new Memo();
                                                        memo.setMemoId(jsonObject.getInt("memoId"));
                                                        memo.setBookId(jsonObject.getInt("bookId"));
                                                        memo.setMemoText(jsonObject.getString("memoText"));
                                                        memo.setMemoDate(jsonObject.getString("memoDate"));

                                                        allMemoList.add(memo);
                                                    }
                                                }

                                                Log.d(TAG, "저장되어있는 allMemoList.size : " + allMemoList.size());

                                            }
                                            catch (Exception e)
                                            {
                                                System.out.println(e.toString());
                                            }


                                            /// JSONArray 로 변환해서 다시 저장하기
                                            JSONArray jsonArray = new JSONArray();

                                            for (int i = 0; i < allMemoList.size(); i++)
                                            {
                                                Memo memo = allMemoList.get(i);

                                                // json 객체에 입력받은 값을 저장한다.
                                                try
                                                {
                                                    JSONObject jsonObject = new JSONObject();

                                                    jsonObject.put("memoId", memo.getMemoId());
                                                    jsonObject.put("bookId", memo.getBookId());
                                                    jsonObject.put("memoText", memo.getMemoText());
                                                    jsonObject.put("memoDate", memo.getMemoDate());

                                                    jsonArray.put(jsonObject);
                                                }
                                                catch (Exception e)
                                                {
                                                    System.out.println(e.toString());
                                                }
                                            }

                                            memoListString = jsonArray.toString();

                                            memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = memoInfo.edit();
                                            editor.putString("memoList", memoListString);
                                            editor.commit();

                                            //Log.d(TAG, "삭제한 뒤 저장되어있는 memoListString : " + afterMemoListString);

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

                    case R.id.edit :
                        // Edit 을 클릭하면 책 id 를 담고 책 편집 화면으로 전환한다.
                        intent = new Intent(getApplicationContext(), EditBookInfoActivity.class);
                        intent.putExtra("bookId", bookId);
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
        String bookListString =bookInfo.getString("bookList", null);

        if(bookListString != null)
        {
            try
            {
                JSONArray jsonArray = new JSONArray(bookListString);
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
                        endDate = jsonObject.getString("endDate");
                        starNum = jsonObject.getInt("starNum");
                        readTime = jsonObject.getString("readTime");
                        img = jsonObject.getString("img");
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }

        // 책 정보를 뷰 요소에 담아준다.
        textView_title.setText(title);
        textView_writer.setText(writer);
        textView_publisher.setText(publisher);
        textView_publish_date.setText(publishDate);
        textView_start_date.setText(startDate);
        textView_end_date.setText(endDate);
        textView_time.setText(readTime);

        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(img);
        imageView_img.setImageBitmap(bitmap);

        // 책에 해당하는 메모를 보여주기 위해서 전체 메모리스트를 가져온 뒤 bookId 와 일치하는 메모만 arrayList에 담아서 어댑터로 보낸다.
        SharedPreferences memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
        String memoListString = memoInfo.getString("memoList", null);
        ArrayList<Memo> thisBookMemoList = new ArrayList<>();
        try
        {
            JSONArray jsonArray = new JSONArray(memoListString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                if(bookId == jsonObject.getInt("bookId"))
                {
                    Memo memo = new Memo();
                    memo.setMemoId(jsonObject.getInt("memoId"));
                    memo.setBookId(jsonObject.getInt("bookId"));
                    memo.setMemoText(jsonObject.getString("memoText"));
                    memo.setMemoDate(jsonObject.getString("memoDate"));
                    thisBookMemoList.add(0, memo);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        // 메모 리스트뷰 어댑터 객체 생성
        memoAdapter = new MemoAdapter(thisBookMemoList);

        listView.setAdapter(memoAdapter);

        // 각 메모를 클릭하면 책 제목, 메모아이디, 메모 내용  데이터를 담아서
        // 메모 편집 화면으로 이동한다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                intent = new Intent(getApplicationContext(), EditMemoActivity.class);

                Memo memo = (Memo)memoAdapter.getItem(position);
                intent.putExtra("title", title);
                intent.putExtra("memoId", memo.getMemoId());
                intent.putExtra("memoText", memo.getMemoText());

                startActivity(intent);
            }
        });


        // 각 메모를 길게 클릭하면 삭제하겠냐고 확인하는 문구가 뜬다.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Memo memo = (Memo)memoAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadBookInfoActivity.this);
                builder.setMessage("메모를 삭제하시겠습니까?.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // 메모를 삭제하기 위해서 저장되어있는 메모 리스트를 불러온다.
                                SharedPreferences memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                                String memoListString = memoInfo.getString("memoList", null);
                                ArrayList<Memo> allMemoList = new ArrayList<>();

                                if (memoListString != null)
                                {
                                    try
                                    {
                                        JSONArray jsonArray = new JSONArray(memoListString);

                                        // JsonArray 형태로는 객체를 삭제할 수 없기 때문에
                                        // jsonArray 의 길이만큼 반복해서 jsonObject 를 가져오고,
                                        // 삭제할 memoId 와 일치하지 않는 jsonObject 만 Memo 객체에 담은 뒤 ArrayList<Memo> 에 담는다.
                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            if(jsonObject.getInt("memoId") != memo.getMemoId())
                                            {
                                                Memo memo = new Memo();
                                                memo.setMemoId(jsonObject.getInt("memoId"));
                                                memo.setBookId(jsonObject.getInt("bookId"));
                                                memo.setMemoText(jsonObject.getString("memoText"));
                                                memo.setMemoDate(jsonObject.getString("memoDate"));

                                                allMemoList.add(memo);
                                            }
                                        }

                                        Log.d(TAG, "저장되어있는 allMemoList.size : " + allMemoList.size());

                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(e.toString());
                                    }


                                    /// JSONArray 로 변환해서 다시 저장하기
                                    JSONArray jsonArray = new JSONArray();

                                    for (int i = 0; i < allMemoList.size(); i++)
                                    {
                                        Memo memo = allMemoList.get(i);

                                        // json 객체에 입력받은 값을 저장한다.
                                        try
                                        {
                                            JSONObject jsonObject = new JSONObject();

                                            jsonObject.put("memoId", memo.getMemoId());
                                            jsonObject.put("bookId", memo.getBookId());
                                            jsonObject.put("memoText", memo.getMemoText());
                                            jsonObject.put("memoDate", memo.getMemoDate());

                                            jsonArray.put(jsonObject);
                                        }
                                        catch (Exception e)
                                        {
                                            System.out.println(e.toString());
                                        }
                                    }

                                    memoListString = jsonArray.toString();

                                    memoInfo = getSharedPreferences("memoInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = memoInfo.edit();
                                    editor.putString("memoList", memoListString);
                                    editor.commit();

                                    dialog.dismiss();

                                }

                                // 현재 화면 종료하고
                                finish();
                                // 다시 요청
                                intent = new Intent(getApplicationContext(), ReadingBookInfoActivity.class);
                                intent.putExtra("bookId", memo.getBookId());
                                startActivity(intent);

                            /*
                            // 확인 클릭했을 때 해당 메모 삭제한다.
                            MemoAdapter.memoList.remove(position);

                            // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                            memoAdapter.notifyDataSetChanged();*/
                                //dialog.dismiss();

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
