package com.map.lanjun.lanjunmap;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;



import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ASUS on 2017/12/12.
 */

public class SaveFile {
    private File mFile;
    private FileOutputStream mOutStream;
    private FileInputStream mInputStream;
    private static String actionUrl = "http://192.168.43.63:3000/upload";
//    private static String actionUrl = "http://120.78.205.45:3000/upload";
    private static String uploadFilename =Environment.getExternalStorageDirectory().getAbsolutePath()+"/wifigps";
    public SaveFile() {
        super();
        mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/wifigps");
        try {
            if (!mFile.exists()){
                System.out.println("创建文件");
                mFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("出错了"+e.getMessage());
            e.printStackTrace();
        }
    }//得到存储位置如果文件不存在则创建文件
    public void saveString(String str) {
        try {
            mOutStream = new FileOutputStream(mFile, true);
            mOutStream.write(str.getBytes());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }//将字符串写入文件
    public void getFile() {
        try {
            mInputStream = new FileInputStream(mFile);
            byte[] result = new byte[2048];
            mInputStream.read(result);
            System.out.println(new String(result, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (mOutStream != null)
                mOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void uploadFile(String filename, Context context) {
        String end = "/r/n";
        String Hyphens = "--";
        String boundary = "*****";
        try
        {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
      /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
      /* 设定传送的method=POST */
            con.setRequestMethod("POST");
      /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            con.setRequestProperty("Content-Disposition", "attachment;name=\""
                    + filename + "\";filename=\"" + filename + "\"");
      /* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());//发送请求，把请求参数传到服务器
      /* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(uploadFilename);
      /* 设定每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
      /* 从文件读取数据到缓冲区 */
            while ((length = fStream.read(buffer)) != -1)
            { //从文件中读取数据发送
        /* 将数据写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            fStream.close();//要求立即将缓冲区的数据输出到接收方。
            ds.flush();
      /* 取得Response内容 */
            InputStream is = con.getInputStream();//请求发送成功之后，即可获取响应的状态码
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            System.out.println("上传成功");
            ds.close();//释放请求的网络资源
            deleteFile();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败" + e.getMessage());
        }
    }
    public static void upload(final String filename, final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                uploadFile(filename, context);
            }
        };
        runnable.run();
    }

    public static void deleteFile(){
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/wifigps");
        if (mFile != null) mFile.delete();
    }
}
