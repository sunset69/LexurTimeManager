package cc.lexur.lexurtimemanager.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormat {
    public static String getAllTime(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s = sdf.format(calendar.getTime());
        return s;
    }
    public static String getDate(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(calendar.getTime());
        return s;
    }

    public static String getTime(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String s = sdf.format(calendar.getTime());
        return s;
    }

}
