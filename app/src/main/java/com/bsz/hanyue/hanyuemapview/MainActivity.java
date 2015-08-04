package com.bsz.hanyue.hanyuemapview;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.bsz.hanyue.hanyuemapview.Fragment.InputFragment;
import com.bsz.hanyue.hanyuemapview.Fragment.ShowFragment;
import com.bsz.hanyue.hanyuemapview.Model.Wifi;
import com.bsz.hanyue.hanyuemapview.UI.DrawerAdapter;
import com.bsz.hanyue.hanyuemapview.UI.FragmentTabAdapter;
import com.bsz.hanyue.hanyuemapview.UI.FragmentTabAdapter.OnRgsExtraCheckedChangedListener;
import com.bsz.hanyue.hanyuemapview.Utils.DatabaseManager;
import com.bsz.hanyue.hanyuemapview.Utils.OnGetWifiResultListener;
import com.bsz.hanyue.hanyuemapview.Utils.OnLimitListener;
import com.bsz.hanyue.hanyuemapview.Utils.ScanLimitModelManager;
import com.bsz.hanyue.hanyuemapview.Utils.WifiScanManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private Activity activity = this;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private View lastView;
    private int count;

    private void initDrawerMenu() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        final DrawerAdapter drawerAdapter = new DrawerAdapter(activity);
        drawerList.setAdapter(drawerAdapter);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastView == null) {
                    view.setScaleX(1.3f);
                    view.setScaleY(1.3f);
                    lastView = view;
                } else {
                    if (!(view == lastView)) {
                        view.setScaleX(1.3f);
                        view.setScaleY(1.3f);
                        lastView.setScaleX(1.0f);
                        lastView.setScaleY(1.0f);
                        lastView = view;
                        count = 0;
                    } else {
                        count++;
                        if (count > 2) {
                            count = 0;
                        }
                    }
                }
                switch (position) {
                    case 0:
                        scanManager.强制扫描();
                        break;
                    case 1:
                        scanManager.自循环扫描();
                        break;
                }
                switch (count) {
                    case 0:
                        scanLimitModelManager.扫描次数为限();
                        break;
                    case 1:
                        scanLimitModelManager.距离最近的WIFI的储存次数为限();
                        break;
                    case 2:
                        scanLimitModelManager.时间为限();
                        break;
                }
            }
        });
    }

    private ArrayList<Fragment> fragmentArrayList;
    private InputFragment inputFragment;
    private ShowFragment showFragment;
    private RadioGroup radioGroup;

    private void initFragment() {
        radioGroup = (RadioGroup) findViewById(R.id.main_view_radio);
        inputFragment = new InputFragment(activity);
        showFragment = new ShowFragment(activity);
        inputFragment.setDatabaseManager(databaseManager);
        showFragment.setDatabaseManager(databaseManager);
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(inputFragment);
        fragmentArrayList.add(showFragment);
    }

    private void initView() {
        initDrawerMenu();
        initFragment();
        FragmentTabAdapter fragmentTabAdapter = new FragmentTabAdapter((FragmentActivity) activity, fragmentArrayList, R.id.main_fragment_content, radioGroup);
        fragmentTabAdapter.setOnRgsExtraCheckedChangedListener(new OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                super.OnRgsExtraCheckedChanged(radioGroup, checkedId, index);

            }
        });
    }

    private WifiScanManager scanManager;

    private void initScanManager() {
        scanManager = new WifiScanManager(this);
        scanManager.setOnGetWifiResultListener(new OnGetWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                databaseManager.add(wifis);
            }
        });
    }

    private ScanLimitModelManager scanLimitModelManager;

    private void initScanLimitManager() {
        scanLimitModelManager = new ScanLimitModelManager(activity, scanManager, databaseManager);
        scanLimitModelManager.setOnLimitListener(new OnLimitListener() {
            @Override
            public void onLimit() {
                inputFragment.setWifis(getAverageWifis());
                showFragment.setWifis(getAverageWifis());
                databaseManager.clearPreWifi();
            }
        });
    }

    private List<Wifi> getAverageWifis() {
        List<Wifi> wifis = new ArrayList<>();
        List<String> bssIDs = getWifiInDatabase();
        for (int i = 0; i < bssIDs.size(); i++) {
            Wifi wifi = getAverage(databaseManager.getPreWifisByBSSID(bssIDs.get(i)));
            wifis.add(wifi);
        }
        return wifis;
    }

    private List<String> getWifiInDatabase() {
        List<String> allBssids = databaseManager.getAllPreBSSID();
        List<String> bssids = new ArrayList<>();
        for (int i = 0; i < allBssids.size(); i++) {
            boolean flag = true;
            for (int j = 0; j < bssids.size(); j++) {
                if (bssids.get(j) == allBssids.get(i)) {
                    flag = false;
                }
            }
            if (flag) {
                bssids.add(allBssids.get(i));
            }
        }
        return bssids;
    }

    private Wifi getAverage(List<Wifi> wifis) {
        int sub = 0;
        for (int i = 0; i < wifis.size(); i++) {
            sub += wifis.get(i).getLevel();
        }
        int averageLevel = sub/wifis.size();
        Wifi wifi = wifis.get(0);
        wifi.setLevel(averageLevel);
        return wifi;
    }

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = new DatabaseManager(this);
        initView();
        initScanManager();
        initScanLimitManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanManager.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        scanManager.reStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseManager.closeDatabase();
    }
}
