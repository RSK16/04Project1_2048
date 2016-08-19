package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.utils.MyHttpUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    private  static  final  int MSG_OK =1;
    private static final int MSG_ERROR_IO =-1 ;
    private static final int MSG_ERROR_URL = -2;
    private static final int MSG_ERROR_JSON = -3;
    private static final int MSG_ERROR_NOTFOUND =-4 ;
    private static final int MSG_ERROR_SEVERINTERNAL = -5;

    private    String downloadurl  ;//="http://10.0.2.2/MoblieManager/mobilemanager.apk";

    private  int CURRENT_VERSION;
    private  String CURRENT_VERSION_NAME;

    private TextView tv_splash_version;
    private ProgressBar pb_splash_downloadapk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        pb_splash_downloadapk = (ProgressBar) findViewById(R.id.pb_splash_downloadapk);


        //去掉标题栏
       /* ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();*/

        CURRENT_VERSION=getVersionCode() ;
        CURRENT_VERSION_NAME=getVersionName();
        tv_splash_version.setText(CURRENT_VERSION_NAME);

        //这里需要通过用户的设置去看是否需要检测更新，如果用户没有开启自动检测更新，则不需要去check

        MyApplication application = (MyApplication) getApplication();

        boolean autoupdate = application.getStateFromSp("autoupdate");

        if (autoupdate)
            checkVersionFromServer();
        else{
            //如果没有开启更新的话，不能直接进入主界面，需要停一会
            new Thread(){
                @Override
                public void run() {

                    try {
                        sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    enterHome();
                }
            }.start();
        }

        copyDB("telocation.db");
        copyDB("antivirus.db");

    }

    //把assets目录下的 telocation.db  /dat/data/apppackge/
    private void copyDB(String dbname)  {


        File dbfile = new File("data/data/"+getPackageName()+"/"+dbname);

        if (dbfile.exists()){
            return;
        }


        AssetManager assetManager = getAssets();
        try {
            FileOutputStream fos = new FileOutputStream(dbfile);
            InputStream open = assetManager.open(dbname);
            byte[] b= new byte[1024];
            int len=-1;
            while ((len=open.read(b,0,1024))!=-1){
                fos.write(b,0,len);
            }

            open.close();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //HttpUrlConnection
    private void checkVersionFromServer() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                String path ="http://10.0.2.2/MobileManager/version.json";
                Message msg= new Message();
                try {
                    URL url = new URL(path);
                    HttpURLConnection httpurlConnection = (HttpURLConnection) url.openConnection();

                    httpurlConnection.setRequestMethod("GET");
                    httpurlConnection.setConnectTimeout(5000);
                    httpurlConnection.setReadTimeout(5000);
                    httpurlConnection.connect();

                    int responseCode = httpurlConnection.getResponseCode();
                    Log.i(TAG,"responseCode="+responseCode);
                    if (responseCode==200){
                        InputStream inputStream = httpurlConnection.getInputStream();
                        String jsonString = MyHttpUtils.getStringFromInputStream(inputStream);
                        Log.i(TAG,jsonString);

                        JSONObject json= new JSONObject(jsonString);

                        String new_verion = json.getString("new_verion");
                        String description = json.getString("description");
                        downloadurl = json.getString("downlaod_url");

                        String[] info={new_verion,description};
                        Log.i(TAG,"new_verion="+new_verion);

                        msg.what=MSG_OK;
                        msg.obj=info;


                    }else{
                        if (responseCode==404){
                            msg.what=MSG_ERROR_NOTFOUND;


                        }else if (responseCode==500){

                            msg.what=MSG_ERROR_SEVERINTERNAL;

                        }
                        
                        
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();

                    msg.what=MSG_ERROR_URL;
                 } catch (IOException e) {
                    e.printStackTrace();
                     msg.what=MSG_ERROR_IO;

                } catch (JSONException e) {
                    e.printStackTrace();

                     msg.what=MSG_ERROR_JSON;
                }finally {
                    myHandler.sendMessage(msg);

                }


            }
        }).start();


    }



    public String getVersionName(){

        String versionName="";

        //PackageManager 可以去获取任意app的信息
        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.cskaoyan.zhao.a04mobilemanager", 0);

            versionName = packageInfo.versionName;
            //


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  versionName;


    }
    public int getVersionCode(){

        int versionCode=-1;
        //PackageManager 可以去获取任意app的信息
        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.cskaoyan.zhao.a04mobilemanager", 0);
            versionCode = packageInfo.versionCode;
            //packageInfo.versionName

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  versionCode;


    }


    Handler myHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {


            switch (msg.what)
            {
                case    MSG_OK :
                    String[] info = (String[]) msg.obj;
                    int version_code_from_server= Integer.parseInt(info[0]);
                    //如果服务器的版本大于当前的版本，则提示用户去更新。
                    if (version_code_from_server>CURRENT_VERSION){
                        askUserToUpdate(info[1]);
                    }else{
                        //版本已经是最新，进入主页面
                        enterHome();
                    }
                break;

                case  MSG_ERROR_IO:
                case  MSG_ERROR_URL:
                case  MSG_ERROR_JSON:
                case  MSG_ERROR_NOTFOUND:
                case  MSG_ERROR_SEVERINTERNAL:
                    Toast.makeText(SplashActivity.this,"error="+msg.what,Toast.LENGTH_LONG).show();
                    enterHome();
                break;


            }

            super.handleMessage(msg);
        }
    };

    private void enterHome() {
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }

    private void askUserToUpdate(String description) {

        new AlertDialog.Builder(this)
                .setTitle("升级")
                .setMessage("发现新版本"+description+"\n是否要升级？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //下载服务器上的新版本，然后去安装
                        pb_splash_downloadapk.setVisibility(View.VISIBLE);
                        downloadAPK();
                    }


                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //进入主页面
                        enterHome();
                    }
                })
                .show();

    }


    //Xutils
    private void downloadAPK() {

        HttpUtils httpUtils = new HttpUtils();
        //
        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.i(TAG,"downloadurl="+downloadurl);
        httpUtils.download(downloadurl, sdCard + "/mobilemanager.apk", new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                File apkfile = responseInfo.result;
                Log.i(TAG,apkfile.getTotalSpace()+"");

                pb_splash_downloadapk.setVisibility(View.INVISIBLE);
                installAPK(apkfile);
            }
            //当下载进度有更新的时候 会call到
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                pb_splash_downloadapk.setMax((int) total);
                pb_splash_downloadapk.setProgress((int) current);

                super.onLoading(total, current, isUploading);
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Log.i(TAG,e.toString());
                //下载失败的时候也要进入主页面
                enterHome();
            }
        });


    }

    //负责安装 应用的 app 叫做PackageInstaller
    private void installAPK(File apkfile) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //如何判断 用户在一个系统的界面点击了cancle
        Log.i(TAG,"resultCode= "+ resultCode);
        if (resultCode==RESULT_CANCELED){
            enterHome();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
