package com.bsz.hanyue.hanyuemapview.UI;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsz.hanyue.hanyuemapview.Model.Wifi;
import com.bsz.hanyue.hanyuemapview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyue on 2015/7/19.
 */
public class WifiResultListAdapter extends BaseAdapter {

    private List<Wifi> data;
    private Activity activity;

    public WifiResultListAdapter(Context context) {
        this.activity = (Activity) context;
    }

    @Override
    public int getCount() {
        if (data == null || data.size() == 0) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.wifi_result_list_item, parent, false);
            initView(convertView, assemblies);
            assemblies.setTag(position);
            convertView.setTag(assemblies);
        } else {
            assemblies = (Assemblies) convertView.getTag();
        }
        if (data == null || data.size() == 0) {
            return convertView;
        }
        Wifi wifi = data.get(position);
        setContent(wifi, assemblies);
        return convertView;
    }

    private final class Assemblies {

        TextView wifiInfo;

        private void setTag(Object position) {
            wifiInfo.setTag(position);
        }

    }

    private void initView(View view, Assemblies assemblies) {
        assemblies.wifiInfo = (TextView) view.findViewById(R.id.info_text);
    }

    private void setContent(Wifi wifi, Assemblies assemblies) {
        if(wifi.getDistance()!=0) {
            assemblies.wifiInfo.setText(
                    "Name: " + wifi.getName() + "\nBSSID: " + wifi.getBssID() + "\nlevel: " + wifi.getLevel() + "\nfrequency: " + wifi.getFrequency()+"\ndistance"+wifi.getDistance());
        }else {
            assemblies.wifiInfo.setText(
                    "Name: " + wifi.getName() + "\nBSSID: " + wifi.getBssID() + "\nlevel: " + wifi.getLevel() + "\nfrequency: " + wifi.getFrequency());
        }
    }

    public void setData(List<Wifi> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

//    public static String getTimestamp(long tm) {
//        //定义显示格式
//        SimpleDateFormat sdf = new SimpleDateFormat(
//                "yyyy-MM-dd ");
//        //转码
//        String tms = sdf.format(tm);
//        //返回
//        return tms;
//    }
}
