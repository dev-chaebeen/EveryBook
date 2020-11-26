package com.example.everybooks;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToReadBookAdapter extends RecyclerView.Adapter<ToReadBookAdapter.BookViewHolder> {

    // todo static 수정하기
    static ArrayList<Book> bookList = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_sub_title;
        TextView textView_insert_date;
        //RatingBar ratingBar_rate;  따로 해야 하나 ?

        // 생성자
        BookViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_sub_title = itemView.findViewById(R.id.sub_title);
            textView_insert_date = itemView.findViewById(R.id.insert_date);

            // 각각의 아이템을 클릭하면 책 정보 수정 페이지로 화면 전환한다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        Intent intent = new Intent(v.getContext(), EditBookInfoActivity.class);
                        v.getContext().startActivity(intent);

                    }
                }
            });

        }


    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ToReadBookAdapter(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ToReadBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_book, parent, false) ;
        ToReadBookAdapter.ViewHolder viewHolder = new ToReadBookAdapter.ViewHolder(view) ;*/

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_read_book, parent, false);

        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ToReadBookAdapter.BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        // holder.imageView_img.set...
        holder.textView_title.setText(book.getTitle());
        holder.textView_sub_title.setText("등록일");   // 읽을 책 : 등록일 / 읽는 책 : 시작일 / 읽은 책 : 작가이름
        holder.textView_insert_date.setText(book.getInsertDate());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return bookList.size() ;
    }
}
