package cc.lexur.lexurtimemanager.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;

import cc.lexur.lexurtimemanager.R;

public class MyTimePicker {

    /**
     * 选择时间
     *
     * @param context
     */
    public static void alertDialog(Context context, View view) {
        Calendar selectedCalender;
        TextView tvDate, tvTime;
        Button btnSubmit, btnCancel;
        View targetView = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.show();

        View dialogView = LayoutInflater.from(context).inflate(R.layout.time_picker, null, false);
        tvDate = dialogView.findViewById(R.id.date);
        tvTime = dialogView.findViewById(R.id.time);
        btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        btnCancel = dialogView.findViewById(R.id.btnCancel);

        selectedCalender = Calendar.getInstance();

        tvDate.setOnClickListener(v -> {
            new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
                selectedCalender.set(i, i1, i2);
                int year = selectedCalender.get(Calendar.YEAR);
                int month = selectedCalender.get(Calendar.MONTH) + 1;
                int day = selectedCalender.get(Calendar.DAY_OF_MONTH);
                String selectTime = year + "/" + month + "/" + day;
                tvDate.setText(selectTime);
            }, selectedCalender.get(Calendar.YEAR), selectedCalender.get(Calendar.MONTH), selectedCalender.get(Calendar.DAY_OF_MONTH)).show();

        });

        tvTime.setOnClickListener(v -> {
            new TimePickerDialog(context, (timePicker, i, i1) -> {
                selectedCalender.set(Calendar.HOUR, i);
                selectedCalender.set(Calendar.MINUTE, i1);
                int hours = selectedCalender.get(Calendar.HOUR_OF_DAY);
                int minutes = selectedCalender.get(Calendar.MINUTE);
                String selectedTime = hours + ":" + minutes;
                tvTime.setText(selectedTime);
            }, selectedCalender.get(Calendar.HOUR), selectedCalender.get(Calendar.MINUTE), true).show();
        });

        btnSubmit.setOnClickListener(v -> {
            String date = DateFormat.getDate(selectedCalender);
            String time = DateFormat.getTime(selectedCalender);
            Log.d("test", "alertDialog: 选择时间" + date + " " + time);
            targetView.setTag(selectedCalender);
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());


        dialog.getWindow().setContentView(dialogView);

    }
}
