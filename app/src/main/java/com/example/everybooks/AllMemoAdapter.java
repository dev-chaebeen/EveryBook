package com.example.everybooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Memo;
import com.example.everybooks.data.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class  AllMemoAdapter extends RecyclerView.Adapter<AllMemoAdapter.ViewHolder>
{
    static ArrayList<Memo> allMemoList = new ArrayList<>();

    int position;
    Memo memo;
    Context context;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // 뷰 요소 선언
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_memo_text;
        TextView textView_memo_date;

        ViewHolder(View itemView)
        {
            super(itemView);

            // 뷰 요소 초기화
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_memo_text = itemView.findViewById(R.id.memo_text);
            textView_memo_date = itemView.findViewById(R.id.memo_date);

            // 아이템을 클릭하면 해당 메모에 해당하는 상세정보 화면으로 전환한다.
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        memo = getItem(position);
                        int bookId = memo.getBookId();
                        String state = "";

                        SharedPreferences bookInfo = v.getContext().getSharedPreferences("bookInfo", Context.MODE_PRIVATE);
                        String bookListString = bookInfo.getString("bookList", null);

                        try
                        {
                            JSONArray jsonArray = new JSONArray(bookListString);
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if(bookId == jsonObject.getInt("bookId"))
                                {
                                    state = jsonObject.getString("state");
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }


                        if(state.equals("reading"))
                        {
                            Intent intent = new Intent(v.getContext(), ReadingBookInfoActivity.class);
                            intent.putExtra("bookId", memo.getBookId());
                            v.getContext().startActivity(intent);
                        }
                        else if(state.equals("read"))
                        {
                            Intent intent = new Intent(v.getContext(), ReadBookInfoActivity.class);
                            intent.putExtra("bookId", memo.getBookId());
                            v.getContext().startActivity(intent);
                        }
                    }
                }
            });

            // 아이템을 길게 클릭하면 메모를 삭제하겠냐는 다이얼로그가 등장한다.
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("메모를 삭제하시겠습니까?.");
                    builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                           MemoAdapter.memoList.remove(position);
                           notifyDataSetChanged();
                           dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Toast.makeText( v.getContext(), "취소" ,Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.show();
                    return true; // 롱클릭 이벤트 이후 클릭 이벤트 발생하지 않도록 true 반환
                }
            });
        }
    }

    // 기본 생성자
    public AllMemoAdapter(){}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public AllMemoAdapter(Context context)
    {
        this.context = context;
    }

    public AllMemoAdapter(Context context, ArrayList<Memo> arrayList)
    {
        this.context = context;
        this.allMemoList = arrayList;
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴하는 메소드
    @Override
    public AllMemoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_memo, parent, false);

        return new ViewHolder(view);
    }

    // onBindViewHolder() - position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시하는 메소드
    @Override
    public void onBindViewHolder(AllMemoAdapter.ViewHolder holder, int position)
    {
        Memo memo = allMemoList.get(position);

        // 메모에 저장되어 있는 bookId 로 해당하는 title, img 가져오기
        SharedPreferences bookInfo = context.getSharedPreferences("bookInfo", Context.MODE_PRIVATE);
        String bookListString = bookInfo.getString("bookList", null);

        String title="";
        String img="";


        try
        {
            JSONArray jsonArray = new JSONArray(bookListString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(memo.getBookId() == jsonObject.getInt("bookId"))
                {
                   title = jsonObject.getString("title");
                   img = jsonObject.getString("img");
                }


            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        holder.textView_title.setText(title);
        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(img);
        holder.imageView_img.setImageBitmap(bitmap);
        holder.textView_memo_text.setText(memo.getMemoText());
        holder.textView_memo_date.setText(memo.getMemoDate());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return allMemoList.size() ;
    }

    // 아이템 삭제 메소드
    public void removeItem(int position)
    {
        allMemoList.remove(position);
        notifyItemRemoved(position);
    }

    // 아이템 가져오는 메소드
    public Memo getItem(int position) {
        return allMemoList.get(position);
    }
}
