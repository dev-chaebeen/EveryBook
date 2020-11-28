package com.example.everybooks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class ReadingBookAdapter extends RecyclerView.Adapter<ReadingBookAdapter.BookViewHolder> {

    // todo static 수정하기
    static ArrayList<Book> readingBookList = new ArrayList<>();

    RatingBar ratingBar;
    int position;
    Book book;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_start_date;

        // 생성자
        BookViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_start_date = itemView.findViewById(R.id.start_date);

            // 각각의 아이템을 클릭하면 책 정보  페이지로 화면 전환한다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        book = getItem(position);
                        Intent intent = new Intent(v.getContext(), ReadingBookInfoActivity.class);

                        // todo 데이터 담고 이동하도록
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


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    position = getAdapterPosition();
                    ShowDialog(v, position);
                    /*
                    //AlertDialog.Builder builder = new AlertDialog.Builder(ToReadBookAdapter.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.rating_dialog);

                    //ratingBar = (RatingBar)dialog.findViewById(R.id.rate);
                    builder.setTitle(" 독서를 마칠까요? \n 별점을 입력해주세요.");

                    // view 는 하나의 부모 뷰에만 추가될 수 있는데 다이얼로그를 여러번 띄우면
                    // 중복으로 view 가 참조되어 에러를 일으킨다.
                    // 따라서 뷰의 참조 여부를 확인한 후 setView() 메소드를 사용한다.
                    if (ratingBar.getParent() != null)
                    {
                        ((ViewGroup) ratingBar.getParent()).removeView(ratingBar);
                    }

                    builder.setView(ratingBar);


                    builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // 읽는 책 → 읽은 책 리스트로 이동시킨다.
                                // 해당하는 책을 찾아서 readBookList 에 추가하고 readingBookList 에서 삭제한다.
                                position = getAdapterPosition();
                                //Book book = getItem(position);
                                book = getItem(position);

                                ReadBookAdapter readBookAdapter = new ReadBookAdapter();
                                readBookAdapter.addItem(book);

                                removeItem(position);

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
                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("독서를 마무리할까요?");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // 별점을 입력받고


                                    // 읽는 책 → 읽은 책 리스트로 이동시킨다.
                                    // 해당하는 책을 찾아서 readBookList 에 추가하고 readingBookList 에서 삭제한다.
                                    position = getAdapterPosition();
                                    Book book = getItem(position);

                                    ReadBookAdapter readBookAdapter = new ReadBookAdapter();
                                    readBookAdapter.addItem(book);

                                    removeItem(position);

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
                    */
                    return true; // 롱클릭 이벤트 이후 클릭이벤트 발생 xx

                }
            });


        }

    }

    // 기본 생성자
    ReadingBookAdapter(){}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ReadingBookAdapter(ArrayList<Book> readingBookList) {
        this.readingBookList = readingBookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ReadingBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reading_book, parent, false);

        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ReadingBookAdapter.BookViewHolder holder, int position) {
        Book book = readingBookList.get(position);

        // holder.imageView_img.set...
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

        //add ratingBar to linearLayout
        linearLayout.addView(ratingBar);

        //popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("독서를 마치겠습니까?\n별점을 입력해주세요. ");

        //add linearLayout to dailog
        popDialog.setView(linearLayout);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });


        // Button OK
        popDialog.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 해당하는 책을 찾아서 입력받은 별점을 세팅하고 읽는 책 → 읽은 책 리스트로 이동시킨다.
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
