package cc.lexur.lexurtimemanager.utils;

import android.app.PictureInPictureUiState;
import android.graphics.Color;
import android.graphics.Point;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChartUtils {

//    PieChart pieChart;
//    LineChart lineChart;
//
//    public ChartUtils() {
//    }
//
//    public ChartUtils(PieChart pieChart) {
//        this.pieChart = pieChart;
//    }
//
//    public ChartUtils(LineChart lineChart) {
//        this.lineChart = lineChart;
//    }

    public static void pieChart(PieChart pieChart, String description,PieData data){

        /**
         * 图标样式设计
         */

        pieChart.setUsePercentValues(true);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        // 饼状图中间部分
        pieChart.getDescription().setEnabled(true);
        pieChart.setCenterText(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);

        // 中间部分与外圈交界的半透明区域
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);

        // 外圈
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        // TODO 点击事件
//        pieChart.setOnChartValueSelectedListener();

        // 动画
        pieChart.animateY(1400, Easing.EaseInOutQuad);

        // 图例
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

        /**
         * 设置数据
         * 名称，个数，颜色
         */
//        ArrayList<PieEntry> entries = new ArrayList<>(); // 存放实体
//        PieDataSet dataSet = new PieDataSet(entries,"数据");// 图表数据
//        ArrayList<Integer> colors = new ArrayList<>();
//        PieData pieData = new PieData(dataSet);
//
//        entries.add(new PieEntry(1));
//        dataSet.setDrawIcons(false);
//        dataSet.setSliceSpace(3f);
//        colors.add(Color.RED);
//        dataSet.setColors(colors);

//        pieData.setValueFormatter(new PercentFormatter());
//        pieData.setValueTextSize(11f);
//        pieData.setValueTextColor(Color.WHITE);

        /**
         * 显示数据
         */
        pieChart.setData(data);

        pieChart.highlightValue(null);
        pieChart.invalidate();
    }


}
