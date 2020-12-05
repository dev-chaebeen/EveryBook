package com.example.everybooks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity
{
    ListView listView;
    CalendarView calendarView;

    final String TAG = "테스트";
    int year;
    int month;
    int dayOfMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // 화면 생성
        setContentView(R.layout.activity_calendar);

        // 뷰 요소 초기화
        calendarView = findViewById(R.id.calendar);
        listView =  findViewById(R.id.calendar_book_list);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                // 저장되어있는 책 리스트 어댑터에 보내주기
                try
                {
                    SharedPreferences bookInfo = getSharedPreferences("bookInfo", MODE_PRIVATE);
                    String bookListString = bookInfo.getString("bookList", null);
                    ArrayList<Book> thisDayBookList = new ArrayList<>();

                    Log.d(TAG, "CalendarActivity, 저장되어있는 책 목록 : " + bookListString);

                    if(bookListString != null)
                    {
                        JSONArray jsonArray = new JSONArray(bookListString);

                        // 가져온 jsonArray의 길이만큼 반복해서 jsonObject 를 가져오고, Book 객체에 담은 뒤 ArrayList<Book> 에 담는다.
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if(jsonObject.getString("endDate").equals(year+"."+(month+1)+"."+dayOfMonth))
                            {
                                int bookId = jsonObject.getInt("bookId");
                                //String img = jsonObject.getString("img");
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
                                //book.setImg(img);
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

        //현재 년도, 월, 일을 책 등록일에 저장한다.
        Calendar cal = Calendar.getInstance();
        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        int date = cal.get ( cal.DATE ) ;

        String today = year + "." + month + "." + date;


        // 저장되어있는 책 리스트 어댑터에 보내주기
        try
        {
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

                    if(jsonObject.getString("endDate").equals(today))
                    {
                        int bookId = jsonObject.getInt("bookId");
                        //String img = jsonObject.getString("img");
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
                        //book.setImg(img);
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
                Log.d(TAG, "CalendarActivity, 클릭한 날짜 : " + year+"."+(month+1)+"."+dayOfMonth);
                Log.d(TAG, "CalendarActivity, 어댑터에 보내는 읽은책.size : " + theDayBookList.size());
                CalendarAdapter adapter = new CalendarAdapter(getApplicationContext(), theDayBookList);
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
}
