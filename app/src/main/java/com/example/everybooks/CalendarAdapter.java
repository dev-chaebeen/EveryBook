package com.example.everybooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.everybooks.data.Book;
import com.example.everybooks.data.Util;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter
{
    // todo static 수정하기
    static ArrayList<Book> theDayBookList = new ArrayList<>();
    Context context;

    public CalendarAdapter(){}

    public  CalendarAdapter(Context context, ArrayList<Book> arrayList)
    {
        this.context = context;
        this.theDayBookList = arrayList;
    }

    @Override
    public int getCount() {
        return theDayBookList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return theDayBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       final int index = position;
       final Context context = parent.getContext();

        // item_calendar_book 레이아웃을 inflate 하여 convertView 참조 획득
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_calendar_book, parent, false);
        }

        // 뷰 요소 초기화
        ImageView imageView_img = convertView.findViewById(R.id.img);
        TextView textView_title = convertView.findViewById(R.id.title);
        TextView textView_writer = convertView.findViewById(R.id.writer);
        TextView textView_publisher = convertView.findViewById(R.id.publisher);
        TextView textView_publish_date = convertView.findViewById(R.id.publish_date);

        // 책 객체로부터 데이터를 얻어서 해당하는 뷰 요소에 나타낸다.
        Book book = theDayBookList.get(position);

        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(book.getImg());
        imageView_img.setImageBitmap(bitmap);
        textView_title.setText(book.getTitle());
        textView_writer.setText(book.getWriter());
        textView_publisher.setText(book.getPublisher());
        //textView_publish_date.setText(book.getPublishDate());

        return convertView;
    }
}
