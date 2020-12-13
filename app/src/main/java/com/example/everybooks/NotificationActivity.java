package com.example.everybooks;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everybooks.data.Notification;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity
{
    // 뷰 요소 선언
    LinearLayout linearLayout_notification;
    TextView textView_add;
    ListView listView;
    NotificationAdapter adapter;

    // 인텐트
    Intent intent;
    final String TAG = "테스트";
    Notification noti;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        //test
        setAlarm();


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

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //리스트뷰에 어댑터를 붙여서 사용자에게 알림이 보이도록 한다.
        listView.setAdapter(adapter);

        // 알림을 클릭하면 알림 데이터를 가지고 알림 편집 화면으로 이동한다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                intent = new Intent(getApplicationContext(), EditNotificationActivity.class);

                noti = (Notification) adapter.getItem(position);
                intent.putExtra("notiId", noti.getNotiId());
                startActivity(intent);

                Log.d(TAG, "NotificationActivity, 인텐트로 보내는 notiId : " + noti.getNotiId());

            }
        });

        // 각 알림을 길게 클릭하면 삭제하겠냐고 확인하는 문구가 뜬다.
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
                            noti = (Notification) adapter.getItem(position);

                            // 알림을 삭제하기 위해서 저장되어있는 알림리스트를 불러온다.
                            SharedPreferences notiInfo = getSharedPreferences("notiInfo", MODE_PRIVATE);
                            String notiListString = notiInfo.getString("notiList", null);
                            ArrayList<Notification> notiList = new ArrayList<>();

                            if (notiListString != null)
                            {
                                try
                                {
                                    JSONArray jsonArray = new JSONArray(notiListString);

                                    // JsonArray 형태로는 객체를 삭제할 수 없기 때문에
                                    // jsonArray 의 길이만큼 반복해서 jsonObject 를 가져오고,
                                    // 삭제할 memoId 와 일치하지 않는 jsonObject 만 Memo 객체에 담은 뒤 ArrayList<Memo> 에 담는다.
                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        if(jsonObject.getInt("notiId") != noti.getNotiId())
                                        {
                                            Notification noti = new Notification();
                                            noti.setNotiId(jsonObject.getInt("notiId"));
                                            noti.setText(jsonObject.getString("text"));
                                            noti.setHour(jsonObject.getInt("hour"));
                                            noti.setMinute(jsonObject.getInt("minute"));
                                            // days
                                            notiList.add(noti);
                                        }
                                    }

                                    Log.d(TAG, "저장되어있는 notiList.size : " + notiList.size());

                                }
                                catch (Exception e)
                                {
                                    System.out.println(e.toString());
                                }


                                /// JSONArray 로 변환해서 다시 저장하기
                                JSONArray jsonArray = new JSONArray();

                                for (int i = 0; i < notiList.size(); i++)
                                {
                                    Notification noti = notiList.get(i);

                                    // json 객체에 입력받은 값을 저장한다.
                                    try
                                    {
                                        JSONObject jsonObject = new JSONObject();

                                        jsonObject.put("notiId", noti.getNotiId());
                                        jsonObject.put("text", noti.getText());
                                        jsonObject.put("hour", noti.getHour());
                                        jsonObject.put("minute", noti.getMinute());
                                        // days
                                        jsonArray.put(jsonObject);
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(e.toString());
                                    }
                                }

                                notiListString = jsonArray.toString();

                                notiInfo = getSharedPreferences("notiInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = notiInfo.edit();
                                editor.putString("notiList", notiListString);
                                editor.commit();

                                dialog.dismiss();

                            }

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

        // 저장되어있는 알림리스트 어댑터에 보내주기
        try
        {
            SharedPreferences notiInfo = getSharedPreferences("notiInfo", MODE_PRIVATE);
            String notiListString = notiInfo.getString("notiList", null);
            ArrayList<Notification> notiList = new ArrayList<>();

            if(notiListString != null)
            {
                JSONArray jsonArray = new JSONArray(notiListString);

                // 가져온 jsonArray의 길이만큼 반복해서 jsonObject 를 가져오고, Book 객체에 담은 뒤 ArrayList<Book> 에 담는다.
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Notification noti = new Notification();
                    noti.setNotiId(jsonObject.getInt("notiId"));
                    noti.setHour(jsonObject.getInt("hour"));
                    noti.setMinute(jsonObject.getInt("minute"));
                    noti.setText(jsonObject.getString("text"));

                    notiList.add(0, noti);

                }

                NotificationAdapter notificationAdapter = new NotificationAdapter(getApplicationContext(), notiList);
                notificationAdapter.notifyDataSetChanged();

            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

    }




    void setAlarm() {
        
        Log.d(TAG, "알람 설정");

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY, 23);
        alarmCalendar.set(Calendar.MINUTE, 43);
        alarmCalendar.set(Calendar.SECOND, 0);
        /*
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        alarmCalendar.set(Calendar.MINUTE, alarmMinute);
        alarmCalendar.set(Calendar.SECOND, 0);*/
        // TimePickerDialog 에서 설정한 시간을 알람 시간으로 설정

        if (alarmCalendar.before(Calendar.getInstance())) alarmCalendar.add(Calendar.DATE, 1);
        // 알람 시간이 현재시간보다 빠를 때 하루 뒤로 맞춤
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent.setAction(AlarmReceiver.ACTION_RESTART_SERVICE);
        PendingIntent alarmCallPendingIntent
                = PendingIntent.getBroadcast
                (getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle
                    (AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact
                    (AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
    } // 알람 설정



}
