package cc.lexur.lexurtimemanager.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    // 空构造函数
    public TaskFragment() {
        setHasOptionsMenu(true);// 开启ToolBar菜单
    }

    /**
     * 渲染菜单
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_menu, menu);
        MenuItem itemAdd = menu.findItem(R.id.menuItemAdd);
        MenuItem itemClear = menu.findItem(R.id.menuItemClear);
        itemAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getContext(), AddTaskActivity.class));
                return true;
            }
        });
        itemClear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new AlertDialog.Builder(getContext())
                        .setTitle("警告")
                        .setMessage("确认清空数据！")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                taskViewModel.clearTasks();
                            }
                        })
                        .setPositiveButton("取消", null)
                        .show();
                return true;
            }
        });
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
    }


}