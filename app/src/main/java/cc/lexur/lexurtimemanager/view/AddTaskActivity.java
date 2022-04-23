package cc.lexur.lexurtimemanager.view;

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

import java.util.Calendar;
import java.util.Date;

import cc.lexur.lexurtimemanager.MainActivity;
import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.ActivityAddTaskBinding;
import cc.lexur.lexurtimemanager.room.Label;
import cc.lexur.lexurtimemanager.room.Task;
import cc.lexur.lexurtimemanager.utils.ChipUtils;
import cc.lexur.lexurtimemanager.utils.MyTimePicker;

/**
 * 添加任务界面
 */

public class AddTaskActivity extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    TaskViewModel taskViewModel;

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

        // 选择开始时间
        binding.selectStartTime.setOnClickListener(v -> {
            MyTimePicker.alertDialog(v.getContext(), v);
        });
        // 选择结束时间
        binding.selectStopTime.setOnClickListener(v -> {
            MyTimePicker.alertDialog(v.getContext(), v);
        });


        /**
         * 分类选择
         */

        // 显示方式为流式布局
        binding.cgLabel.setLayoutMode(StaggeredGridLayoutManager.HORIZONTAL);
        binding.cgLabel.setSingleSelection(true);

        // 获取分类
        taskViewModel.getAllLabelsLive().observe(this, labels -> {
            binding.cgLabel.removeAllViews();
            //无分类自动创建
            if (labels.size() == 0) {
                Label label = new Label();
                label.setId(0);
                label.setName("其他");
                label.setColor(Color.GRAY);
                taskViewModel.insertLabels(label);
                return;
            }
            for (int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                Chip chip = new Chip(this);
                chip.setCheckable(true);
                chip.setText(label.getName());
                chip.setChipBackgroundColor(ChipUtils.setChipColor(label.getColor()));
                binding.cgLabel.addView(chip);
            }
            binding.cgLabel.setSingleSelection(true);
            binding.cgLabel.setSelectionRequired(true);
        });


        /**
         * 提交与取消按钮
         */

        // 添加按钮
        binding.btnAdd.setOnClickListener(view -> {

            /**
             * 获取数据
             */

            String title = binding.etTitle.getText().toString();
            String description = binding.etDescription.getText().toString();
            Calendar calendarStart;
            Calendar calendarStop;
            calendarStart = (Calendar) binding.selectStopTime.getTag();
            calendarStop = (Calendar) binding.selectStopTime.getTag();

            int selectId = binding.cgLabel.getCheckedChipId();
            Chip selectedChip = null;
            for (int i = 0; i < binding.cgLabel.getChildCount(); i++) {
                Chip child = (Chip) binding.cgLabel.getChildAt(i);
                if (child.getId() == selectId) {
                    selectedChip = (Chip) binding.cgLabel.getChildAt(i);
                }
            }


            /**
             * 检测是否填写
             */
            if (title.isEmpty()) {
                Toast.makeText(getApplicationContext(), "请输入标题", Toast.LENGTH_SHORT).show();
                return;
            }
            if (calendarStart == null) {
                Toast.makeText(getApplicationContext(), "请选择开始时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (calendarStop == null) {
                Toast.makeText(getApplicationContext(), "请选择结束时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedChip == null || selectedChip.getId() == -1) {
                Toast.makeText(getApplicationContext(), "请选择分类！", Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * 封装数据并提交
             */

            Task task = new Task();
            task.setName(title);
            task.setDescription(description);
            task.setCreateTime(new Date());
            task.setStartTime(calendarStart.getTime());
            task.setStartTime(calendarStop.getTime());
            task.setLabelId(selectedChip.getId());

            taskViewModel.insertTasks(task);

            Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
            Log.d("test", "onClick: 添加了" + task.toString());
            finish(); //退出
        });

        // 取消按钮
        binding.btnCancel.setOnClickListener(view -> finish());

    }
}