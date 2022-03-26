package cc.lexur.lexurtimemanager.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.CellCardBinding;
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


    /**
     * RecyclerView适配器
     */

    public static class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        private List<Task> allTasks = new ArrayList<>();
        TaskViewModel taskViewModel;
        CellCardBinding binding;

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
            return new MyViewHolder(binding);
        }

        /**
         * 当调用ViewHolder时响应
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            //获取当前位置的一行数据
            Task task = allTasks.get(position);
            holder.cardView.setTag(R.string.ITEM_TASK_TAG, task);
            //设置数据
            holder.tvTaskName.setText(task.getName());
            holder.tvTaskDescription.setText(task.getDescription());

            // 点击事件
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "clicked position:" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    Task task = (Task) view.getTag(R.string.ITEM_TASK_TAG);
                    Log.d("test", "onClick: "+task.toString());
                }
            });

            // 长按事件
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.cell_card_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Task task = (Task) holder.itemView.getTag(R.string.ITEM_TASK_TAG);
                            switch (menuItem.getItemId()) {
                                case R.id.menuItemDelete:
                                    taskViewModel.deleteTasks(task);
                                    Toast.makeText(view.getContext(), "删除：" + task.getName(), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.menuItemArchive:
                                    Toast.makeText(view.getContext(), "归档：" + ((Task) holder.itemView.getTag(R.string.ITEM_TASK_TAG)).getId(), Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    return false;
                }
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

            private CellCardBinding binding;
            TextView tvTaskName, tvTaskDescription;
            CardView cardView;

            public MyViewHolder(CellCardBinding binding) {
                super(binding.getRoot());
                tvTaskName = binding.tvTaskName;
                tvTaskDescription = binding.tvTaskDescription;
                cardView = binding.cardView;
            }
        }
    }
}