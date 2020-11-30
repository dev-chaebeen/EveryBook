package com.example.everybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    LinearLayout linearLayout_notification;
    TextView textView_add;
    ListView listView;
    NotificationAdapter adapter;

    // 인텐트
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // 요소 초기화
        linearLayout_notification = findViewById(R.id.notification);
        textView_add = findViewById(R.id.add);
        listView = findViewById(R.id.notification_list);
        adapter = new NotificationAdapter();

        // 각 요소를 클릭하면 수행할 동작 지정해두기
        View.OnClickListener click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.add :
                        // Add 클릭했을 때 알림 추가하는 화면으로 전환 
                        intent = new Intent(getApplicationContext(), AddNotificationActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        // 각 요소가 클릭되면 동작 수행
        textView_add.setOnClickListener(click);

        // 임시로 데이터를 넣어준다.
        Notification noti = new Notification();
        noti.setHour(14);
        noti.setMinute(30);
        noti.setText("알림 문구 설정");
        NotificationAdapter.notiList.add(noti);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //리스트뷰에 어댑터를 붙여서 사용자에게 메모가 보이도록 한다.
        listView.setAdapter(adapter);

        // 알림을 클릭하면 알림 데이터를 가지고 알림 편집 화면으로 이동한다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                intent = new Intent(getApplicationContext(), EditNotificationActivity.class);

                Notification noti = (Notification) adapter.getItem(position);
                intent.putExtra("position", position);
                intent.putExtra("hour", noti.getHour());
                intent.putExtra("minute", noti.getMinute());
                intent.putExtra("text", noti.getText());
                startActivity(intent);
            }
        });

        // 각 메모를 길게 클릭하면 삭제하겠냐고 확인하는 문구가 뜬다.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                builder.setMessage("알림을 삭제하시겠습니까?.");
                builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // 확인 클릭했을 때 해당 알림을 삭제하고 삭제 결과를 리스트뷰에 반영하기 위해 어댑터를 새로고침한다.
                            NotificationAdapter.notiList.remove(position);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });

                builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // 취소 클릭했을 때
                            Toast.makeText( getApplicationContext(), "취소" ,Toast.LENGTH_SHORT).show();
                        }
                    });

                builder.show();
                return true; // 롱클릭 이벤트 이후 클릭이벤트 발생하지 않도록 true 반환
            }
        });
    }
}
