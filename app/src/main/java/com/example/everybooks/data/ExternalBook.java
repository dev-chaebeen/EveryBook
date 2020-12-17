package com.example.everybooks.data;

public class ExternalBook extends Book
{
    private String description;
    private String imgFilePath;
    private String link;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
