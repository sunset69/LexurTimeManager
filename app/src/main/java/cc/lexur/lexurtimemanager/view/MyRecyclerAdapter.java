package cc.lexur.lexurtimemanager.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.CellCardBinding;
import cc.lexur.lexurtimemanager.room.Task;

/**
 * RecyclerView适配器
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

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
