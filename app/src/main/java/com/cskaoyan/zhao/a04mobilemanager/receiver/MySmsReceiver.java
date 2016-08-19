package com.cskaoyan.zhao.a04mobilemanager.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.service.MyLocationUpdateService;

/**
 * Created by zhao on 2016/8/9.
 */
public class MySmsReceiver extends BroadcastReceiver {


    private static final String TAG ="04mobileMySmsReceiver" ;


    Context ctx;
    //接收短信
    @Override
    public void onReceive(Context context, Intent intent) {

        ctx= context;
        boolean phonesafe = MyApplication.getStateFromSp("phonesafe");

        if (phonesafe){

            Log.i(TAG,"receive sms!");

            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();
                String sender = smsMessage.getOriginatingAddress();


                Log.i(TAG,"body="+body + "sender = "+ sender);

                String safenum = MyApplication.getStringFromSp("safenum");
                if (sender.equals(safenum)){


                    switch (body){
                        case  "*#alarm#*":
                            palyAlarm();
                            break;
                        case  "*#location#*":

                            sendLoaction();
                            break;
                        case  "*#lockscreen#*":

                            lockscreen();
                            break;

                        case  "*#wipedata#*":

                            wipedata();
                            break;
                    }


                }
            }


        }
    }


    private void wipedata() {
        //
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.wipeData(0);
    }

    private void lockscreen() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.lockNow();
        devicePolicyManager.resetPassword("123",DevicePolicyManager.FLAG_MANAGED_CAN_ACCESS_PARENT);
    }

    // WIFI
    // GPS (搜星)
    //发送两次短信，去获取位置
    //
    private void sendLoaction() {
        ctx.startService(new Intent(ctx, MyLocationUpdateService.class));

        String latitude = MyApplication.getStringFromSp("latitude");
        String longitude = MyApplication.getStringFromSp("longitude");

        Log.i(TAG,"latitude="+latitude+"longitude=" + longitude);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("5556","","you phone is lost!",null,null);
    }

    private void palyAlarm() {
        Log.i(TAG,"play alarm!");
        //Mediaplayer

        MediaPlayer player = MediaPlayer.create(ctx, R.raw.alarm);
        player.setLooping(true);
        player.setVolume(1.0f, 1.0f);
        player.start();

        Log.i(TAG,"play alarm over!");

    }
}
