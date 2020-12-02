package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;

import java.util.ArrayList;
import java.util.Calendar;

public class ReadingBookAdapter extends RecyclerView.Adapter<ReadingBookAdapter.BookViewHolder>
{
    // todo static 수정하기
    static ArrayList<Book> readingBookList = new ArrayList<>();

    RatingBar ratingBar;
    int position;
    Book book;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder
    {
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

            // 아이템을 클릭하면 책 정보를 인텐트에 담아서 책 정보 확인 페이지로 화면 전환한다.
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        book = getItem(position);
                        Intent intent = new Intent(v.getContext(), ReadingBookInfoActivity.class);

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

            // 책을 길게 클릭하면 별점을 입력받는 다이얼로그를 띄운다.
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    position = getAdapterPosition();
                    ShowDialog(v, position);
                    return true; // 롱클릭 이벤트 이후 클릭 이벤트 발생하지 않도록 true 반환
                }
            });
        }
    }

    // 기본 생성자
    ReadingBookAdapter(){}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    // todo 이유 작성
    ReadingBookAdapter(ArrayList<Book> readingBookList) {
        this.readingBookList = readingBookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ReadingBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reading_book, parent, false);
        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ReadingBookAdapter.BookViewHolder holder, int position)
    {
        Book book = readingBookList.get(position);

        holder.imageView_img.setImageDrawable(book.getImg());
        holder.textView_title.setText(book.getTitle());
        holder.textView_start_date.setText(book.getStartDate());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return readingBookList.size() ;
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
        book.setStartDate(today);
        book.setState("reading");
        readingBookList.add(0, book);
        notifyItemInserted(0);
    }

    // 아이템 삭제 메소드
    public void removeItem(int position)
    {
        readingBookList.remove(position);
        notifyItemRemoved(position);
    }

    // 아이템 가져오는 메소드
    public Book getItem(int position) {
        return readingBookList.get(position);
    }

    public void ShowDialog(View v, int position)
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(v.getContext());

        LinearLayout linearLayout = new LinearLayout(v.getContext());
        ratingBar = new RatingBar(v.getContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ratingBar.setLayoutParams(lp);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        // todo 세팅하는 이유 설명, 남을 위해서 주석을 작성하도록 하기
        linearLayout.addView(ratingBar);
        popDialog.setTitle("독서를 마칠까요?\n별점을 입력해주세요. ");
        popDialog.setView(linearLayout);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        // Button OK
        popDialog.setPositiveButton("확인",
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // 해당하는 책을 찾아서 입력받은 별점을 저장하고 읽는 책 → 읽은 책 리스트로 이동시킨다.
                    book = getItem(position);
                    book.setStarNum((int)ratingBar.getRating());
                    ReadBookAdapter readBookAdapter = new ReadBookAdapter();
                    readBookAdapter.addItem(book);

                    removeItem(position);
                    dialog.dismiss();
                }
            })

            // Button Cancel
            .setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(v.getContext(), "취소" ,Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

        popDialog.create();
        popDialog.show();

    }
}
