package cc.lexur.lexurtimemanager.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {
    public static String getAllTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s = sdf.format(calendar.getTime());
        return s;
    }

    public static String getAllTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s = sdf.format(date);
        return s;
    }

    public static String getDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(calendar.getTime());
        return s;
    }

    public static String getTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String s = sdf.format(calendar.getTime());
        return s;
    }

}
