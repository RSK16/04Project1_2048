package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.view.SettingItem;

public class Setup2Activity extends SetupBaseAcitivty {

    private static final String TAG = "Setup2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        getSupportActionBar().hide();



        SettingItem si_setup2activity_bindsim = (SettingItem) findViewById(R.id.si_setup2activity_bindsim);

        si_setup2activity_bindsim.SetMySettingItemOnCheckStateChangeListener(
                new SettingItem.MySettingItemOnCheckStateChangeListener() {
            @Override
            public void onChecked() {
                Log.i(TAG,"oncheck ，bind sim");
                //绑定SIM卡 --》 记录每一个SIM卡的全球唯一标识号码 IMSI
                //    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
                //   Emulator 有一个虚拟的SIM卡号。
                //   I/Setup2Activity: simSerialNumber=89014103211118510720

                //IMEI International Mobile Equipment Identity国际移动设备标识 IMEI由15位数字
                //手机的唯一识别码

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String simSerialNumber = telephonyManager.getSimSerialNumber();
                Log.i(TAG,"simSerialNumber="+simSerialNumber);
                MyApplication.saveStringToSp("imsi",simSerialNumber);

            }

            @Override
            public void onUnChecked() {
                Log.i(TAG,"onUnChecked ，unbind sim");
                //解绑SIM卡
                MyApplication.saveStringToSp("imsi","");

            }
        });
    }


    public void previous(View v){
        startActivity(new Intent(this,Setup1Activity.class));
        //设置两个页面之间的切换动画
        // para1 页面显示的东西
        //     2 页面退出的动画
        overridePendingTransition(R.anim.slidein_left,R.anim.sildeout_rigth );
    }

    public void next(View v){

        //让用户务必绑定sim卡，否则防盗的业务逻辑无法使用
        //判断用户是否绑定了SIm卡，
        // 方法1 ，去问自定义控件SettingItem，
        // 方法2 ，去check sharedpreference ,如果有imsi就说明已经绑定

        String imsi = MyApplication.getStringFromSp("imsi");
        if (imsi.isEmpty()){
            Toast.makeText(Setup2Activity.this, "请先绑定sim卡，否则无法使用本防盗功能", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(this,Setup3Activity.class));
            overridePendingTransition(R.anim.slidein_rigth,R.anim.slideout_left);
        }



    }
}
