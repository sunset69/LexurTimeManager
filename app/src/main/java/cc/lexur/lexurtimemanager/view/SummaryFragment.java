package cc.lexur.lexurtimemanager.view;

import android.graphics.Color;
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
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.lexur.lexurtimemanager.R;
import cc.lexur.lexurtimemanager.TaskViewModel;
import cc.lexur.lexurtimemanager.databinding.FragmentSummaryBinding;
import cc.lexur.lexurtimemanager.room.Label;
import cc.lexur.lexurtimemanager.room.Task;
import cc.lexur.lexurtimemanager.utils.TaskStatus;


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
        pieChart = binding.pieChart;
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

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 恢复了");

        Log.d(TAG, "onResume: all tasks=====================");
        for (Task task : allTasks) {
            Log.d(TAG, "onResume: "+task);
        }
        Log.d(TAG, "onResume: all labels=====================");
        for (Label label : allLabels) {
            Log.d(TAG, "onResume: "+ label);
        }
        Log.d(TAG, "onResume: ===================================");

        getTaskStatusSummary();
        getTaskLabelSummary();



    }

    /**
     * 获取任务状态统计数据
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
     * 获取分类相关统计数据
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
            result.put(label,data.get(label.getId()));
        }

        Log.d(TAG, "getTaskLabelSummary: result:" + result);

        return result;

    }

    private void addPieChart() {
        pieChart.setUsePercentValues(true);// 使用百分比
        pieChart.getDescription().setEnabled(false); // 开启描述
        pieChart.setExtraOffsets(5, 10, 5, 5); //设置额外偏移
        pieChart.setDragDecelerationFrictionCoef(0.95f); //设置动画阻尼
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawHoleEnabled(true); //设置允许拖拽
        pieChart.setHoleColor(Color.WHITE); //设置中心颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
    }


    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString(getString(R.string.task_type) + "");
        return s;
    }

    private void pieChart() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        List<Task> tasks = taskViewModel.getAllTasksLive().getValue();
        List<Label> labels = taskViewModel.getAllLabelsLive().getValue();
        Log.d("test", "pieChart: 标签：" + labels.size());
        Log.d("test", "pieChart: 任务：" + tasks.size());
//        for (Label label : labels) {
//            Log.d("test", "pieChart: 标签："+label.toString());
//            int count = getLabelCount(tasks, label.getId());
//            PieEntry pieEntry = new PieEntry(count, label.getName());
//            entries.add(pieEntry);
//            colors.add(label.getColor());
//        }

//        // 数据处理
//        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.task_type));
//        dataSet.setSliceSpace(3f);
//        dataSet.setIconsOffset(new MPPointF(0, 40));
//        dataSet.setSelectionShift(5f);
//        dataSet.setColors(colors);
//
//        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.WHITE);
//        pieChart.setData(data);
//        pieChart.highlightValues(null);
//        pieChart.invalidate();

    }

    private int getLabelCount(List<Task> tasks, int labelId) {
        int count = 0;
        for (Task task : tasks) {
            if (task.getLabelId() == labelId) {
                count++;
            }
        }
        return count;
    }

}