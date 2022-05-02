package cc.lexur.lexurtimemanager.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.view.TaskInfoActivity;

public class NotificationUtil {
    public static void notification(Context context, String title, String content,String action) {
        NotificationManager notificationManager;
        String channelId = "lexur";
        int nmId = 1;
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            channel = new NotificationChannel(channelId, "通知测试", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // 设置点击后跳转至页面
        Intent intent = new Intent(context, TaskInfoActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("CURRENT_ID", 1);
//        intent.putExtras(bundle);
        intent.setAction(String.valueOf(action));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title) //标题
                .setContentText(content) //内容
                .setSmallIcon(R.drawable.ic_launcher_foreground) //设置小图标，必须！
                .setContentIntent(pendingIntent) //点击操作
                .setAutoCancel(true) //点击通知后自动取消
                .build();

        notificationManager.notify(nmId, notification);
    }
}
