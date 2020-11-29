package com.example.everybooks;

import android.graphics.drawable.Drawable;

// 책 정보 객체 생성하는 클래스
public class Book
{
    // 일반 생성자
    public Book()
    {
        this.bookId = tempBookId+1;
    }

    // 리사이클러뷰 임시 생성자
    public Book(String title, String insertDate)
    {
        this.bookId = tempBookId + 1;
        this.title = title;
        this.insertDate = insertDate;
    }

    // 임시 아이디
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
}
