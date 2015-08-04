package com.bsz.hanyue.hanyuemapview.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bsz.hanyue.hanyuemapview.Model.Wifi;
import com.bsz.hanyue.hanyuemapview.R;
import com.bsz.hanyue.hanyuemapview.UI.WifiResultListAdapter;
import com.bsz.hanyue.hanyuemapview.Utils.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class InputFragment extends Fragment {

    private Activity activity;
    private ListView listView;
    private WifiResultListAdapter wifiResultListAdapter;
    private Button saveButton;
    private EditText distanceEdit;
    private boolean isRun = true;
    private Wifi inputWifi;

    private DatabaseManager databaseManager;

    public InputFragment(Context context) {
        // Required empty public constructor
        isRun = false;
        this.activity = (Activity) context;
    }

    private void initView(View view) {
        wifiResultListAdapter = new WifiResultListAdapter(this.activity);
        listView = (ListView) view.findViewById(R.id.input_list_view);
        listView.setAdapter(wifiResultListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isRun = false;
                inputWifi = (Wifi)wifiResultListAdapter.getItem(position);
                List<Wifi> dbWifis = databaseManager.getWifisByBSSID(inputWifi.getBssID());
                List<Wifi> inputWifis = new ArrayList<>();
                inputWifis.add(inputWifi);
                for (Wifi wifi:dbWifis){
                    inputWifis.add(wifi);
                }
                wifiResultListAdapter.setData(inputWifis);
                saveButton.setVisibility(View.VISIBLE);
                distanceEdit.setVisibility(View.VISIBLE);
            }
        });
        distanceEdit = (EditText)view.findViewById(R.id.input_edit);
        saveButton = (Button)view.findViewById(R.id.input_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRun = true;
                saveButton.setVisibility(View.GONE);
                distanceEdit.setVisibility(View.GONE);
                inputWifi.setDistance(Integer.valueOf(distanceEdit.getText().toString()));
                databaseManager.add(inputWifi);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRun = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
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

    public void setWifis(List<Wifi> wifis) {
        if(isRun) {
            wifiResultListAdapter.setData(wifis);
        }
    }

    public void setDatabaseManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }
}
