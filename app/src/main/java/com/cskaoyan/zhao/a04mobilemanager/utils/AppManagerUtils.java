package com.cskaoyan.zhao.a04mobilemanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.cskaoyan.zhao.a04mobilemanager.bean.AppInfo;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyAppLockDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2016/8/11.
 */
public class AppManagerUtils {


    //算出来的结果是字节
    public  static  long getSDcardAvailableSize(){

        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        StatFs statFs = new StatFs(absolutePath);
        long availableBlocksLong=0;
        long blockSizeLong=0;
        if (Build.VERSION.SDK_INT>=18){
              availableBlocksLong = statFs.getAvailableBlocksLong();
              blockSizeLong = statFs.getBlockSizeLong();
        }else {
            availableBlocksLong = statFs.getAvailableBlocks();
            blockSizeLong = statFs.getBlockSize();
        }
        return  availableBlocksLong*blockSizeLong;

    }
    public static long getRomAvailableSize(){

        //内部存储空间的路径
        String absolutePath = Environment.getDataDirectory().toString();
        StatFs statFs = new StatFs(absolutePath);
        long availableBlocksLong=0;
        long blockSizeLong=0;
        if (Build.VERSION.SDK_INT>=18){
            availableBlocksLong = statFs.getAvailableBlocksLong();
            blockSizeLong = statFs.getBlockSizeLong();
        }else {
            availableBlocksLong = statFs.getAvailableBlocks();
            blockSizeLong = statFs.getBlockSize();
        }
        return  availableBlocksLong*blockSizeLong;
    }


    public static List<AppInfo> getAllAppInfo(Context ctx){


        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        MyAppLockDao dao = new MyAppLockDao(ctx);

        List<AppInfo> resultList = new ArrayList<AppInfo>();
        PackageManager packageManager = ctx.getPackageManager();

        //获取当前手机上的所有应用信息
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);

        for (ApplicationInfo info: installedApplications) {

            //如何从代表每一个应用的ApplicationInfo 的实例 info中拿到我们需要的数据

            //图标，包名，应用名，是否sdcard 是否系统应用
            String pkgName =  info.packageName;

            Drawable icon = info.loadIcon(packageManager);
            String appname = info.loadLabel(packageManager).toString();

            boolean isSystem=false;
            boolean isSDcard=false;

            //假设系统是这么赋值的
//            int ISSDCARD =2; //10
//            int ISSYSTEM= 4; //100
            //如果安装在sd卡上，
//            info.flags= info.flags| ISSDCARD;  //2 4
            //100  110

            if ((info.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                //说明是系统应用
                isSystem=true;
            }

            if ((info.flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                //说明安装在sd卡上
                isSDcard=true;
            }


            boolean isLocked = dao.queryAppIsLocked(pkgName);
            //默认返回的是所有应用都是没有加锁的
            AppInfo appinfo = new AppInfo(icon,appname,isSDcard,isSystem,isLocked,pkgName);

            //最后把这个appinfo 防到我们的
            resultList.add(appinfo);

           /* try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }


        return  resultList;
    }

}
