package cc.lexur.lexurtimemanager.room;

import android.graphics.Color;
import android.os.Build;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? new Date() : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? new Date().getTime() : date.getTime();
    }

//    @TypeConverter
//    public static Color fromArgb(Long value) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return value == null ? Color.valueOf(Color.GRAY) : Color.valueOf(value);
//        }
//        return null;
//    }
//
//    @TypeConverter
//    public static Long colorToArgb(Color color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int defaultColor = Color.valueOf(163, 163, 163).toArgb();
//            return Long.valueOf(color == null ? defaultColor : color.toArgb());
//        }
//        return null;
//    }
}
