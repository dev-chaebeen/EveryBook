package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.everybooks.data.Book;
import com.example.everybooks.data.ExternalBook;
import com.example.everybooks.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.BookViewHolder>
{
    private int position;
    ExternalBook searchBook;
    Context context;
    int bookId;
    String bookListString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Intent intent;
    static ArrayList<ExternalBook> searchBookList = new ArrayList<>();

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_writer;
        TextView textView_comment;

        // 생성자
        BookViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조.
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_writer = itemView.findViewById(R.id.writer);
            textView_comment = itemView.findViewById(R.id.comment);

            // 아이템을 클릭하면 책을 추가할거냐고 묻는다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 책 추가 이미지 클릭하면 팝업 메뉴를 띄운다.
                    // 1. 카메라  2.갤러리  3.기본이미지
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.book_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch(menuItem.getItemId())
                            {
                                case R.id.link :
                                    position = getAdapterPosition();
                                    searchBook = getItem(position);

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchBook.getLink()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);

                                    break;

                                case R.id.add_book :

                                    // 읽을 책 리스트에 추가한다.
                                    position = getAdapterPosition();
                                    searchBook = getItem(position);

                                    //addItem(book);

                                    // ImageView 의 이미지를 SharedPreference 에 저장하기 위해서
                                    // ImageView 의 resource 를 Bitmap 으로 가져온 뒤
                                    BitmapDrawable drawable = (BitmapDrawable) imageView_img.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();

                                    // Bitmap 을 문자열로 바꿔서 이미지를 문자열 변수에 저장한다.
                                    Util util = new Util();
                                    String imgString = util.bitMapToString(bitmap);

                                    // 책들을 구분하는 값을 저장하기 위해서 bookId 를 사용한다.
                                    // bookInfo 라는 SharedPreferences 파일에서 bookId를 가져온다.
                                    // 저장된 값이 존재하지 않는다면 0을 가져온다.
                                    SharedPreferences bookInfo = context.getSharedPreferences("bookInfo", MODE_PRIVATE);
                                    bookId = bookInfo.getInt("bookId", 0);

                                    // 아이템의 정보를 book 객체에 저장한 뒤 JsonArray 에 저장하기 위해서 jsonObject 형태로 바꾼다.
                                    Book book = new Book();
                                    book.setBookId(bookId);
                                    book.setImg(imgString);
                                    book.setTitle(searchBook.getTitle());
                                    book.setWriter(searchBook.getWriter());
                                    book.setPublisher(searchBook.getPublisher());
                                    book.setPublishDate(searchBook.getPublishDate());
                                    book.setState("toRead");

                                    Calendar cal = Calendar.getInstance();
                                    int year = cal.get ( cal.YEAR );
                                    int month = cal.get ( cal.MONTH ) + 1 ;
                                    int date = cal.get ( cal.DATE ) ;

                                    String today = year + "." + month + "." + date;
                                    book.setInsertDate(today);
                                    book.setStartDate("");
                                    book.setEndDate("");
                                    book.setReadTime("00:00:00");
                                    book.setStarNum(0);

                                    try
                                    {
                                        JSONObject bookJson = new JSONObject();

                                        bookJson.put("bookId", book.getBookId());
                                        bookJson.put("img", book.getImg());
                                        bookJson.put("title", book.getTitle());
                                        bookJson.put("writer", book.getWriter());
                                        bookJson.put("publisher", book.getPublisher());
                                        bookJson.put("publishDate", book.getPublishDate());
                                        bookJson.put("state", book.getState());
                                        bookJson.put("insertDate", book.getInsertDate());
                                        bookJson.put("startDate", book.getStartDate());
                                        bookJson.put("endDate", book.getEndDate());
                                        bookJson.put("readTime", book.getReadTime());
                                        bookJson.put("starNum", book.getStarNum());

                                        // 책을 구분하기 위해 저장된 책의 bookId 가 겹치지 않도록 bookInfo 에 저장된 bookId의 값을 1 증가시킨다.
                                        SharedPreferences.Editor editor = bookInfo.edit();
                                        editor.putInt("bookId", bookId + 1);
                                        editor.commit();

                                        // SharedPreference bookInfo 파일에서 "toReadBookLIst" 키로 저장된 책 리스트를 불러온다.
                                        bookListString = bookInfo.getString("bookList", null);

                                        // 저장된 책 리스트가 있다면 문자열을 JsonArray 형식으로 바꾼 뒤
                                        // 사용자의 입력값을 저장해둔 jsonObject 를 추가한다.
                                        if(bookListString != null)
                                        {
                                            jsonArray = new JSONArray(bookListString);
                                            jsonArray.put(bookJson);
                                            bookListString = jsonArray.toString();
                                            editor.putString("bookList", bookListString);
                                            editor.commit();
                                        }
                                        else
                                        {
                                            // 저장된 책 리스트가 없다면 JsonArray 를 만들어서
                                            // 사용자의 입력값을 저장해둔 jsonObject 를 추가한다.
                                            jsonArray = new JSONArray();
                                            jsonArray.put(bookJson);
                                            bookListString = jsonArray.toString();
                                            editor.putString("bookList", bookListString);
                                            editor.commit();
                                        }

                                        Log.d("recommendBookAdapter", " bookListString : " + bookListString);

                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(e.toString());
                                    }

                                    intent = new Intent(context, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);

                                    break;

                                case R.id.cancel :
                                    Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return true;
                        }
                    });

                    popupMenu.show();// 팝업 메뉴 보이기

                }
            });
        }
    }

    SearchBookAdapter(){}

    SearchBookAdapter(Context context, ArrayList<ExternalBook> searchBookList)
    {
        this.context = context;
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
        ExternalBook book = searchBookList.get(position);

        Glide.with(holder.itemView.getContext()).load(book.getImgFilePath()).into(holder.imageView_img);

        holder.textView_title.setText(book.getTitle());
        holder.textView_writer.setText(book.getWriter());
        holder.textView_comment.setText(book.getDescription());

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
    public void addItem(ExternalBook book)
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
    public ExternalBook getItem(int position) {
        return searchBookList.get(position);
    }

}
