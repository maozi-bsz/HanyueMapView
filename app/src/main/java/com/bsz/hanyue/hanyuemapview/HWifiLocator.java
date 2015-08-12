package com.bsz.hanyue.hanyuemapview;

import android.app.Activity;

import com.bsz.hanyue.hanyuemapview.Utils.DatabaseManager;
import com.bsz.hanyue.hanyuemapview.Utils.LocatorRunController;

/**
 * Created by hanyue on 2015/8/12
 */
public class HWifiLocator {

    private static LocatorRunController locatorRunController;

    public static LocatorRunController with(Activity activity) {
        if (locatorRunController == null)
        {
            locatorRunController = new LocatorRunController(activity);
        }
        return locatorRunController;
    }

    public static DatabaseManager getDatabaseManager(){
        if (locatorRunController!=null){
            return locatorRunController.getDatabaseManager();
        }
        return null;
    }

}
