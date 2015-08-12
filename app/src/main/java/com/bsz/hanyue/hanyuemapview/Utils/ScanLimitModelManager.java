package com.bsz.hanyue.hanyuemapview.Utils;

import android.app.Activity;
import android.widget.Toast;

import com.bsz.hanyue.hanyuemapview.Interface.OnGotWifiResultListener;
import com.bsz.hanyue.hanyuemapview.Interface.OnLimitListener;
import com.bsz.hanyue.hanyuemapview.Model.Wifi;

import java.util.Date;
import java.util.List;

/**
 * Created by hanyue on 2015/7/24
 */
public class ScanLimitModelManager {

    private Activity activity;
    private WifiScanManager wifiScanManager;
    private DatabaseManager databaseManager;
    private OnLimitListener onLimitListener;
    private OnGotWifiResultListener onGotWifiResultListener;
    private Wifi wifi;
    private long time;
    private int count;

    public ScanLimitModelManager(WifiScanManager wifiScanManager, DatabaseManager databaseManager) {
        this.wifiScanManager = wifiScanManager;
        this.databaseManager = databaseManager;
        this.activity = wifiScanManager.getActivity();
        扫描次数为限();
    }

    public ScanLimitModelManager 扫描次数为限() {
        Toast.makeText(activity, "正在记录扫描次数", Toast.LENGTH_SHORT).show();
        initState();
        wifiScanManager.setOnGotWifiResultListener(onGotWifiResultListener = new OnGotWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                count++;
                if (count > 5) {
                    onLimitListener.onLimit();
                }
            }
        });
        return this;
    }

    public ScanLimitModelManager 距离最近的WIFI的储存次数为限() {
        Toast.makeText(activity, "正在记录WIFI储存次数", Toast.LENGTH_SHORT).show();
        initState();
        wifiScanManager.setOnGotWifiResultListener(onGotWifiResultListener = new OnGotWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                if (wifis.size() != 0 && count == 0) {
                    wifi = wifis.get(0);
                    count++;
                }
                if (databaseManager.getPreWifisByBSSID(wifi.getBssID()).size() > 5) {
                    onLimitListener.onLimit();
                }
            }
        });
        return this;
    }

    public ScanLimitModelManager 时间为限() {
        Toast.makeText(activity, "正在记录时间次数", Toast.LENGTH_SHORT).show();
        initState();
        wifiScanManager.setOnGotWifiResultListener(onGotWifiResultListener = new OnGotWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                if (count == 0) {
                    time = new Date().getTime();
                }
                if (((new Date().getTime() - time) / 1000) > 10) {
                    onLimitListener.onLimit();
                }
            }
        });
        return this;
    }

    private void initState() {
        if (onGotWifiResultListener != null) {
            wifiScanManager.removeListener(onGotWifiResultListener);
            onGotWifiResultListener = null;
        }
        count = 0;
        time = 0;
        wifi = null;
    }

    public void setOnLimitListener(OnLimitListener onLimitListener) {
        this.onLimitListener = onLimitListener;
    }

}
