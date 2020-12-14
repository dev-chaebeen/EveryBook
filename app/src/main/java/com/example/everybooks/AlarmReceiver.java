package com.example.everybooks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_RESTART_SERVICE = "Restart";


    @Override
    public void onReceive(Context context, Intent intent)
    {

        Log.d("테스트","AlarmReceiver 알람 리시버 ");
        if(intent.getAction().equals(ACTION_RESTART_SERVICE)) {
            Intent in = new Intent(context, AlarmService.class);
            context.startService(in);
        }

    }
}
