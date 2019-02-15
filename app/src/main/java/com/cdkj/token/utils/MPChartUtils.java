package com.cdkj.token.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * @updateDts 2019/1/16
 */
public class MPChartUtils {

    /**
     * 设置LineChart的样式
     */
    public static void setLineChartStyle(LineChart lineChart) {

        //===========================设置一些表头  手势的交互等============================
//        Description description = new Description();
//        description.setText("测试图表");//右下角显示  表格的名字
//        description.setTextColor(Color.RED);//名字的演策
//        description.setTextSize(20);//表格名字字体大小
//        mBinding.lineChart.setDescription(description);//设置图表描述信息
        lineChart.setNoDataText("没有数据哦");//没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawBorders(false);//禁止绘制图表边框的线
//        lineChart.animateXY(1000, 1000);//启用动画
        //lineChart.setBorderColor(); //设置 chart 边框线的颜色。
        //lineChart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
        //lineChart.setLogEnabled(true);//打印日志
        //lineChart.notifyDataSetChanged();//刷新数据
        //lineChart.invalidate();//重绘

        lineChart.setTouchEnabled(false); // 设置是否可以触摸
        lineChart.setDragEnabled(false);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放 x和y轴, 默认是true
        lineChart.setScaleXEnabled(false); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(false); //是否可以缩放 仅y轴
        lineChart.setPinchZoom(false);  //设置x轴和y轴能否同时缩放。默认是否
        lineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
//        mBinding.lineChart.setHighlightEnabled(false); //这个好像是设置阴影的  但是会报错 //If set to true, highlighting/selecting values via touch is possible for all underlying DataSets.
        lineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setAutoScaleMinMaxEnabled(false);
        lineChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

//=================================设置X  Y轴的样式===========================
        //透明化图例
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);

        //隐藏x轴描述  就是设置的label
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);//绘制x轴的表格线
        xAxis.setDrawGridLines(false);//是否绘制表格内部为小格子  这两句就是 内部的x  y  轴线
        xAxis.setDrawLabels(false);//是否绘制  m默认右下角的文字

//        xAxis.setEnabled(false);//是否启用x轴  不启用  因为不需要显示X轴的数据
//        xAxis.setDrawLabels(true);//是否绘制x轴的外边框

        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setEnabled(false);//禁用右边y轴
        axisRight.setDrawAxisLine(false);//是否绘制y轴表格线
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y坐标显示在哪里  里面 还是外面
        yAxis.setEnabled(true);//是否禁用左边的y轴
        yAxis.setDrawAxisLine(true);//是否绘制y轴边框线
        yAxis.setDrawZeroLine(true);//是否从0开始绘制,默认会有一段间距
        yAxis.setTextColor(Color.parseColor("#666666"));//设置y轴数值的字体颜色

    }

    /**
     * 设置折线图的数据
     *
     * @param lineChart
     * @param mdata
     */
    public static void setLineChartData(LineChart lineChart, ArrayList<Entry>... mdata) {
        if (lineChart == null || mdata.length == 0) {
            return;
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (ArrayList<Entry> mdatum : mdata) {
            LineDataSet set = new LineDataSet(mdatum, "label");//这个label(图例)  会再最下面显示   已禁用
            setLineChartSetStyle(set);
            dataSets.add(set);
            //            dataSets.add(set1); // add the datasets
        }
        LineData data = new LineData(dataSets);
        // 添加到图表中
        lineChart.setData(data);
        lineChart.animateX(1000);//启用动画
        //绘制图表
        lineChart.invalidate();
    }

    /**
     * 设置数据  定点  折线圆滑度  make等
     *
     * @param set1
     */
    private static void setLineChartSetStyle(LineDataSet set1) {
        set1.setColor(Color.parseColor("#6455E6"));//设置线的颜色 6455E6
        set1.setLineWidth(3f);//设置线宽
//            set1.setCircleColor(Color.BLACK);//设置顶点显示圆圈的 颜色
//            set1.setCircleRadius(3f);//设置焦点圆心的大小
        set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
//            set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
//            set1.setHighlightEnabled(true);//是否禁用点击高亮线
//            set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
//            set1.setValueTextSize(9f);//设置显示值的文字大小
        set1.setDrawFilled(true);//设置禁用范围背景填充
        set1.setDrawCircles(false);//是否绘制定点小圆圈
        set1.setDrawValues(false);//是否绘制x轴的值
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置平滑的曲线  默认是直线
        set1.setCubicIntensity(0.2f);//设置平滑度 也就是弯曲度   值越大越平滑
        // 填充曲线下方的区域，颜色，透明度。
        set1.setDrawFilled(true);
        set1.setFillAlpha(90);
        set1.setFillColor(Color.parseColor("#6455E6"));
        set1.setDrawValues(false);//是否显示顶点的值  默认显示
    }
}
