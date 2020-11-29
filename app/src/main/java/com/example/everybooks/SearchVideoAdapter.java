package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchVideoAdapter extends RecyclerView.Adapter<SearchVideoAdapter.BookViewHolder> {

    private int position;
    Video video;
    Intent intent;

    // todo static 수정하기
    static ArrayList<Video> searchVideoList = new ArrayList<>();

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_img;
        TextView textView_title;
        TextView textView_channel;
        TextView textView_description;

        // 생성자
        BookViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조.
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_channel = itemView.findViewById(R.id.channel);
            textView_description = itemView.findViewById(R.id.description);

            // 각각의 아이템을 클릭하면 인텐트를 이용해 유튜브 실행한다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=gShDtRwTLXw"));
                    intent.setPackage("com.google.android.youtube");
                    v.getContext().startActivity(intent);

                }
            });
        }
    }

    // 기본 생성자
    SearchVideoAdapter(){}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    SearchVideoAdapter(ArrayList<Video> searchVideoList) {
        this.searchVideoList = searchVideoList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SearchVideoAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_video, parent, false);

        return new BookViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SearchVideoAdapter.BookViewHolder holder, int position) {
        Video video = searchVideoList.get(position);

        holder.imageView_img.setImageDrawable(video.getImg());
        holder.textView_title.setText(video.getTitle());
        holder.textView_channel.setText(video.getChannel());
        holder.textView_description.setText(video.getDescription());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return searchVideoList.size() ;
    }

    public int getPosition(int position)
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }


    // 아이템 가져오는 메소드
    public Video getItem(int position) {
        return searchVideoList.get(position);
    }

}
