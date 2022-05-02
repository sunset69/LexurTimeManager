package cc.lexur.lexurtimemanager.view;

import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.FragmentSummaryBinding;
import cc.lexur.lexurtimemanager.room.Label;
import cc.lexur.lexurtimemanager.room.Task;
import cc.lexur.lexurtimemanager.utils.ChartUtils;
import cc.lexur.lexurtimemanager.utils.TaskStatus;
import cc.lexur.lexurtimemanager.utils.TimeTaskUtil;


/**
 * 总结页面，提供任务相关总结
 */

public class SummaryFragment extends Fragment {

    private static final String TAG = "test";

    FragmentSummaryBinding binding;
    TaskViewModel taskViewModel;
    private PieChart pieChart;
    private BarChart barChart;
    private LineChart lineChart;
    private LiveData<List<Task>> allTasksLive;
    private LiveData<List<Label>> allLabelsLive;
    private List<Task> allTasks;
    private List<Label> allLabels;

    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false);
        taskViewModel = new ViewModelProvider(requireActivity(), new SavedStateViewModelFactory(getActivity().getApplication(), requireActivity())).get(TaskViewModel.class);
        allTasksLive = taskViewModel.getAllTasksLive();
        allLabelsLive = taskViewModel.getAllLabelsLive();
        pieChart = binding.pieChartTaskLabel;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allTasksLive.observe(this, tasks -> {
            allTasks = tasks;
        });
        allLabelsLive.observe(this, labels -> {
            allLabels = labels;
        });

        binding.button.setOnClickListener(v->{
            TimeTaskUtil.timeTask(v.getContext(),null);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 恢复了");

        Log.d(TAG, "onResume: all tasks=====================");
        for (Task task : allTasks) {
            Log.d(TAG, "onResume: " + task);
        }
        Log.d(TAG, "onResume: all labels=====================");
        for (Label label : allLabels) {
            Log.d(TAG, "onResume: " + label);
        }
        Log.d(TAG, "onResume: ===================================");


        ChartUtils.pieChart(binding.pieChartTaskType, getString(R.string.task_status), getTaskStatusSummaryPiedata());
        ChartUtils.pieChart(binding.pieChartTaskLabel, getString(R.string.task_label), getTaskLabelSummaryPiedata());

        /**
         * 任务标签饼状图
         * 统计每种类型任务数目
         */


    }

    /**
     * 获取任务状态统计数据
     *
     * @return 统计数据
     */
    public HashMap<Integer, Integer> getTaskStatusSummary() {
        HashMap<Integer, Integer> data = new HashMap<>();
        int doing = 0;
        int delay = 0;
        int abort = 0;
        int finish = 0;
        for (Task task : allTasks) {
            switch (task.getStatus()) {
                case TaskStatus.DOING:
                    doing++;
                    break;
                case TaskStatus.DELAY:
                    delay++;
                    break;
                case TaskStatus.ABORT:
                    abort++;
                    break;
                case TaskStatus.FINISH:
                    finish++;
                    break;
            }
        }
        data.put(TaskStatus.DOING, doing);
        data.put(TaskStatus.DELAY, delay);
        data.put(TaskStatus.ABORT, abort);
        data.put(TaskStatus.FINISH, finish);

        Log.d(TAG, "getTaskStatus: task status:" + data);

        return data;
    }

    /**
     * 任务状态统计饼状图
     * 统计各种类型任务数w目
     * 4中状态
     *
     * @return 返回饼状图数据
     */
    public PieData getTaskStatusSummaryPiedata() {
        HashMap<Integer, Integer> taskStatusSummary = getTaskStatusSummary();
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(taskStatusSummary.get(TaskStatus.DOING), getContext().getString(R.string.doing)));
        entries.add(new PieEntry(taskStatusSummary.get(TaskStatus.DELAY), getString(R.string.delay)));
        entries.add(new PieEntry(taskStatusSummary.get(TaskStatus.ABORT), getString(R.string.abort)));
        entries.add(new PieEntry(taskStatusSummary.get(TaskStatus.FINISH), getString(R.string.finish)));

        List<Integer> colors = new ArrayList<>();
        colors.add(getContext().getColor(R.color.doing));
        colors.add(getContext().getColor(R.color.delay));
        colors.add(getContext().getColor(R.color.abort));
        colors.add(getContext().getColor(R.color.finish));

        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.legend));
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
//        pieData.setDataSet(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);

        return pieData;
    }

    /**
     * 获取分类相关统计数据
     *
     * @return 统计数据
     */
    public HashMap<Label, Integer> getTaskLabelSummary() {

        HashMap<Integer, Integer> data = new HashMap<>();
        HashMap<Label, Integer> result = new HashMap<>();

        for (Task task : allTasks) {
            int labelId = task.getLabelId();
            if (data.get(labelId) == null) {
                data.put(labelId, 1);
            }
            data.put(labelId, data.get(labelId) + 1);
        }

        Log.d(TAG, "getTaskLabelSummary: task labels:" + data);

        List<Label> labels = taskViewModel.getAllLabelsLive().getValue();
        for (Label label : labels) {
            result.put(label, data.get(label.getId()));
        }

        Log.d(TAG, "getTaskLabelSummary: result:" + result);

        return result;

    }

    public PieData getTaskLabelSummaryPiedata() {
        HashMap<Label, Integer> taskLabelSummary = getTaskLabelSummary();
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        for (Label label : taskLabelSummary.keySet()) {
            Integer size = taskLabelSummary.get(label);
            if (size == null) {
                size = 0;
            }
            entries.add(new PieEntry(size, label.getName()));
            colors.add(label.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.legend));
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        return pieData;
    }


}