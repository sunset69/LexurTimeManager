package cc.lexur.lexurtimemanager.view;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.FragmentTaskBinding;
import cc.lexur.lexurtimemanager.room.Task;


/**
 * 任务界面，负责查看任务，添加任务，完成任务等功能
 */
public class TaskFragment extends Fragment {

    FragmentTaskBinding binding;
    TaskViewModel taskViewModel;
    private MyRecyclerAdapter recyclerAdapter;

    public TaskFragment() {
    }


    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false);
        taskViewModel = new ViewModelProvider(requireActivity(), new SavedStateViewModelFactory(getActivity().getApplication(), requireActivity())).get(TaskViewModel.class);
        binding.setData(taskViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerAdapter = new MyRecyclerAdapter(taskViewModel);

        binding.recyclerView.setAdapter(recyclerAdapter);

        //为实现LiveData的数据设置观察者，以便当数据改变时通知UI更新数据
        taskViewModel.getAllTasksLive().observe(requireActivity(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                recyclerAdapter.setAllWords(tasks);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        // 添加
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task1 = new Task("学习", "学习编程");
                Task task2 = new Task("购物", "购买东西，AAAAAAVVVVBBBASFASFSAFASF");
                taskViewModel.insertTasks(task1, task2);
            }
        });

        // 修改
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task("修改", "修改过后");
                task.setId(2);
                taskViewModel.updateTasks(task);
            }
        });

        // 清空
        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskViewModel.clearTasks();
            }
        });
    }


}