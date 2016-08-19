package com.cskaoyan.zhao.a04mobilemanager.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zhao on 2016/8/9.
 */
public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    private static final String TAG = "MyDeviceAdminReceiver";



    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i(TAG,"endable");
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i(TAG,"endable");
        super.onDisabled(context, intent);
    }
}
