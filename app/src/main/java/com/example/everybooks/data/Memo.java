package com.example.everybooks.data;

public class Memo
{
    //todo static 수정
    int bookId;
    int memoId;
    String memoText;
    String memoDate;

    // 객체를 구분하는 id를 객체마다 가지도록 하기 위해서
    // 객체를 생성할 때마다 memoId 가 1씩 증가하도록 한다.
    public Memo()
    {
        this.memoId = memoId+1;
    }

   public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getMemoText() {
        return memoText;
    }

    public void setMemoText(String memoText) {
        this.memoText = memoText;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

}
