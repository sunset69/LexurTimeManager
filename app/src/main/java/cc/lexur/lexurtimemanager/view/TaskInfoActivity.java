package cc.lexur.lexurtimemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.ActivityTaskInfoBinding;
import cc.lexur.lexurtimemanager.room.Task;
import cc.lexur.lexurtimemanager.utils.TaskStatus;

/**
 * 任务详情界面
 */

public class TaskInfoActivity extends AppCompatActivity {

    ActivityTaskInfoBinding binding;
    TaskViewModel taskViewModel;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_info);
        taskViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(TaskViewModel.class);
        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        int current_id = extras.getInt("CURRENT_ID");
        String action = intent.getAction();
        Log.d("LexurTest", "onCreate: 接收到action："+action);
        if (action == null){
            Toast.makeText(getApplicationContext(), "action为空！action:"+action, Toast.LENGTH_SHORT).show();
            finish();
        }
        int current_id = Integer.valueOf(action) ;
        task = taskViewModel.getTaskById(current_id);
        if (task == null) {
            Toast.makeText(getApplicationContext(), "发生错误！", Toast.LENGTH_SHORT).show();
            finish();
        }
        binding.setTask(task);
        binding.setLifecycleOwner(this);
        String status = "error";
        switch (task.getStatus()) {
            case TaskStatus.DOING:
                status = getString(R.string.doing);
                break;
            case TaskStatus.DELAY:
                status = getString(R.string.delay);
                break;
            case TaskStatus.ABORT:
                status = getString(R.string.abort);
                break;
            case TaskStatus.FINISH:
                status = getString(R.string.finish);
                break;
        }
        binding.taskStatus.setText(status);
    }
}