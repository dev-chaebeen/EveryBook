package com.example.everybooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Book;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class ToReadBookAdapter extends RecyclerView.Adapter<ToReadBookAdapter.BookViewHolder>
{
    private int position;
    Book book;
    Context context;


    public ToReadBookAdapter(Context context)
    {
        this.context = context;
    }


    // todo static 수정하기
    static ArrayList<Book> toReadBookList = new ArrayList<>();

    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_insert_date;

        BookViewHolder(View itemView) {
            super(itemView) ;

           // 뷰 요소 초기화
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_insert_date = itemView.findViewById(R.id.insert_date);

            // 각각의 아이템을 클릭하면 책 데이터를 가지고 책 정보수정 화면으로 전환한다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        book = getItem(position);
                        Intent intent = new Intent(v.getContext(), EditBookInfoActivity.class);
                        //intent.putExtra("img", book.getImg());
                        intent.putExtra("title", book.getTitle());
                        intent.putExtra("writer", book.getWriter());
                        intent.putExtra("publisher", book.getPublisher());
                        intent.putExtra("publishDate", book.getPublishDate());
                        intent.putExtra("position", position);
                        intent.putExtra("state", book.getState());
                        v.getContext().startActivity(intent);

                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //AlertDialog.Builder builder = new AlertDialog.Builder(ToReadBookAdapter.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("독서를 시작할까요?");
                    builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // 읽을 책 → 읽는 책 리스트로 이동시킨다.
                                // 해당하는 책을 찾아서 readingBookList에 추가하고 toReadbookList에서 삭제한다.
                                position = getAdapterPosition();
                                Book book = getItem(position);

                                ReadingBookAdapter readingBookAdapter = new ReadingBookAdapter();
                                readingBookAdapter.addItem(book);

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
                    return true; // 롱클릭 이벤트 이후 클릭이벤트 발생하지 않도록 true 반환
                }
            });
        }
    }

    ToReadBookAdapter(){}

    ToReadBookAdapter(ArrayList<Book> toReadBookList) {
        //this.toReadBookList = toReadBookList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴하는 메소드
    @Override
    public ToReadBookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_read_book, parent, false);

        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시하는 메소드
    @Override
    public void onBindViewHolder(ToReadBookAdapter.BookViewHolder holder, int position) {
        Book book = toReadBookList.get(position);

        holder.imageView_img.setImageDrawable(book.getImg());
        holder.textView_title.setText(book.getTitle());
        holder.textView_insert_date.setText(book.getInsertDate());

    }

    // getItemCount() - 전체 데이터 갯수 리턴하는 메소드
    @Override
    public int getItemCount() {
        return toReadBookList.size() ;
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
        //현재 년도, 월, 일을 책 등록일에 저장한다.
        Calendar cal = Calendar.getInstance();

        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        int date = cal.get ( cal.DATE ) ;

        String today = year + "." + month + "." + date;
        book.setInsertDate(today);
        book.setState("toRead");
        toReadBookList.add(0,book);
        notifyItemInserted(0);
    }

    // 아이템 삭제 메소드
    public void removeItem(int position)
    {
        toReadBookList.remove(position);
        notifyItemRemoved(position);
    }

    // 아이템 가져오는 메소드
    public Book getItem(int position) {
        return toReadBookList.get(position);
    }

    private ArrayList getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

}
