package com.example.everybooks;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectBookAdapter extends RecyclerView.Adapter<SelectBookAdapter.BookViewHolder>
{
    ArrayList<Book> selectBookList = ReadingBookAdapter.readingBookList;

    int position;
    Book book;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder
    {
        // 뷰 요소 선언
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_start_date;

        BookViewHolder(View itemView)
        {
            super(itemView) ;

            // 뷰 요소 초기화
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_start_date = itemView.findViewById(R.id.start_date);

            // 각각의 아이템을 클릭하면 인텐트에 책 정보를 담아서 시간 측정 화면으로 전환한다.
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        book = getItem(position);
                        Intent intent = new Intent(v.getContext(), TimeRecordActivity.class);

                        // intent.putExtra("img", book.getImg());
                        // readTime
                        intent.putExtra("bookId", book.getBookId());
                        intent.putExtra("title", book.getTitle());
                        intent.putExtra("writer", book.getWriter());
                        intent.putExtra("publisher", book.getPublisher());
                        intent.putExtra("publishDate", book.getPublishDate());
                        intent.putExtra("startDate", book.getStartDate());
                        intent.putExtra("position", position);
                        intent.putExtra("state", book.getState());
                        v.getContext().startActivity(intent);

                    }
                }
            });
        }
    }

    SelectBookAdapter(){}

    SelectBookAdapter(ArrayList<Book> selectBookList) {
        this.selectBookList = selectBookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴하는 메소드
    @Override
    public SelectBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reading_book, parent, false);
        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시하는 메소드
    @Override
    public void onBindViewHolder(SelectBookAdapter.BookViewHolder holder, int position) {
        Book book = selectBookList.get(position);

        // holder.imageView_img.set...
        holder.imageView_img.setImageDrawable(book.getImg());
        holder.textView_title.setText(book.getTitle());
        holder.textView_start_date.setText(book.getStartDate());

    }

    // getItemCount() - 전체 데이터 갯수 리턴하는 메소드
    @Override
    public int getItemCount() {
        return selectBookList.size() ;
    }


    // 아이템 추가 메소드
    public void addItem(Book book)
    {
        book.setState("reading");
        selectBookList.add(0, book);
        notifyItemInserted(0);
    }

    // 아이템 삭제 메소드
    public void removeItem(int position)
    {
        selectBookList.remove(position);
        notifyItemRemoved(position);
    }

    // 아이템 가져오는 메소드
    public Book getItem(int position) {
        return selectBookList.get(position);
    }


}
