package com.cskaoyan.zhao.a04mobilemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;

/**
 * Created by zhao on 2016/8/8.
 */
public class MyBootCompletetReceiver extends BroadcastReceiver {
    private static final String TAG = "My04xxxBootComRece";

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i(TAG,"onReceive");

        boolean phonesafe = MyApplication.getStateFromSp("phonesafe");

        if (phonesafe){
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            String imsi = MyApplication.getStringFromSp("imsi");
            if (!simSerialNumber.equals(imsi)){
                Log.i(TAG,"sim card is changed!");
                //给安全号码发短信
                String safenum = MyApplication.getStringFromSp("safenum");
                Log.i(TAG,safenum);
                //短信的管理器
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safenum,"","you phone is lost!",null,null);
            }
        }



    }
}
