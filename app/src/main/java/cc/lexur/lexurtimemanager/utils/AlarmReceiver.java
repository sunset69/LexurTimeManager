package cc.lexur.lexurtimemanager.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("接收到通知！");
        String action = intent.getAction();
        NotificationUtil.notification(context,"任务到期","任务即将结束",action);

    }
}
