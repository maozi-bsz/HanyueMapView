package com.bsz.hanyue.hanyuemapview.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bsz.hanyue.hanyuemapview.Model.Wifi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyue on 2015/7/21.
 */
public class DatabaseManager {

    public final static String SHEET2 = "wifi";
    public final static String SHEET1 = "prewifi";
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void add(List<Wifi> wifis) {
        for (Wifi wifi : wifis) {
            add(wifi);
        }
    }

    public void add(Wifi wifi) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("bssid", wifi.getBssID());
        contentValues.put("level", wifi.getLevel());
        contentValues.put("frequency", wifi.getFrequency());
        contentValues.put("name", wifi.getName());
        contentValues.put("date", wifi.getDate());
        if (wifi.getDistance() == 0) {
            database.insert(SHEET1, null, contentValues);
        } else {
            contentValues.put("distance", wifi.getDistance());
            database.insert(SHEET2, null, contentValues);
        }
    }

    public void clearPreWifi() {
        database.delete(SHEET1, null, null);
    }

    public List<String> getAllPreBSSID() {
        List<String> bssIDs = new ArrayList<>();
        String queryString = "SELECT bssid FROM "+SHEET1;
        Cursor c = database.rawQuery(queryString, null);
        while (c.moveToNext()) {
            String bssID = c.getString(c.getColumnIndex("bssid"));
            bssIDs.add(bssID);
        }
        c.close();
        return bssIDs;
    }

    public List<Wifi> getPreWifisByBSSID(String bssID) {
        ArrayList<Wifi> wifis = new ArrayList<>();
        String[] attributes = new String[]{bssID};
        String queryString = "SELECT * FROM "+SHEET1+" WHERE bssid=?";
        Cursor c = database.rawQuery(queryString, attributes);
        while (c.moveToNext()) {
            Wifi wifi = new Wifi();
            wifi.set_id(c.getInt(c.getColumnIndex("_id")));
            wifi.setBssID(c.getString(c.getColumnIndex("bssid")));
            wifi.setLevel(c.getInt(c.getColumnIndex("level")));
            wifi.setFrequency(c.getInt(c.getColumnIndex("frequency")));
            wifi.setName(c.getString(c.getColumnIndex("name")));
            wifi.setDate(c.getLong(c.getColumnIndex("date")));
            wifis.add(wifi);
        }
        c.close();
        return wifis;
    }

    public List<Wifi> getWifisByBSSID(String bssID) {
        ArrayList<Wifi> wifis = new ArrayList<>();
        String[] attributes = new String[]{bssID};
        String queryString = "SELECT * FROM "+SHEET2+" WHERE bssid=?";
        Cursor c = database.rawQuery(queryString, attributes);
        while (c.moveToNext()) {
            Wifi wifi = new Wifi();
            wifi.set_id(c.getInt(c.getColumnIndex("_id")));
            wifi.setBssID(c.getString(c.getColumnIndex("bssid")));
            wifi.setLevel(c.getInt(c.getColumnIndex("level")));
            wifi.setFrequency(c.getInt(c.getColumnIndex("frequency")));
            wifi.setName(c.getString(c.getColumnIndex("name")));
            wifi.setDistance(c.getInt(c.getColumnIndex("distance")));
            wifi.setDate(c.getLong(c.getColumnIndex("date")));
            wifis.add(wifi);
        }
        c.close();
        return wifis;
    }

    public Wifi getWifisByBSSIDAndLevel(Wifi oWifi) {
        Wifi wifi = new Wifi();
        String[] attributes = new String[]{oWifi.getBssID(), String.valueOf(oWifi.getLevel())};
        String queryString = "SELECT * FROM "+SHEET2+" WHERE bssid=? AND level>? ORDER BY level DESC LIMIT 1";
        Cursor c = database.rawQuery(queryString, attributes);
        while (c.moveToNext()) {
            wifi.set_id(c.getInt(c.getColumnIndex("_id")));
            wifi.setBssID(c.getString(c.getColumnIndex("bssid")));
            wifi.setLevel(c.getInt(c.getColumnIndex("level")));
            wifi.setFrequency(c.getInt(c.getColumnIndex("frequency")));
            wifi.setDistance(c.getInt(c.getColumnIndex("distance")));
            wifi.setName(c.getString(c.getColumnIndex("name")));
            wifi.setDate(c.getLong(c.getColumnIndex("date")));
        }
        c.close();
        return wifi;
    }

    public List<Wifi> getWifisByBSSIDAndLevel(List<Wifi> oWifis) {
        ArrayList<Wifi> wifis = new ArrayList<>();
        for (Wifi oWifi : oWifis) {
            Wifi wifi = getWifisByBSSIDAndLevel(oWifi);
            if (!(wifi.getBssID().isEmpty())){
                wifis.add(wifi);
            }
        }
        return wifis;
    }

    public void closeDatabase() {
        database.close();
    }
}
