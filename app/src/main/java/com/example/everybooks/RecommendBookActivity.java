package com.example.everybooks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.ExternalBook;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class RecommendBookActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_total_num;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    String requestUrl;
    ArrayList<ExternalBook> externalBookList = new ArrayList<>();
    ExternalBook externalBook;

    String totalCount;

    final String TAG = "RecommendBookActivity";

    int year;
    int month;
    int lastDay;

    String thisMonthStart;
    String thisMonthEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_book);

        // 뷰 요소 초기화
        textView_total_num = findViewById(R.id.total_num);

        // 임시로 데이터 추가
       /* ArrayList<Book> list = RecommendBookAdapter.externalBookList;
        Book book = new Book();
        book.setTitle("추천책");
        book.setWriter("추천책작가");
        book.setPlot("추천책 줄거리");
        list.add(book);*/

        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.recommend_book_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        lastDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);

        thisMonthStart = Integer.toString(year)+Integer.toString(month)+ "01";
        thisMonthEnd = Integer.toString(year)+Integer.toString(month)+Integer.toString(lastDay);

        Log.d(TAG, "첫 일" + thisMonthStart);
        Log.d(TAG, "마지막 일" + thisMonthEnd);

        //AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();


    }

   /* private void xml_parse() throws IOException {
        String TAG = "Parsing";

        requestUrl = "https://nl.go.kr/NL/search/openApi/saseoApi.do?key=" + getString(R.string.recommend_book_api_key);
        URL url = new URL(requestUrl);
        InputStream inputStream = url.openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        XmlPullParserFactory xmlPullParserFactory = null;
        XmlPullParser xmlPullParser = null;

        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(reader);

            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml START");
                        break;
                    case XmlPullParser.START_TAG:
                        Log.i(TAG, "Start TAG :" + xmlPullParser.getName());
                        break;
                    case XmlPullParser.END_TAG:
                        Log.i(TAG,"End TAG : "+ xmlPullParser.getName());
                        break;
                    case XmlPullParser.TEXT:
                        Log.i(TAG,"TEXT : "+ xmlPullParser.getText());
                        break;
                }
                try {
                    eventType = xmlPullParser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally{
            try{
                if(reader !=null) reader.close();
                if(inputStreamReader !=null) inputStreamReader.close();
                if(inputStream !=null) inputStream.close();
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
    }*/

    public class MyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            requestUrl = "https://nl.go.kr/NL/search/openApi/saseoApi.do?key=" + getString(R.string.recommend_book_api_key)
                    +"&start_date=" + thisMonthStart + "&end_date=" + thisMonthEnd;

            URL url = null;
            try {
                url = new URL(requestUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            XmlPullParserFactory xmlPullParserFactory = null;
            XmlPullParser xmlPullParser = null;

            try {
                xmlPullParserFactory = XmlPullParserFactory.newInstance();
                xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(reader);

                int eventType = xmlPullParser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            Log.i(TAG, "xml START");
                            break;
                        case XmlPullParser.START_TAG:
                            String startTag = xmlPullParser.getName();
                            Log.i(TAG, "Start TAG :" + startTag);
                            if(startTag.equals("totalCount"))
                            {
                                totalCount = xmlPullParser.nextText();
                                Log.d(TAG,"totalCount : " + totalCount);

                            }
                            else if(startTag.equals("item"))
                            {
                                externalBook = new ExternalBook();
                                Log.d(TAG,"recommnedBook 객체 추가");
                            }
                            else if(startTag.equals("recomtitle"))
                            {
                                externalBook.setTitle(xmlPullParser.nextText());
                                Log.d(TAG, "recomTitle : " + externalBook.getTitle());
                            }
                            else if(startTag.equals("recomauthor"))
                            {
                                externalBook.setWriter(xmlPullParser.nextText());
                                Log.d(TAG, "recomAuthor : " + externalBook.getWriter());
                            }
                            else if(startTag.equals("recompublisher"))
                            {
                                externalBook.setPublisher(xmlPullParser.nextText());
                                Log.d(TAG, "recomPublisher : " + externalBook.getPublisher());
                            }
                            else if(startTag.equals("recomcontens"))
                            {
                                String comment = xmlPullParser.nextText();

                                comment = getOnlyKor(comment);
                                Log.d(TAG, "comment : " + comment);
                                externalBook.setDescription(comment);
                                Log.d(TAG, "recomcontens : " + externalBook.getDescription());

                            }
                            else if(startTag.equals("publishYear"))
                            {
                                externalBook.setPublishDate(xmlPullParser.nextText());
                                Log.d(TAG, "publishYear : " + externalBook.getPublishDate());
                            }
                            else if(startTag.equals("recomfilepath"))
                            {
                                externalBook.setImgFilePath(xmlPullParser.nextText());
                                Log.d(TAG, "recomfilepath : " + externalBook.getImgFilePath());
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            Log.i(TAG,"End TAG : "+ xmlPullParser.getName());

                            String endTag = xmlPullParser.getName();
                            if(endTag.equals("item"))
                            {
                                externalBookList.add(externalBook);
                            }

                            break;
                        case XmlPullParser.TEXT:
                            Log.i(TAG,"TEXT : "+ xmlPullParser.getText());
                            break;
                    }
                    try {
                        eventType = xmlPullParser.next();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            } finally{
                try{
                    if(reader !=null) reader.close();
                    if(inputStreamReader !=null) inputStreamReader.close();
                    if(inputStream !=null) inputStream.close();
                }catch(Exception e2){
                    e2.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "externalBookList.size() : " + externalBookList.size());
            //어답터 연결
            RecommendBookAdapter adapter = new RecommendBookAdapter(getApplicationContext(), externalBookList);
            recyclerView.setAdapter(adapter);

            textView_total_num.setText(totalCount);
        }

    }


    public static String getOnlyKor(String str)
    {
        String textWithoutTag = str.replaceAll("&nbsp;", " ");
        textWithoutTag = textWithoutTag.replaceAll("&rsquo;","");
        textWithoutTag = textWithoutTag.replaceAll("&lsquo;","");
        textWithoutTag = textWithoutTag.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        return textWithoutTag;
    }




}
