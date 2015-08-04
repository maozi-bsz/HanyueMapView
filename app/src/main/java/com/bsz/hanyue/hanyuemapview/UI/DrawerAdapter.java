package com.bsz.hanyue.hanyuemapview.UI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsz.hanyue.hanyuemapview.R;

/**
 * Created by hanyue on 2015/7/23.
 */
public class DrawerAdapter extends BaseAdapter {

    Activity activity;
    private String[] itemTexts = {"强制扫描","自循环扫描"};
    private TextView textView;

    public DrawerAdapter(Context context) {
        this.activity = (Activity) context;
    }

    public DrawerAdapter(Context context,String[] itemTexts) {
        this.activity = (Activity) context;
        this.itemTexts = itemTexts;
    }

    @Override
    public int getCount() {
        return itemTexts.length;
    }

    @Override
    public Object getItem(int position) {
        return itemTexts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.switch_list_item,null);
            textView = (TextView)convertView.findViewById(R.id.switch_list_model);
            textView.setText(itemTexts[position]);
            convertView.setTag(textView);
        }else {
            textView = (TextView)convertView.getTag();
        }
        return convertView;
    }
}
