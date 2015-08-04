package com.bsz.hanyue.hanyuemapview.Utils;

import com.bsz.hanyue.hanyuemapview.Model.Wifi;

import java.util.List;

/**
 * Created by hanyue on 2015/7/23.
 */
public interface OnGetWifiResultListener {

    public abstract void getScanResult(List<Wifi> wifis);

}
