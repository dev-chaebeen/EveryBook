package com.example.everybooks;

import android.graphics.drawable.Drawable;

// 책 정보 객체 생성하는 클래스
public class Book
{
    // 리사이클러뷰 임시 생성자
    public Book(int bookId, String title, String insertDate)
    {
        this.bookId = bookId;
        this.title = title;
        this.insertDate = insertDate;
    }

    private int bookId;
    private Drawable img;       // 표지
    private String title;       // 제목
    private String writer;      // 작가
    private String publisher;   // 출판사
    private String publishDate;// 출간일
    private String insertDate; // 등록일
    private String startDate;  // 독서 시작일
    private String endDate;    // 독서 마감일
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
