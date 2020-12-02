package com.example.everybooks.data;

public class Notification
{
    //todo static 수정
    static int notiId;
    String text;
    String[] days;
    int hour;
    int minute;

    boolean isOn;

    // 객체를 구분하는 id를 객체마다 가지도록 하기 위해서
    // 객체를 생성할 때마다 notiId 가 1씩 증가하도록 한다.
    public Notification()
    {
        this.notiId = notiId+1;
    }

    public int getNotiId() {
        return notiId;
    }

    public void setNotiId(int notiId) {
        this.notiId = notiId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
