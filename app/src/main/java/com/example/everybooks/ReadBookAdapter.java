package com.example.everybooks;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReadBookAdapter extends RecyclerView.Adapter<ReadBookAdapter.BookViewHolder> {

    // todo static 수정하기
    static ArrayList<Book> ReadBookList = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_img;
        TextView textView_title;
        RatingBar ratingBar_rate;

        // 생성자
        BookViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            ratingBar_rate = itemView.findViewById(R.id.rate);


            // 각각의 아이템을 클릭하면 책 정보 수정 페이지로 화면 전환한다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        Intent intent = new Intent(v.getContext(), ReadBookInfoActivity.class);

                        // todo 데이터 담고 이동하도록
                        v.getContext().startActivity(intent);

                    }
                }
            });

        }


    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ReadBookAdapter(ArrayList<Book> ReadBookList) {
        this.ReadBookList = ReadBookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ReadBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_book, parent, false) ;
        ToReadBookAdapter.ViewHolder viewHolder = new ToReadBookAdapter.ViewHolder(view) ;*/

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_read_book, parent, false);

        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ReadBookAdapter.BookViewHolder holder, int position) {
        Book book = ReadBookList.get(position);

        // holder.imageView_img.set...
        holder.textView_title.setText(book.getTitle());
        holder.ratingBar_rate.setRating(book.getStarNum());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return ReadBookList.size() ;
    }


}
