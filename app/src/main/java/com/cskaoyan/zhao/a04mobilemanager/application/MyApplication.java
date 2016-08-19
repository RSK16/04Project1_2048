package com.cskaoyan.zhao.a04mobilemanager.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by zhao on 2016/8/5.
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    public final static int number =10;
    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");

        sp = getSharedPreferences("config", MODE_PRIVATE);
        super.onCreate();
    }


    public static void saveStateToSp(String key,boolean state){
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,state);
        edit.commit();

    }

    public  static boolean getStateFromSp(String name){
        return sp.getBoolean(name,false);
    }

    public static void saveStringToSp(String key,String str){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,str);
        edit.commit();

    }

    public static String getStringFromSp(String name){
        return sp.getString(name,"");
    }
    @Override
    public void onTerminate() {
        Log.i(TAG,"onTerminate");
        super.onTerminate();
    }
}
