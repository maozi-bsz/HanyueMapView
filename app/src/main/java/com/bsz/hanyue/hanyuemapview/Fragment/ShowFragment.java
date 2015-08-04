package com.bsz.hanyue.hanyuemapview.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bsz.hanyue.hanyuemapview.Model.Wifi;
import com.bsz.hanyue.hanyuemapview.R;
import com.bsz.hanyue.hanyuemapview.UI.WifiResultListAdapter;
import com.bsz.hanyue.hanyuemapview.Utils.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowFragment extends Fragment {

    private Activity activity;
    private List<Wifi> wifis;
    private ListView listView;
    private WifiResultListAdapter wifiResultListAdapter;
    private boolean isRun = true;

    private DatabaseManager databaseManager;

    public ShowFragment(Context context) {
        // Required empty public constructor
        isRun = false;
        this.activity = (Activity) context;
    }

    private void initView(View view) {
        wifiResultListAdapter = new WifiResultListAdapter(this.activity);
        listView = (ListView) view.findViewById(R.id.show_list);
        listView.setAdapter(wifiResultListAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRun = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isRun = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        isRun = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isRun = true;
    }

    public void setWifis(List<Wifi> wifis){
        if (isRun) {
            this.wifis = wifis;
            wifiResultListAdapter.setData(databaseManager.getWifisByBSSIDAndLevel(wifis));//算法接入点
        }
    }

    public void setDatabaseManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }
}
