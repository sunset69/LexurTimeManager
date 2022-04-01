package cc.lexur.lexurtimemanager.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;
import java.util.Date;

import cc.lexur.lexurtimemanager.R;

public class TimePickerOld {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showDatePickerDialog(Context context) {

        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();

        View dialogView = LayoutInflater.from(context).inflate(R.layout.time_picker_old, null, false);
        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        View selectTime = dialogView.findViewById(R.id.ivSelectTime);

        // 选择时间
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR, i);
                        calendar.set(Calendar.MINUTE, i1);
                    }
                }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        // 确认按钮
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, day);
                Date time = calendar.getTime();
                Log.d("test", "onClick: 时间：" + time.toString());
            }
        });



        builder.setTitle("选择时间");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Calendar calendar = Calendar.getInstance();

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        dialog.setView(dialogView);
        dialog.show();
    }

}
