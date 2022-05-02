package cc.lexur.lexurtimemanager.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("接收到通知！");
        if (intent.getAction().equals("short")) {
            Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "repeating alarm", Toast.LENGTH_LONG).show();
        }
    }
}
