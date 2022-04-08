package cc.lexur.lexurtimemanager.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.CellCardBinding;
import cc.lexur.lexurtimemanager.databinding.FragmentTaskBinding;
import cc.lexur.lexurtimemanager.room.Task;
import cc.lexur.lexurtimemanager.utils.TaskStatus;


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
     * 工具栏菜单
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
        itemAdd.setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(getContext(), AddTaskActivity.class));
            return true;
        });
        itemClear.setOnMenuItemClickListener(menuItem -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("警告")
                    .setMessage("确认清空数据！")
                    .setNegativeButton("确定", (dialogInterface, i) -> taskViewModel.clearTasks())
                    .setPositiveButton("取消", null)
                    .show();
            return true;
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
        taskViewModel.getAllTasksLive().observe(requireActivity(), tasks -> {
            for (Task task : tasks) {
                if (task.getStatus() == TaskStatus.DOING && task.getStopTime().before(Calendar.getInstance().getTime())) {
                    task.setStatus(TaskStatus.DELAY);
                    taskViewModel.updateTasks(task);
                }
            }
            recyclerAdapter.setAllWords(tasks);
            recyclerAdapter.notifyDataSetChanged();
        });
    }


    /**
     * RecyclerView适配器
     */

    public static class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        private List<Task> allTasks = new ArrayList<>();
        TaskViewModel taskViewModel;
        CellCardBinding binding;
        private Context context;

        /**
         * 构造函数，传入ViewModel
         *
         * @param taskViewModel
         */
        public MyRecyclerAdapter(TaskViewModel taskViewModel) {
            this.taskViewModel = taskViewModel;
        }


        /**
         * 返回所有的数据
         *
         * @param allTasks
         */
        public void setAllWords(List<Task> allTasks) {
            this.allTasks = allTasks;
        }

        /**
         * 当适配器创建的时候调用
         *
         * @param parent
         * @param viewType
         * @return
         */
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //RecyclerView中调用DataBinding，绑定布局，获取binding实例
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cell_card, parent, false);
            //把binding作为参数，返回一个自定义的MyViewHolder
            context = binding.getRoot().getContext();
            return new MyViewHolder(binding);
        }

        /**
         * 当调用ViewHolder时响应,渲染页面
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            //获取当前位置的一行数据
            Task task = allTasks.get(position);


            String status = "";
            int color = Color.GRAY;

            switch (task.getStatus()) {
                case TaskStatus.DOING:
                    status = "doing";
                    color = ContextCompat.getColor(context, R.color.doing);
                    break;
                case TaskStatus.DELAY:
                    status = "delay";
                    color = ContextCompat.getColor(context, R.color.delay);
                    break;
                case TaskStatus.ABORT:
                    status = "abort";
                    color = ContextCompat.getColor(context, R.color.abort);
                    break;
                case TaskStatus.FINISH:
                    status = "finish";
                    color = ContextCompat.getColor(context, R.color.finish);
                    break;
            }

            holder.cardView.setTag(R.string.ITEM_TASK_TAG, task);
            //设置数据
            holder.tvTaskName.setText(task.getName());
            holder.tvTaskDescription.setText(task.getDescription());
            holder.tvStatus.setText(status);
            holder.cardView.setBackgroundColor(color);

            // 点击事件,跳转至任务详情页面
            holder.cardView.setOnClickListener(view -> {
                Task clickedTask = (Task) view.getTag(R.string.ITEM_TASK_TAG);
                Intent intent = new Intent(view.getContext(), TaskInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CURRENT_ID", clickedTask.getId());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            });

            // 长按事件
            holder.cardView.setOnLongClickListener(v -> {
                Log.d("test", "onBindViewHolder: 长按事件");
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.cell_card_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    Task selectedTask = (Task) holder.cardView.getTag(R.string.ITEM_TASK_TAG);
                    switch (menuItem.getItemId()) {

                        case R.id.menuItemDelete:
                            Log.d("test", "onBindViewHolder: 删除：" + selectedTask);
                            taskViewModel.deleteTasks(selectedTask);
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.delete) + " " + selectedTask.getName(), Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.menuItemArchive:
                            selectedTask.setStatus(TaskStatus.FINISH);
                            taskViewModel.updateTasks(selectedTask);
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.archive) + " " + selectedTask.getName(), Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.menuItemDoing:
                            selectedTask.setStatus(TaskStatus.DOING);
                            taskViewModel.updateTasks(selectedTask);
                            break;

                        case R.id.menuItemAbort:
                            selectedTask.setStatus(TaskStatus.ABORT);
                            taskViewModel.updateTasks(selectedTask);
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.abort) + " " + selectedTask.getName(), Toast.LENGTH_SHORT).show();
                            break;

                    }
                    return false;
                });
                popupMenu.show();
                return false;
            });

            holder.checkBox.setOnClickListener(view -> {
                Task clickedTask = (Task) holder.cardView.getTag(R.string.ITEM_TASK_TAG);
                if (holder.checkBox.isChecked()) {
                    clickedTask.setStatus(TaskStatus.FINISH);
                } else {
                    clickedTask.setStatus(TaskStatus.DOING);
                }
                taskViewModel.updateTasks(clickedTask);
            });

        }

        /**
         * 返回数据item个数
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return allTasks.size();
        }

        /**
         * 把onCreateViewHolder方法中的binding存起来，可以下次再用。这样做的好处就是不必每次都到布局文件中去拿到你的View，提高了效率。
         */
        protected class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvTaskName, tvTaskDescription, tvStatus;
            CardView cardView;
            CheckBox checkBox;

            public MyViewHolder(CellCardBinding binding) {
                super(binding.getRoot());
                tvTaskName = binding.tvTaskName;
                tvTaskDescription = binding.tvTaskDescription;
                tvStatus = binding.tvStatus;
                cardView = binding.cardView;
                checkBox = binding.checkBox;
            }
        }
    }
}