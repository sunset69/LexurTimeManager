package cc.lexur.lexurtimemanager.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.ValueFormatter;

import cc.lexur.lexurtimemanager.R;

public class ChartUtils {

    /**
     * 显示饼状图
     *
     * @param pieChart    饼状图控件
     * @param description 饼状图中间的描述
     * @param data        数据
     */
    public static void pieChart(PieChart pieChart, String description, PieData data) {

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

    /**
     * 折线图
     * @param lineChart
     * @param data
     */
    public static void lineChart(LineChart lineChart, LineData data) {

    }

    /**
     * 条形图
     * @param barChart
     * @param data 折线图数据
     */
    public static void barChart(Context context,BarChart barChart, BarData data){

        //设置样式
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(60); //设置做多显示个数
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.getAxisRight().setEnabled(false);

        //x轴样式
        ValueFormatter xAxisFormatter = new MyValueFormatter(context,barChart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);//通过x轴数字转换为文字

        //Y轴样式
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinimum(0); // this replaces setStartAtZero(true)

        //图例
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        //设置数据
            barChart.setData(data);
            barChart.notifyDataSetChanged();
        }


    static class MyValueFormatter extends ValueFormatter {

        Context context;

        String[] taskStatus = new String[]{
                "结束","延时","放弃","完成"
        };
        private final BarLineChartBase<?> chart;

        public MyValueFormatter(Context context,BarLineChartBase<?> chart) {
            this.context = context;
            this.chart = chart;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            String str = "";
            switch ((int) value){
                case TaskStatus.DOING:
                    str = context.getString(R.string.doing);
                    break;
                case TaskStatus.DELAY:
                    str = context.getString(R.string.delay);
                    break;
                case TaskStatus.ABORT:
                    str = context.getString(R.string.abort);
                    break;
                case TaskStatus.FINISH:
                    str = context.getString(R.string.finish);
                    break;
            }

            return str;
        }
    }
}
