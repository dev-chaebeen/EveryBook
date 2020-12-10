package com.example.everybooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Book;
import com.example.everybooks.data.Memo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity
{
    ListView listView;
    CalendarView calendarView;

    final String TAG = "테스트";
    int setYear;
    int setMonth;
    int setDate;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_calendar);

        // 뷰 요소 초기화
        calendarView = findViewById(R.id.calendar);
        listView =  findViewById(R.id.calendar_book_list);

        Calendar cal = Calendar.getInstance();
        setYear = cal.get ( cal.YEAR );
        setMonth = cal.get ( cal.MONTH ) + 1 ;
        setDate = cal.get ( cal.DATE ) ;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                setYear = year;
                setMonth = month;
                setDate = dayOfMonth;

                try
                {
                    // 캘린더의 날짜를 클릭했을 때 그 날 독서를 완료한 책 리스트를 보여준다.

                    // 저장되어있는 책 리스트를 가져온 뒤 JsonObject 를 얻기 위해서 JsonArray 형식으로 변환한다.
                    // jsonObject 에 저장되어있는 endDate 가 캘린더에서 선택된 날짜와 같다면
                    // ArrayList<Book> thisDayBookList 에 저장한다.
                    // month 는 실제보다 1 작기 때문에 +1 해준다.
                    SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                    String bookListString = bookInfo.getString("bookList", null);
                    ArrayList<Book> thisDayBookList = new ArrayList<>();

                    Log.d(TAG, "CalendarActivity, 저장되어있는 책 목록 : " + bookListString);

                    if(bookListString != null)
                    {
                        JSONArray jsonArray = new JSONArray(bookListString);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if(jsonObject.getString("endDate").equals(year+"."+(month+1)+"."+dayOfMonth))
                            {
                                int bookId = jsonObject.getInt("bookId");
                                String img = jsonObject.getString("img");
                                String title = jsonObject.getString("title");
                                String writer = jsonObject.getString("writer");
                                String publisher = jsonObject.getString("publisher");
                                String publishDate = jsonObject.getString("publishDate");
                                String insertDate = jsonObject.getString("insertDate");
                                String startDate = jsonObject.getString("startDate");
                                String endDate = jsonObject.getString("endDate");
                                String state = jsonObject.getString("state");
                                String readTime = jsonObject.getString("readTime");
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
                                book.setState(state);
                                book.setReadTime(readTime);
                                book.setStarNum(starNum);

                                thisDayBookList.add(0, book);

                            }
                        }

                        //어댑터에 보내기
                        Log.d(TAG, "CalendarActivity, 클릭한 날짜 : " + year+"."+(month+1)+"."+dayOfMonth);
                        Log.d(TAG, "CalendarActivity, 어댑터에 보내는 읽은책.size : " + thisDayBookList.size());
                        CalendarAdapter adapter = new CalendarAdapter(getApplicationContext(), thisDayBookList);
                        adapter.notifyDataSetChanged();

                        //리스트뷰에 어댑터를 붙여서 사용자에게 내용이 보이도록 한다.
                        listView.setAdapter(adapter);
                    }

                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 액티비티가 전면에 보일 때마다 클릭된 날의 독서를 완료한 책 리스트를 보여주기 위해서 클릭된 날을 저장한다.
        String clickDate = setYear + "." + setMonth + "." + setDate;

        try
        {
            // 저장되어있는 책 리스트를 가져온 뒤 JsonObject 를 얻기 위해서 JsonArray 형식으로 변환한다.
            // jsonObject 에 저장되어있는 endDate 가 캘린더에서 선택된 날짜와 같다면
            // ArrayList<Book> theDayBookList 에 저장한다.
            // month 는 실제보다 1 작기 때문에 +1 해준다.
            SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
            String bookListString = bookInfo.getString("bookList", null);
            ArrayList<Book> theDayBookList = new ArrayList<>();

            Log.d(TAG, "CalendarActivity, 저장되어있는 책 목록 : " + bookListString);

            if(bookListString != null)
            {
                JSONArray jsonArray = new JSONArray(bookListString);

                // 가져온 jsonArray의 길이만큼 반복해서 jsonObject 를 가져오고, Book 객체에 담은 뒤 ArrayList<Book> 에 담는다.
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject.getString("endDate").equals(clickDate))
                    {
                        int bookId = jsonObject.getInt("bookId");
                        String img = jsonObject.getString("img");
                        String title = jsonObject.getString("title");
                        String writer = jsonObject.getString("writer");
                        String publisher = jsonObject.getString("publisher");
                        String publishDate = jsonObject.getString("publishDate");
                        String insertDate = jsonObject.getString("insertDate");
                        String startDate = jsonObject.getString("startDate");
                        String endDate = jsonObject.getString("endDate");
                        String state = jsonObject.getString("state");
                        String readTime = jsonObject.getString("readTime");
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
                        book.setState(state);
                        book.setReadTime(readTime);
                        book.setStarNum(starNum);

                        theDayBookList.add(0, book);

                    }
                }

                //어댑터에 보내기
                Log.d(TAG, "CalendarActivity, 클릭한 날짜 : " + setYear+"."+(setMonth+1)+"."+setDate);
                Log.d(TAG, "CalendarActivity, 어댑터에 보내는 읽은책.size : " + theDayBookList.size());
                CalendarAdapter adapter = new CalendarAdapter(getApplicationContext(), theDayBookList);
                adapter.notifyDataSetChanged();

                //리스트뷰에 어댑터를 붙여서 사용자에게 내용이 보이도록 한다.
                listView.setAdapter(adapter);

                // 각 책을 클릭하면 BookId  데이터를 담아서
                // 상세 화면으로 이동한다.
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id)
                    {
                        intent = new Intent(getApplicationContext(), ReadBookInfoActivity.class);

                        Book book = (Book)adapter.getItem(position);
                        intent.putExtra("bookId", book.getBookId());

                        startActivity(intent);
                    }
                });
            }

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
