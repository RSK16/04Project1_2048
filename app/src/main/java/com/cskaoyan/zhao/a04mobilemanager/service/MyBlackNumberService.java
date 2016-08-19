package com.cskaoyan.zhao.a04mobilemanager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyBlackNumberDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyBlackNumberService extends Service {
    private static final String TAG = "MyBlackNumberService";

    public MyBlackNumberService() {
    }

    
    MyBlackNumberDao dao;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        
        dao= new MyBlackNumberDao(this);

        //动态注册，去拦截端新的广播接受者

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //有序广播的优先级 1000 ~  -1000,保证最先受到短信
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(new MyBlackNumberSMSReceiver(),intentFilter);

        TelephonyManager  telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new MyBlackNumberListener(),PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    class MyBlackNumberListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (state==TelephonyManager.CALL_STATE_RINGING){
                //看看来电是不是我们需要拦截的号码 ,如果是就挂掉该来电

                int mode = dao.queryMode(incomingNumber);
                if (mode==1||mode==3){
                    //把电话挂断的逻辑
                    endCall();
                    Log.i(TAG,"this number "+ incomingNumber +" need block");
                }


            }

            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void endCall() {
        // getITelephony().endCall();
        // getITelephony() = ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE))
        // ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE)) .endCall();

        //通过反射来调用
        //ServiceManager.getService(Context.TELEPHONY_SERVICE)
        //ServiceManager.class;
        try {
            Class<?> ServiceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManagerClass.getMethod("getService", String.class);
            IBinder invoke = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony.Stub.asInterface(invoke).endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    class MyBlackNumberSMSReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
                Log.i(TAG,"receive sms!");
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for(Object obj:objs){
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                    String body = smsMessage.getMessageBody();
                    String sender = smsMessage.getOriginatingAddress();

                    int mode = dao.queryMode(sender);
                    if (mode==2||mode==3){
                        Log.i(TAG,"this number "+ sender +" 需要拦截短信");
                        //终止有序广播
                        abortBroadcast();
                    }


                }
        }
    }
}
