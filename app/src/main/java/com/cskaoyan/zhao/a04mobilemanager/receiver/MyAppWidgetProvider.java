package com.cskaoyan.zhao.a04mobilemanager.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.utils.ProcessManagerUtils;

/**
 * Created by zhao on 2016/8/15.
 */

//MyAppWidgetProvider 也是广播接受者
//重新父类的方法的时候，要不要调用了父类的方法，  得看父类的方法在做什么
// 如果父类该方法没有意义，则可以删除掉
    /*new Thread(){

            @Override
            public void run() {
                super.run();
            }
        }.start();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();*/

    //广播接受者
public class MyAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "MyAppWidgetProvider";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG,intent.getAction());
        
       /* if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
           // update();
        }*/
        super.onReceive(context, intent); //得看父类的方法在做什么
    }

    /*//我们可以更新一下widget的ui。保证每次放上去的时候，
    private void update() {
    }*/

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG,"onEnabled");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.i(TAG,"onUpdate");

        //去更新一下 当前的widget

        //AppWidgetManager systemService = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);

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


        //PendingIntent是一个 不会立即发送，先pending 。但是将来会发送的广播
        Intent intent = new Intent("com.cskaoyan.mywidget.click");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteviews.setOnClickPendingIntent(R.id.btn_proceewidget_clear, pendingIntent);

        appWidgetManager.updateAppWidget(name,remoteviews);


        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG,"onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG,"onDisabled");
        super.onDisabled(context);
    }


}
