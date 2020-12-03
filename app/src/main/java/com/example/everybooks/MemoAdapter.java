package com.example.everybooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.everybooks.data.Memo;


public class MemoAdapter extends BaseAdapter
{
    // todo static 수정하기
    static ArrayList<Memo> memoList = new ArrayList<>();

    public MemoAdapter(){}

    public MemoAdapter(ArrayList<Memo> arrayList)
    {
        this.memoList = arrayList;
    }
    // 데이터 추가 메소드
    public void addItem(int bookId, String memoText)
    {
        Memo memo = new Memo();
        memo.setBookId(bookId);
        memo.setMemoText(memoText);

        // 생성자에서 증가시킨다
      /*  if(Memo.memoId == 0)
        {
            memo.setMemoId(0);
            Memo.memoId = Memo.memoId + 1;
        }
        else
        {
            memo.setMemoId(Memo.memoId);
        }*/

        //현재 년도, 월, 일
        Calendar cal = Calendar.getInstance();

        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        int date = cal.get ( cal.DATE );
        int hour = cal.get(cal.HOUR_OF_DAY);
        int minute = cal.get(cal.MINUTE);
        int second = cal.get(cal.SECOND);

        String today = year + "." + month + "." + date + " " + hour + ":" + minute +":" + second;
        memo.setMemoDate(today);

        memoList.add(0, memo);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return memoList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return memoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       final int index = position;
       final Context context = parent.getContext();

       // 재활용할 뷰가 null 인 경우에만 inflate 한다.(xml에 정의되어있는 view를 실제 view 객체로 만든다.)
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_memo, parent, false);
        }

        // 뷰 요소 초기화
        TextView textView_memo_text = convertView.findViewById(R.id.memo_text);
        TextView textView_memo_date = convertView.findViewById(R.id.memo_date);

        Memo memo = memoList.get(position);

        // 뷰 요소에 나타내주기
        textView_memo_text.setText(memo.getMemoText());
        textView_memo_date.setText(memo.getMemoDate());

        return convertView;
    }
}
