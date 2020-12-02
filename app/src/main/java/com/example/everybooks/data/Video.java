package com.example.everybooks.data;

import android.graphics.drawable.Drawable;

public class Video
{
    //todo static 수정
    static int videoId;
    String title;
    String channel;
    String description;
    Drawable img;


    // 객체를 구분하는 id를 객체마다 가지도록 하기 위해서
    // 객체를 생성할 때마다 bookId 가 1씩 증가하도록 한다.
    public Video()
    {
        this.videoId = videoId+1;
    }


    public static int getVideoId() {
        return videoId;
    }

    public static void setVideoId(int videoId) {
        Video.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
