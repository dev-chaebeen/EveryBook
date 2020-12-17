package com.example.everybooks.data;

public class RecommendBook extends Book
{
    private int recommendYear;
    private int recommendMonth;
    private String recommendComment;
    private String imgFilePath;

    public int getRecommendYear() {
        return recommendYear;
    }

    public void setRecommendYear(int recommendYear) {
        this.recommendYear = recommendYear;
    }

    public int getRecommendMonth() {
        return recommendMonth;
    }

    public void setRecommendMonth(int recommendMonth) {
        this.recommendMonth = recommendMonth;
    }

    public String getRecommendComment() {
        return recommendComment;
    }

    public void setRecommendComment(String recommendComment) {
        this.recommendComment = recommendComment;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    @Override
    public String getImg() {
        return super.getImg();
    }

    @Override
    public void setImg(String img) {
        super.setImg(img);
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public String getWriter() {
        return super.getWriter();
    }

    @Override
    public void setWriter(String writer) {
        super.setWriter(writer);
    }

    @Override
    public String getPublisher() {
        return super.getPublisher();
    }

    @Override
    public void setPublisher(String publisher) {
        super.setPublisher(publisher);
    }



}
