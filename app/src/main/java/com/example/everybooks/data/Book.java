package com.example.everybooks.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

// 책 정보 객체 생성하는 클래스
public class Book
{
    ArrayList<Book> toReadBookList;
    ArrayList<Book> readingBookList;
    ArrayList<Book> readBookList;

    // ArrayList를 Json으로 변환하여 SharedPreferences에 String을 저장
    private void setStringArrayPref(Context context, String key, ArrayList values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    // SharedPreferences에서 Json형식의 String을 가져와서 다시 ArrayList로 변환
    public ArrayList getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }




    // 객체를 구분하는 id를 객체마다 가지도록 하기 위해서
    // 객체를 생성할 때마다 bookId 가 1씩 증가하도록 한다.
    public Book()
    {
        //this.bookId = tempBookId+1;
    }

    // 리사이클러뷰 임시 생성자
    public Book(int bookId, String title, String insertDate)
    {
        this.bookId = tempBookId + 1;
        this.title = title;
        this.insertDate = insertDate;
    }

    // 임시 아이디
    //todo static 수정
    static int tempBookId = 1;

    private int bookId;
    private Drawable img;
    private String title;
    private String writer;
    private String publisher;
    private String publishDate;
    private String insertDate;
    private String startDate;
    private String endDate;
    private String state;

    private int pages;
    private int readTime;
    private int starNum;
    private String plot;


    // getter / setter
    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String publishDate() {
        return publishDate;
    }

    public void publishDate(String publish_date) {
        this.publishDate = publish_date;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getReadTime() {
        return readTime;
    }

    public void setReadTime(int readTime) {
        this.readTime = readTime;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public ArrayList<Book> getToReadBookList() {
        return toReadBookList;
    }

    public void setToReadBookList(ArrayList<Book> toReadBookList) {
        this.toReadBookList = toReadBookList;
    }

    public ArrayList<Book> getReadingBookList() {
        return readingBookList;
    }

    public void setReadingBookList(ArrayList<Book> readingBookList) {
        this.readingBookList = readingBookList;
    }

    public ArrayList<Book> getReadBookList() {
        return readBookList;
    }

    public void setReadBookList(ArrayList<Book> readBookList) {
        this.readBookList = readBookList;
    }
}
