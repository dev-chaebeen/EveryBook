package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.BookViewHolder>
{
    private int position;
    Book book;

    // todo static 수정하기
    static ArrayList<Book> searchBookList = new ArrayList<>();

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_writer;
        TextView textView_plot;

        // 생성자
        BookViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조.
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_writer = itemView.findViewById(R.id.writer);
            textView_plot = itemView.findViewById(R.id.plot);

            // 아이템을 클릭하면 책을 추가할거냐고 묻는다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("책을 추가할까요?");
                    builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // 읽을 책 리스트에 추가한다.
                                position = getAdapterPosition();
                                //Book book = getItem(position);
                                //addItem(book);

                                dialog.dismiss();

                            }
                        });
                    builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // 취소 클릭했을 때
                                Toast.makeText(v.getContext(), "취소" ,Toast.LENGTH_SHORT).show();
                            }
                        });
                    builder.show();
                }
            });
        }
    }

    SearchBookAdapter(){}

    SearchBookAdapter(ArrayList<Book> searchBookList) {
        this.searchBookList = searchBookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴하는 메소드
    @Override
    public SearchBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_book, parent, false);
        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시하는 메소드
    @Override
    public void onBindViewHolder(SearchBookAdapter.BookViewHolder holder, int position)
    {
        Book book = searchBookList.get(position);

        holder.imageView_img.setImageDrawable(book.getImg());
        holder.textView_title.setText(book.getTitle());
        holder.textView_writer.setText(book.getWriter());
        holder.textView_plot.setText(book.getPlot());

    }

    // getItemCount() - 전체 데이터 갯수 리턴하는 메소드
    @Override
    public int getItemCount() {
        return searchBookList.size() ;
    }

    public int getPosition(int position)
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
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
        book.setInsertDate(today);
        book.setState("toRead");
        searchBookList.add(0,book);
        notifyItemInserted(0);
    }

    // 아이템 삭제 메소드
    public void removeItem(int position)
    {
        searchBookList.remove(position);
        notifyItemRemoved(position);
    }

    // 아이템 가져오는 메소드
    public Book getItem(int position) {
        return searchBookList.get(position);
    }

}
