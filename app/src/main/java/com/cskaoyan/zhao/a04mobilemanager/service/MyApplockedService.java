package com.cskaoyan.zhao.a04mobilemanager.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.cskaoyan.zhao.a04mobilemanager.activity.AppLockedActivity;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyAppLockDao;

public class MyApplockedService extends Service {
    private static final String TAG = "MyApplockedService";

    private MyAppLockDao dao;

    public MyApplockedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    boolean flag;
    @Override
    public void onCreate() {
        flag=true;
        dao = new MyAppLockDao(this);
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(flag){

                    ActivityManager activityManager   = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

                    //获取当前正在前台运行的app
                    String processName = activityManager.getRunningAppProcesses().get(0).processName;

                    Log.i(TAG,"current running app =" +processName);

                    //如何判断当前运行的app是否是加锁的？
                    boolean locked = dao.queryAppIsLocked(processName);
                    //如果加锁 ，去显示一个我们的页面，告诉该用户应用已经加锁
                    if (locked){
                        // AndroidRuntimeException:
                        // Calling startActivity() from outside of an Activity  context
                        // requires the FLAG_ACTIVITY_NEW_TASK flag.
                        // 没有任务栈的时候，不知道Activity放哪里 ,加上该Flag的时候，会新建任务栈
                        // 如果当前的app 任务栈存在，会直接使用之前的任务栈
                        Intent intent = new Intent(MyApplockedService.this, AppLockedActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("pkgName",processName);
                        startActivity(intent);
                    }


                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();



        super.onCreate();
    }

    @Override
    public void onDestroy() {

        flag=false;
        super.onDestroy();
    }
}
