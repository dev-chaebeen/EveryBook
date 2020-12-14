package com.example.everybooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.everybooks.data.Memo;
import com.example.everybooks.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class  AllMemoAdapter extends RecyclerView.Adapter<AllMemoAdapter.ViewHolder>
{
    static ArrayList<Memo> allMemoList = new ArrayList<>();

    int position;
    Memo memo;
    Context context;
    // test
    TextToSpeech tts;

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
                        // 메모를 클릭했을 때 각 메모에 해당하는 책의 상세정보 화면으로 전환하기 위해서
                        // 클릭한 아이템의 메모 객체를 가져온 뒤 메모의 bookId 를 얻는다.
                        memo = getItem(position);
                        int bookId = memo.getBookId();
                        String state = "";

                        // 저장해둔 책 리스트를 얻어와서 bookId 에 해당하는 책의 독서 상태를 가져온다.
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

                        // 책의 독서상태가 reading 이라면 ReadingBookInfoActivity 로 화면을 전환한다.
                        // ReadingBookInfoActivity 에서 bookId 를 통해 해당하는 책의 정보를 가져올 수 있도록
                        // 인텐트에 bookId 를 담아서 화면을 전환한다.
                        if(state.equals("reading"))
                        {
                            Intent intent = new Intent(v.getContext(), ReadingBookInfoActivity.class);
                            intent.putExtra("bookId", memo.getBookId());
                            v.getContext().startActivity(intent);
                        }
                        // 책의 독서상태가 read 라면 ReadBookInfoActivity 로 화면을 전환한다.
                        // ReadBookInfoActivity 에서 bookId 를 통해 해당하는 책의 정보를 가져올 수 있도록
                        // 인텐트에 bookId 를 담아서 화면을 전환한다.
                        else if(state.equals("read"))
                        {
                            Intent intent = new Intent(v.getContext(), ReadBookInfoActivity.class);
                            intent.putExtra("bookId", memo.getBookId());
                            v.getContext().startActivity(intent);
                        }
                    }
                }
            });



            // 아이템을 길게 클릭하면 메모를 읽어준다.
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        // 메모를 클릭했을 때 각 메모에 해당하는 책의 상세정보 화면으로 전환하기 위해서
                        // 클릭한 아이템의 메모 객체를 가져온 뒤 메모의 bookId 를 얻는다.
                        memo = getItem(position);

                        System.out.println("-------------------------------------- 음성출력 시작!");
                        String totalSpeak = memo.getMemoText();

                        tts.setPitch(1.5f); //1.5톤 올려서
                        tts.setSpeechRate(1.0f); //1배속으로 읽기
                        tts.speak(totalSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    return true; // 롱클릭 이벤트 이후 클릭 이벤트 발생하지 않도록 true 반환
                }
            });
        }
    }

    public AllMemoAdapter(){}

    public AllMemoAdapter(Context context)
    {
        this.context = context;
    }

    public AllMemoAdapter(Context context, ArrayList<Memo> arrayList)
    {
        this.context = context;
        this.allMemoList = arrayList;
    }

    public AllMemoAdapter(Context context, TextToSpeech tts)
    {
        this.context = context;
        this.tts = tts;
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

        // 메모에 저장되어 있는 bookId 에 해당하는 title, img 가져온다.
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

        // 가져온 데이터를 뷰 요소에 배치해준다.
        // 이미지의 경우 문자열로 저장되어있으므로 bitmap 으로 변환해주는 함수를 이용한다.
        Util util = new Util();
        Bitmap bitmap = util.stringToBitmap(img);
        holder.imageView_img.setImageBitmap(bitmap);
        holder.textView_title.setText(title);
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
