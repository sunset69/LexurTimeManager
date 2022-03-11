package cc.lexur.lexurtimemanager.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.ActivityAddTaskBinding;
import cc.lexur.lexurtimemanager.room.Task;

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
                Log.d("test", "onClick: "+task.toString());
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