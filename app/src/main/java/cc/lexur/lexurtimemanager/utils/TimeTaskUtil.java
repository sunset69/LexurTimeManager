package cc.lexur.lexurtimemanager.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class TimeTaskUtil {
    public static void timeTask(Context context, Calendar calendar1,int taskId){
        //操作：发送一个广播，广播接收后Toast提示定时操作完成
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(String.valueOf(taskId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //设定一个五秒后的时间
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);

        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //或者以下面方式简化
        //alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*1000, sender);
    }

}
