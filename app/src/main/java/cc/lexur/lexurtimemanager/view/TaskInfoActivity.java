package cc.lexur.lexurtimemanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.ActivityTaskInfoBinding;
import cc.lexur.lexurtimemanager.room.Task;

public class TaskInfoActivity extends AppCompatActivity {

    ActivityTaskInfoBinding binding;
    TaskViewModel taskViewModel;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_info);
        taskViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(),this)).get(TaskViewModel.class);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int current_id = extras.getInt("CURRENT_ID");
        task = taskViewModel.getTaskById(current_id);
        Log.d("test", "onCreate: 当前task："+task.toString());
        binding.setLifecycleOwner(this);
    }
}