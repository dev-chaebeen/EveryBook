package com.example.everybooks;

import android.graphics.drawable.Drawable;

// 책 정보 객체 생성하는 클래스
public class Book
{
    private Drawable img;       // 표지
    private String title;       // 제목
    private String writer;      // 작가
    private String publisher;   // 출판사
    private String publish_date;// 출간일
    private String insert_date; // 등록일
    private String start_date;  // 독서 시작일
    private String end_date;    // 독서 마감일
    private String state;       // 상태 : to_read, reading, read

    private int pages;          // 페이지수
    private int time;           // 기록한 시간
    

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

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
