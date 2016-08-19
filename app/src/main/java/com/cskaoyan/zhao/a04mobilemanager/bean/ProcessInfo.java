package com.cskaoyan.zhao.a04mobilemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by zhao on 2016/8/13.
 */
public class ProcessInfo {

    Drawable icon;
    String appname;
    long appRam;
    boolean isCheck;
    boolean isSystem;
    String pkgNmame;

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "icon=" + icon +
                ", appname='" + appname + '\'' +
                ", appRam=" + appRam +
                ", isCheck=" + isCheck +
                ", isSystem=" + isSystem +
                '}';
    }

    public ProcessInfo(Drawable icon, String appname, long appRam, boolean isCheck, boolean isSystem) {
        this.icon = icon;
        this.appname = appname;
        this.appRam = appRam;
        this.isCheck = isCheck;
        this.isSystem = isSystem;
    }


    public ProcessInfo(Drawable icon, String appname, long appRam, boolean isCheck, boolean isSystem, String pkgNmame) {
        this.icon = icon;
        this.appname = appname;
        this.appRam = appRam;
        this.isCheck = isCheck;
        this.isSystem = isSystem;
        this.pkgNmame = pkgNmame;
    }

    public String getPkgNmame() {
        return pkgNmame;
    }

    public void setPkgNmame(String pkgNmame) {
        this.pkgNmame = pkgNmame;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public long getAppRam() {
        return appRam;
    }

    public void setAppRam(long appRam) {
        this.appRam = appRam;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }
}
