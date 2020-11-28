package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class AllMemoAdapter extends RecyclerView.Adapter<AllMemoAdapter.ViewHolder> {

    // todo static 수정하기
    static ArrayList<Memo> allMemoList =  MemoAdapter.memoList;

    int position;
    Memo memo;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView_img;
        TextView textView_title;
        TextView textView_memo_text;
        TextView textView_memo_date;

        // 생성자
        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView_img = itemView.findViewById(R.id.img);
            textView_title = itemView.findViewById(R.id.title);
            textView_memo_text = itemView.findViewById(R.id.memo_text);
            textView_memo_date = itemView.findViewById(R.id.memo_date);

            // 각각의 아이템을 클릭하면 메모 편집화면으로 전환한다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        memo = getItem(position);
                        Intent intent = new Intent(v.getContext(), EditMemoActivity.class);

                        // todo 데이터 담고 이동하도록
                        // readTime
                        intent.putExtra("memoId", memo.getMemoId());
                        intent.putExtra("position", position);
                        intent.putExtra("bookId", memo.getBookId());
                        intent.putExtra("title", textView_title.getText().toString());
                        intent.putExtra("memoText", textView_memo_text.getText().toString());
                        intent.putExtra("memoDate", textView_memo_date.getText().toString());
                        v.getContext().startActivity(intent);

                    }
                }
            });

            // 아이템을 길게 클릭하면
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("메모를 삭제하시겠습니까?.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // 확인 클릭했을 때 해당 메모 삭제한다.
                                    MemoAdapter.memoList.remove(position);

                                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                                   notifyDataSetChanged();
                                   dialog.dismiss();

                                }
                            });
                    builder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // 취소 클릭했을 때
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
    AllMemoAdapter(){}

    // 생성자에서 데이터 리스트 객체를 전달받음.
    AllMemoAdapter(ArrayList<Memo> allMemoList) {
        this.allMemoList = allMemoList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public AllMemoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_memo, parent, false);

        return new ViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(AllMemoAdapter.ViewHolder holder, int position) {
        Memo memo = allMemoList.get(position);

        // 메모에 저장되어 있는 bookId 로 해당하는 title, img 가져오기
        // holder.textView_title
        // holder.imageView_img
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
