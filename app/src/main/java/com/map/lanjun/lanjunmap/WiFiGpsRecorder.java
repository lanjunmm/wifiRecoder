package com.map.lanjun.lanjunmap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.baidu.location.LocationClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ASUS on 2017/12/12.
 */

public class WiFiGpsRecorder extends Service {
    private static WiFiAdmin wifi;
    private static List<ScanResult> list;
    private static String macAdr="";
    //gps
    private static baiduLoc gps;
    private static Context context;
    private static SaveFile mSaveFile;
    private static Handler handler = new Handler();

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getAndSaveDate();
            handler.postDelayed(this, 2000);//这里再次调用此Runnable对象，以实现每3秒实现一次的定时器操作
        }
    };
    public static void doStartService(Context cxt,Context appCtx,LocationClient mLocClient) {
        context = cxt;
        mSaveFile = new SaveFile();////得到存储位置如果文件不存在则创建文件
        Intent serviceIntent = new Intent(cxt, WiFiGpsRecorder.class);//在程序运行的过程中连接两个不同的组件
        cxt.startService(serviceIntent);//启动一个service或者传消息给一个运行的service
        wifi = new WiFiAdmin(context); //
        gps = new baiduLoc(context,mLocClient);///尝试定位？？
        runnable.run();
        Toast.makeText(context, "记录服务已开始！", Toast.LENGTH_LONG).show();
    }
    public static void getAndSaveDate() {
        wifi.startScan();
        list = wifi.getWifiList();
        System.out.println("wifi列表"+list);
        JSONObject json = toJson(list);
        System.out.println("全部"+json);
        if (json == null) return;
        mSaveFile.saveString(json.toString() + "/n");
    }
    private static JSONObject toJson(List<ScanResult> list) {
        JSONArray wifiArray = new JSONArray();
        for (int temp =0; temp< list.size();++temp) {
            JSONObject json = new JSONObject();
            try {
                json.put("SSID", list.get(temp).SSID);
                json.put("level", list.get(temp).level);
                json.put("frequency", list.get(temp).frequency);//添加
                wifiArray.put(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Error:Cannot convert URL 'G:/workspace/MyApplication/apache-httpcomponents-httpclient/apache-httpcomponents-httpclient.jar' to a file.
        JSONObject wifiObj = new JSONObject();
//        System.out.println("地理位置"+gps.getmLocation().getLatitude()+gps.getmLocation().getLongitude());
        try {
            if (gps.getmLocation() == null) return null;
            wifiObj.put("wifiList", wifiArray);
            macAdr = wifi.getMacAddress();
            wifiObj.put("macAdr", macAdr);
            wifiObj.put("time", System.currentTimeMillis());
            JSONObject gpsObj = new JSONObject();
            gpsObj.put("longitude", gps.getmLocation().getLongitude());
            gpsObj.put("latitude", gps.getmLocation().getLatitude());
            wifiObj.put("gps", gpsObj);
            wifiObj.put("brand",getPhoneMsg.getDeviceBrand());
            wifiObj.put("model",getPhoneMsg.getSystemModel());
            wifiObj.put("imei",getPhoneMsg.getIMEI(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiObj;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public static void stopService() {
        gps.removeUdate(context);
        Intent serviceIntent = new Intent(context, WiFiGpsRecorder.class);
        context.stopService(serviceIntent);
        handler.removeCallbacks(runnable);
        Toast.makeText(context, "记录服务已停止！", Toast.LENGTH_LONG).show();
    }
}
