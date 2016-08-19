package com.cskaoyan.zhao.a04mobilemanager.service;

import android.app.ActivityManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.bean.ProcessInfo;
import com.cskaoyan.zhao.a04mobilemanager.receiver.MyAppWidgetProvider;
import com.cskaoyan.zhao.a04mobilemanager.utils.ProcessManagerUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyAppWidgetService extends Service {

    private Context context;

    public MyAppWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = MyAppWidgetService.this;

        Log.i("MyAppWidgetService","registerReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cskaoyan.mywidget.click");
        registerReceiver(new MyKillBgProcessReceiver(),intentFilter);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);

                //拿到当前的进程数
                int processNumber = ProcessManagerUtils.getProcessNumber(context);
                //拿到当前的可用内存
                long availRam = ProcessManagerUtils.getAvailRam(context);
                String availRamS = Formatter.formatFileSize(context, availRam);


                //appWidgetManager 去更新Launcher上的我们的UI控件 （跨进程）
                //参数一
              /*  ComponentName name= new ComponentName(context,MyAppWidgetProvider.class);
                //参数二  去寻找桌面上要更新的widget  RemoteViews views
                RemoteViews remoteviews = new RemoteViews("com.cskaoyan.zhao.a04mobilemanager", R.layout.processmanager_appwidget);

                remoteviews.setTextViewText(R.id.tv_processwidget_count,"正在运行的软件:"+processNumber+"个");
                remoteviews.setTextViewText(R.id.tv_processwidget_memory,"可用内存:"+availRamS);*/


               // appWidgetManager.updateAppWidget(name,remoteviews);

            }
        };

        Timer timer = new Timer();

        timer.schedule(timerTask,1000,30000);


    }

    class MyKillBgProcessReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //去kill 后台的进程。
            Log.i("MyKBGReceiver",intent.getAction());
            ActivityManager ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            List<ProcessInfo> allProcessInfo = ProcessManagerUtils.getAllProcessInfo(MyAppWidgetService.this);

            for (ProcessInfo pinfo: allProcessInfo) {

                if (pinfo.getPkgNmame().equals("com.cskaoyan.zhao.a04mobilemanager"))
                   continue;

                ams.killBackgroundProcesses(pinfo.getPkgNmame());
            }

            //kill之后，更新widget的显示
            AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);
            //拿到当前的进程数
            int processNumber = ProcessManagerUtils.getProcessNumber(context);
            //拿到当前的可用内存
            long availRam = ProcessManagerUtils.getAvailRam(context);
            String availRamS = Formatter.formatFileSize(context, availRam);
            //appWidgetManager 去更新Launcher上的我们的UI控件 （跨进程）
            //参数一
            ComponentName name= new ComponentName(context,MyAppWidgetProvider.class);
            //参数二  去寻找桌面上要更新的widget  RemoteViews views
            RemoteViews remoteviews = new RemoteViews("com.cskaoyan.zhao.a04mobilemanager", R.layout.processmanager_appwidget);
            remoteviews.setTextViewText(R.id.tv_processwidget_count,"正在运行的软件:"+processNumber+"个");
            remoteviews.setTextViewText(R.id.tv_processwidget_memory,"可用内存:"+availRamS);
            appWidgetManager.updateAppWidget(name,remoteviews);

        }
    }
}
