package com.cskaoyan.zhao.a04mobilemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by zhao on 2016/8/11.
 */
public class AppInfo {

    //图标
    Drawable icon;

    //名字
    String  appName;

    //是否安装在sdcard
    boolean isSdcard;

    //是不是系统应用
    boolean isSystemApp;

    //是否加锁
    boolean isLocked;


    public Drawable getIcon() {
        return icon;
    }

    public String getAppName() {
        return appName;
    }

    public boolean isSdcard() {
        return isSdcard;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public String getPkgName() {
        return pkgName;
    }

    //应用的包名
    String pkgName;

    public AppInfo(Drawable icon, String appName, boolean isSdcard, boolean isSystemApp, boolean isLocked, String pkgName) {
        this.icon = icon;
        this.appName = appName;
        this.isSdcard = isSdcard;
        this.isSystemApp = isSystemApp;
        this.isLocked = isLocked;
        this.pkgName = pkgName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", appName='" + appName + '\'' +
                ", isSdcard=" + isSdcard +
                ", isSystemApp=" + isSystemApp +
                ", isLocked=" + isLocked +
                ", pkgName='" + pkgName + '\'' +
                '}';
    }
}
