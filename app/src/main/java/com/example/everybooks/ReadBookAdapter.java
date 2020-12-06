package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;
import com.example.everybooks.data.Util;

import java.util.ArrayList;
import java.util.Calendar;

public class ReadBookAdapter extends RecyclerView.Adapter<ReadBookAdapter.BookViewHolder> {

    static ArrayList<Book> readBookList = new ArrayList<>() ;

    int position;
    Book book;

    Context context;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_img;
        TextView textView_title;
        TextView textView_writer;
        RatingBar ratingBar_rate;

        // 생성자
        BookViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_writer = itemView.findViewById(R.id.writer);
            ratingBar_rate = itemView.findViewById(R.id.rate);


            // 각각의 아이템을 클릭하면 책 정보 페이지로 화면 전환한다.
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        book = getItem(position);

                        // bookId 로 해당하는 책 정보를 얻어올 수 있도록 인텐트에 bookId 를 담아서 화면을 전환한다.
                        Intent intent = new Intent(v.getContext(), ReadBookInfoActivity.class);
                        intent.putExtra("bookId", book.getBookId());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    // 기본 생성자
    ReadBookAdapter(){}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ReadBookAdapter(Context context, ArrayList<Book> ReadBookList)
    {
        this.context = context;
        this.readBookList = ReadBookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ReadBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_read_book, parent, false);
        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ReadBookAdapter.BookViewHolder holder, int position)
    {
        Book book = readBookList.get(position);

        // 문자열 이미지 비트맵으로 변환
        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(book.getImg());
        holder.imageView_img.setImageBitmap(bitmap);
        holder.textView_title.setText(book.getTitle());
        holder.textView_writer.setText(book.getWriter());
        holder.ratingBar_rate.setRating(book.getStarNum());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return readBookList.size() ;
    }

    // 아이템 추가 메소드
    public void addItem(Book book)
    {
        //현재 년도, 월, 일
        Calendar cal = Calendar.getInstance();

        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        int date = cal.get ( cal.DATE ) ;

        String today = year + "." + month + "." + date;
        book.setEndDate(today);
        book.setState("read");

        readBookList.add(0,book);
        notifyItemInserted(0);
    }

    // 아이템 삭제 메소드
    public void removeItem(int position)
    {
        readBookList.remove(position);
        notifyItemRemoved(position);
    }

    // 아이템 가져오는 메소드
    public Book getItem(int position) {
        return readBookList.get(position);
    }
}
