package cc.lexur.lexurtimemanager.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.ActivityAddTaskBinding;
import cc.lexur.lexurtimemanager.room.Task;
import cc.lexur.lexurtimemanager.utils.DateFormat;

public class AddTaskActivity extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    TaskViewModel taskViewModel;
    Calendar selectedCalendar;
    private Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_task);
        taskViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(TaskViewModel.class);
        binding.setLifecycleOwner(this);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        selectedCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();

        // 日期选择
        binding.selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selectedCalendar.set(i, i1, i2);
                        binding.selectDate.setText(DateFormat.getDate(selectedCalendar));
                        Log.d("test", "onDateSet: date:" + selectedCalendar.getTime());
                    }
                }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        // 时间选择
        binding.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timePicker.setIs24HourView(true);
                        timePicker.setLayoutMode(1);
                        selectedCalendar.set(Calendar.HOUR, i);
                        selectedCalendar.set(Calendar.MINUTE, i1);
                        binding.selectTime.setText(DateFormat.getTime(selectedCalendar));
                        Log.d("test", "onDateSet: time:" + selectedCalendar.getTime());
                    }
                }, currentCalendar.get(Calendar.HOUR), currentCalendar.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        // 添加按钮
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.etTitle.getText().toString();
                String description = binding.etDescription.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task = new Task();
                task.setName(title);
                task.setDescription(description);
//                task.setCreateTime(new Date());
                taskViewModel.insertTasks(task);
                Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                Log.d("test", "onClick: " + task.toString());
                finish();
            }
        });

        // 取消按钮
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}