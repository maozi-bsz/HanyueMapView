package com.bsz.hanyue.hanyuemapview.UI;

import android.app.Activity;
import android.content.Entity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bsz.hanyue.hanyuemapview.Model.Wifi;
import com.bsz.hanyue.hanyuemapview.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyue on 2015/8/12
 */
public class WifiChartListAdapter extends BaseAdapter {

    private Activity activity;

    private List<List<Wifi>> data;

    public WifiChartListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public List<Wifi> getItem(int position) {
        if (data != null) {
            return data.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Assemblies assemblies;
        if (convertView == null) {
            assemblies = new Assemblies();
            convertView = LayoutInflater.from(activity).inflate(R.layout.wifi_result_list_item_chart, parent, false);
            initView(convertView, assemblies);
            assemblies.setTag(position);
            convertView.setTag(assemblies);
        } else {
            assemblies = (Assemblies) convertView.getTag();
        }
        if (data == null || data.size() == 0) {
            return convertView;
        }
        List<Wifi> wifis = data.get(position);
        setContent(wifis, assemblies);
        return convertView;
    }

    private final class Assemblies {

        LineChart lineChart;

        private void setTag(Object position) {
            lineChart.setTag(position);
        }

    }

    private void initView(View view, Assemblies assemblies) {
        assemblies.lineChart = (LineChart) view.findViewById(R.id.wifi_chart_list_chart);
    }

    private void setContent(List<Wifi> wifis, Assemblies assemblies) {
        assemblies.lineChart.setDrawBorders(false);
        assemblies.lineChart.setDescription(wifis.get(0).getName());
        assemblies.lineChart.setNoDataTextDescription("no wifi recorder");
        assemblies.lineChart.setDrawGridBackground(false);
        assemblies.lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);
        assemblies.lineChart.setTouchEnabled(true);
        assemblies.lineChart.setDragEnabled(true);
        assemblies.lineChart.setScaleEnabled(true);
        assemblies.lineChart.setPinchZoom(false);
        assemblies.lineChart.setBackgroundColor(Color.rgb(114, 188, 223));

        // add data
        assemblies.lineChart.setData(getLineData(wifis)); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = assemblies.lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字号
        mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        assemblies.lineChart.animateX(0); // 立即执行的动画,x轴
    }

    private LineData getLineData(List<Wifi> wifis) {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < wifis.size(); i++) {
            times.add("" + i);
        }
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < wifis.size(); i++) {
            int y = -wifis.get(i).getLevel();
            entries.add(new Entry(y, i));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "wifi chart");
        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1f); // 线宽
        lineDataSet.setCircleSize(1f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色

        return new LineData(times, lineDataSet);
    }

    public void setData(List<List<Wifi>> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setSelectedData(List<Wifi> wifis) {
        this.data = new ArrayList<>();
        this.data.add(wifis);
        this.notifyDataSetChanged();
    }

}
