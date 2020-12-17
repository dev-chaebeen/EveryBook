package com.example.everybooks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;
import com.example.everybooks.data.RecommendBook;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecommendBookActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    TextView textView_total_num;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    String requestUrl;
    ArrayList<RecommendBook> recommendBookList = new ArrayList<>();
    RecommendBook recommendBook;

    String totalCount;

    final String TAG = "RecommendBookActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_book);

        // 뷰 요소 초기화
        textView_total_num = findViewById(R.id.total_num);

        // 임시로 데이터 추가
       /* ArrayList<Book> list = RecommendBookAdapter.recommendBookList;
        Book book = new Book();
        book.setTitle("추천책");
        book.setWriter("추천책작가");
        book.setPlot("추천책 줄거리");
        list.add(book);*/

        // 리사이클러뷰 생성
        recyclerView = findViewById(R.id.recommend_book_list);
        recyclerView.setHasFixedSize(true);
        //adapter = new RecommendBookAdapter(RecommendBookAdapter.recommendBookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.setAdapter(adapter);

        // test
//        AsyncTask
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

            //todo 현재년도, 현재월 -1 이랑 월의 일수 구해서 start_date, end_date로 넘긴다.

            requestUrl = "https://nl.go.kr/NL/search/openApi/saseoApi.do?key=" + getString(R.string.recommend_book_api_key)
                    +"&start_date=20201101&end_date=20201131";
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
                                recommendBook = new RecommendBook();
                                Log.d(TAG,"recommnedBook 객체 추가");
                            }
                            else if(startTag.equals("recomtitle"))
                            {
                                recommendBook.setTitle(xmlPullParser.nextText());
                                Log.d(TAG, "recomTitle : " + recommendBook.getTitle());
                            }
                            else if(startTag.equals("recomauthor"))
                            {
                                recommendBook.setWriter(xmlPullParser.nextText());
                                Log.d(TAG, "recomAuthor : " + recommendBook.getWriter());
                            }
                            else if(startTag.equals("recompublisher"))
                            {
                                recommendBook.setPublisher(xmlPullParser.nextText());
                                Log.d(TAG, "recomPublisher : " + recommendBook.getPublisher());
                            }
                            else if(startTag.equals("recomcontens"))
                            {
                                String comment = xmlPullParser.nextText();

                                comment = getOnlyKor(comment);
                                Log.d(TAG, "comment : " + comment);
                                recommendBook.setRecommendComment(comment);
                                Log.d(TAG, "recomcontens : " + recommendBook.getRecommendComment());

                            }
                            else if(startTag.equals("publishYear"))
                            {
                                recommendBook.setPublishDate(xmlPullParser.nextText());
                                Log.d(TAG, "publishYear : " + recommendBook.getPublishDate());
                            }
                            else if(startTag.equals("recomfilepath"))
                            {
                                recommendBook.setImgFilePath(xmlPullParser.nextText());
                                Log.d(TAG, "recomfilepath : " + recommendBook.getImgFilePath());
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            Log.i(TAG,"End TAG : "+ xmlPullParser.getName());

                            String endTag = xmlPullParser.getName();
                            if(endTag.equals("item"))
                            {
                                recommendBookList.add(recommendBook);
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


            //requestUrl = "https://nl.go.kr/NL/search/openApi/saseoApi.do?key=" + getString(R.string.recommend_book_api_key);

           /* try {

                boolean b_totalCount = false;
                boolean b_recom_title = false;
                boolean b_recom_author = false;
                boolean b_recom_publisher = false;
                boolean b_recom_contents = false;
                boolean b_recom_file_path = false;
                boolean b_recom_year = false;
                boolean b_recom_month = false;
                boolean b_publish_year = false;

                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            recommendBookList = new ArrayList<RecommendBook>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("item") && recommendBook != null) {
                                recommendBookList.add(recommendBook);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if(parser.getName().equals("busArrivalList")){
                                recommendBook = new RecommendBook();
                            }
                            if (parser.getName().equals("totalCount")) b_totalCount = true;
                            if (parser.getName().equals("recom_title")) b_recom_title = true;
                            if (parser.getName().equals("recom_author")) b_recom_author = true;
                            if (parser.getName().equals("recom_publisher")) b_recom_publisher = true;
                            if (parser.getName().equals("recom_contents")) b_recom_contents = true;
                            if (parser.getName().equals("recom_file_path")) b_recom_file_path = true;
                            if (parser.getName().equals("recom_year")) b_recom_year = true;
                            if (parser.getName().equals("recom_month")) b_recom_month = true;
                            if (parser.getName().equals("publish_year")) b_publish_year = true;


                            break;
                        case XmlPullParser.TEXT:
                            if(b_totalCount){
                                totalCount = Integer.parseInt(parser.getText());
                                b_totalCount = false;
                            } else if(b_recom_title) {
                                recommendBook.setTitle(parser.getText());
                                b_recom_title = false;
                            } else if (b_recom_author) {
                                recommendBook.setWriter(parser.getText());
                                b_recom_author = false;
                            } else if (b_recom_publisher) {
                                recommendBook.setPublisher(parser.getText());
                                b_recom_publisher = false;
                            } else if (b_recom_contents) {
                                recommendBook.setPlot(parser.getText());
                                b_recom_contents = false;
                            } else if (b_recom_file_path) {
                                recommendBook.setImg(parser.getText());
                                b_recom_file_path = false;
                            } else if (b_recom_year) {
                                recom_year = Integer.parseInt(parser.getText());
                                b_recom_year = false;
                            } else if (b_recom_month) {
                                recom_month = Integer.parseInt(parser.getText());
                                b_recom_month = false;
                            } else if (b_publish_year) {
                                recommendBook.setPublishDate(parser.getText());
                                b_publish_year = false;
                            }

                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "recommendBookList.size() : " + recommendBookList.size());
            //어답터 연결
            RecommendBookAdapter adapter = new RecommendBookAdapter(getApplicationContext(), recommendBookList);
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

    private String safeNextText(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String result = parser.nextText();
        if (parser.getEventType() != XmlPullParser.END_TAG) {
            parser.nextTag();
        }
        return result;
    }


}
