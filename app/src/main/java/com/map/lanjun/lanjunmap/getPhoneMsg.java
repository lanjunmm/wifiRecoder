package com.map.lanjun.lanjunmap;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by ASUS on 2018/1/10.
 */

public class getPhoneMsg {
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }//Android系统版本号
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }//手机型号
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }//手机厂商
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();//
        }
        return null;
    }//获取IMEI
}
