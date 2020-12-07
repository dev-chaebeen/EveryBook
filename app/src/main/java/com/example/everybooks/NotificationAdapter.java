package com.example.everybooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.everybooks.data.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter
{
    Context context;
    static ArrayList<Notification> notiList = new ArrayList<>();

    public NotificationAdapter(){}

    public NotificationAdapter(Context context, ArrayList<Notification> notiList)
    {
        this.context = context;
        this.notiList = notiList;
    }

    // 데이터 추가 메소드
    public void addItem(Notification noti)
    {
        // 현재 알림 아이디가 0이라면 0을 저장하고 1을 증가시킨다.
        // 왜냐하면 리스트의 포지션과 아이디의 값을 맞추기 위해서 ???
        /*
        if(Notification.notiId == 0)
        {
            noti.setNotiId(0);
            Notification.notiId  = Notification.notiId  + 1;
        }
        else
        {
            noti.setNotiId(Notification.notiId);
        }

         */

/*        noti.setNotiId(Notification.notiId);
        Notification.notiId +=1;*/


        notiList.add(0, noti);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notiList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return notiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       final int index = position;
       final Context context = parent.getContext();

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_notification, parent, false);
        }

        // 뷰 요소 초기화
        TextView textView_notification_time = convertView.findViewById(R.id.notification_time);
        TextView textView_mon = convertView.findViewById(R.id.mon);
        TextView textView_tue = convertView.findViewById(R.id.tue);
        TextView textView_wed = convertView.findViewById(R.id.wed);
        TextView textView_thu = convertView.findViewById(R.id.thu);
        TextView textView_fri = convertView.findViewById(R.id.fri);
        TextView textView_sat = convertView.findViewById(R.id.sat);
        TextView textView_sun = convertView.findViewById(R.id.sun);
        TextView textView_notification_text = convertView.findViewById(R.id.notification_text);
        Switch switch_on = convertView.findViewById(R.id.notification_switch);

        // 알림 객체 얻기
        Notification noti = notiList.get(position);

        // 뷰 요소에 나타내주기
        textView_notification_time.setText(noti.getHour() + ":" + noti.getMinute());
        textView_mon.setText("월");
        textView_tue.setText("화");
        textView_wed.setText("수");
        textView_thu.setText("목");
        textView_fri.setText("금");
        textView_sat.setText("토");
        textView_sun.setText("일");
        textView_notification_text.setText(noti.getText());
        //switch_on.setChecked(noti.isOn);

        return convertView;
    }


}
