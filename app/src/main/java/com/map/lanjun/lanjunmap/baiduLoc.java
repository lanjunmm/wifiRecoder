package com.map.lanjun.lanjunmap;

import android.content.Context;
import android.os.Handler;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by ASUS on 2017/12/12.
 */

public class baiduLoc {
    private static Context ctx;
    private static double lati = 0.0;
    private static double lonti = 0.0;
    private static LocationClient mclient;
    private static BDLocation mloc;
    private static Handler handler = new Handler();

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getLocations();
            handler.postDelayed(this, 1000);//这里再次调用此Runnable对象，以实现每3秒实现一次的定时器操作
        }
    };

    public baiduLoc(Context appCtx,LocationClient mLocClient){
        ctx=appCtx;
        mclient=mLocClient;
        runnable.run();
    }
    public static void getLocations(){
        mloc=mclient.getLastKnownLocation();
        lati= mloc.getLatitude();
        lonti=mloc.getLongitude();
        System.out.println("定位"+lati+lonti);
    }
    public BDLocation getmLocation() {
        return mloc;
    }
    public void removeUdate(Context context){
        mclient.stop();
    }
}
