package cc.lexur.lexurtimemanager.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private List<Chip> labelChips;

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
        binding.selectDate.setOnClickListener(view -> new DatePickerDialog(view.getContext(), (datePicker, i, i1, i2) -> {
            selectedCalendar.set(i, i1, i2);
            binding.selectDate.setText(DateFormat.getDate(selectedCalendar));
            Log.d("test", "onDateSet: date:" + selectedCalendar.getTime());
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH))
                .show());

        // 时间选择
        binding.selectTime.setOnClickListener(view -> new TimePickerDialog(view.getContext(), (timePicker, i, i1) -> {
            timePicker.setIs24HourView(true);
            timePicker.setLayoutMode(1);
            selectedCalendar.set(Calendar.HOUR, i);
            selectedCalendar.set(Calendar.MINUTE, i1);
            binding.selectTime.setText(DateFormat.getTime(selectedCalendar));
            Log.d("test", "onDateSet: time:" + selectedCalendar.getTime());
        }, currentCalendar.get(Calendar.HOUR), currentCalendar.get(Calendar.MINUTE), true)
                .show());



        /**
         * 分类选择
         */

        // 显示方式为流式布局
        binding.cgLabel.setLayoutMode(StaggeredGridLayoutManager.HORIZONTAL);

        // 获取分类
        labelChips = new ArrayList<>();
        Chip chipStudy = new Chip(this);
        chipStudy.setText("学习");
        Chip chipWork = new Chip(this);
        chipWork.setText("工作");
        Chip chipLife = new Chip(this);
        chipLife.setText("生活");
        Chip chipOther = new Chip(this);
        chipOther.setText("其他");
        chipOther.setCheckable(true);
        chipOther.setChecked(true);
        labelChips.add(chipOther);
        labelChips.add(chipStudy);
        labelChips.add(chipWork);
        labelChips.add(chipLife);

        for (Chip chip : labelChips) {
            chip.setCheckable(true);//设置为可选择
            binding.cgLabel.addView(chip);
        }
        binding.cgLabel.setSingleSelection(true);
        // 设置点击事件
        binding.cgLabel.setOnCheckedChangeListener((group, checkedId) -> {
            // 使用findViewById来获取控件
            Chip chipTest = group.findViewById(checkedId);
            if (chipTest != null){
                Log.d("test", "init: 选中了："+chipTest.getText());
            }
        });

        /**
         * 提交与取消按钮
         */

        // 添加按钮
        binding.btnAdd.setOnClickListener(view -> {
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

            int selectId = binding.cgLabel.getCheckedChipId();
            // 默认未选择为-1
            if (selectId == -1){
                task.setLabelId(0);
            }else {
                Chip selectedChip = (Chip) binding.cgLabel.getChildAt(binding.cgLabel.getCheckedChipId());
                task.setLabelId(selectId);
            }



            Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
            Log.d("test", "onClick: " + task.toString());
            finish();
        });

        // 取消按钮
        binding.btnCancel.setOnClickListener(view -> finish());

    }
}