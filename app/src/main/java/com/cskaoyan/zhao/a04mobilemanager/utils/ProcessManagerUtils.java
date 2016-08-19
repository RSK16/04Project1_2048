package com.cskaoyan.zhao.a04mobilemanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.cskaoyan.zhao.a04mobilemanager.bean.AppInfo;
import com.cskaoyan.zhao.a04mobilemanager.bean.ProcessInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2016/8/13.
 */
public class ProcessManagerUtils {


    //获取当前的进程数
    public static int getProcessNumber(Context ctx){
        ActivityManager ams = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ams.getRunningAppProcesses();
        return  runningAppProcesses.size();
    }

    public static long getTotalRam(Context ctx){
        ActivityManager ams = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ams.getMemoryInfo(memoryInfo);
       return   memoryInfo.totalMem;

    }

    public static long getAvailRam(Context ctx){
        ActivityManager ams = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ams.getMemoryInfo(memoryInfo);
        return   memoryInfo.availMem;

    }

    public static List<ProcessInfo> getAllProcessInfo(Context ctx){

        List<ProcessInfo> processInfoList = new ArrayList<ProcessInfo>();
        ActivityManager ams = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ams.getRunningAppProcesses();


        PackageManager packageManager = ctx.getPackageManager();

        for (ActivityManager.RunningAppProcessInfo pInfo:  runningAppProcesses) {



            String pkgName = pInfo.processName;
            //ischeck
            boolean ischeck =false;

            //isSystem
            boolean isSystem =false;
            int ram=0;
            String appname="";
            try {

                //ram
                int[] pids =new int[]{pInfo.pid};
                Debug.MemoryInfo[] processMemoryInfo = ams.getProcessMemoryInfo(pids);
                Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
                //当前process占用的内存大小
                ram = memoryInfo.getTotalPss();//usage in kB.


                ApplicationInfo appinfo = packageManager.getApplicationInfo(pkgName, 0);
                //name
                 appname = appinfo.loadLabel(packageManager).toString();
                 if( (appinfo.flags &ApplicationInfo.FLAG_SYSTEM)== 1  ){
                   isSystem =true;
                 }
                //icon
                Drawable icon = appinfo.loadIcon(packageManager);

                ProcessInfo p=new ProcessInfo(icon,appname,ram,ischeck,isSystem,pkgName);
                processInfoList.add(p);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

                ProcessInfo p=new ProcessInfo(null,pkgName,ram,ischeck,true,pkgName);
                processInfoList.add(p);
            }


        }



        return  processInfoList;
    }
}
