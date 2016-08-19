package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyBlackNumberDao;
import com.cskaoyan.zhao.a04mobilemanager.service.MyAppWidgetService;
import com.cskaoyan.zhao.a04mobilemanager.service.MyApplockedService;
import com.cskaoyan.zhao.a04mobilemanager.service.MyBlackNumberService;
import com.cskaoyan.zhao.a04mobilemanager.service.MyShowNumberLocationService;
import com.cskaoyan.zhao.a04mobilemanager.view.SettingItem;

import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG ="SettingActivity" ;
    private TextView tv_setting_updatestatus;
    private MyApplication application;
    private SettingItem si_settingactivity_showlocation;
    private SettingItem si_settingactivity_applock;
    private SettingItem si_settingactivity_blacknumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        startService(new Intent(this, MyAppWidgetService.class));


        RelativeLayout rl_settingactivity_setlocation = (RelativeLayout) findViewById(R.id.rl_settingactivity_setlocation);
        rl_settingactivity_setlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,SetLocationActivity.class));
            }
        });

        initShowLocationSI();
        intiAppLockSI();
        initBlackNumberSI();

    }

    private void initBlackNumberSI() {
        si_settingactivity_blacknumber = (SettingItem) findViewById(R.id.si_settingactivity_blacknumber);
        si_settingactivity_blacknumber.SetMySettingItemOnCheckStateChangeListener(new SettingItem.MySettingItemOnCheckStateChangeListener() {
            @Override
            public void onChecked() {
                startService(new Intent(SettingActivity.this, MyBlackNumberService.class));

            }

            @Override
            public void onUnChecked() {
                stopService(new Intent(SettingActivity.this, MyBlackNumberService.class));

            }
        });
    }

    private void intiAppLockSI() {
        si_settingactivity_applock = (SettingItem) findViewById(R.id.si_settingactivity_applock);
        si_settingactivity_applock.SetMySettingItemOnCheckStateChangeListener(new SettingItem.MySettingItemOnCheckStateChangeListener() {
            @Override
            public void onChecked() {
                startService(new Intent(SettingActivity.this, MyApplockedService.class));

            }

            @Override
            public void onUnChecked() {
                stopService(new Intent(SettingActivity.this, MyApplockedService.class));

            }
        });
    }

    private void initShowLocationSI() {
        si_settingactivity_showlocation = (SettingItem) findViewById(R.id.si_settingactivity_showlocation);
        si_settingactivity_showlocation.SetMySettingItemOnCheckStateChangeListener(new SettingItem.MySettingItemOnCheckStateChangeListener() {
            @Override
            public void onChecked() {
                startService(new Intent(SettingActivity.this, MyShowNumberLocationService.class));
            }

            @Override
            public void onUnChecked() {
                stopService(new Intent(SettingActivity.this, MyShowNumberLocationService.class));

            }
        });
    }

    @Override
    protected void onResume() {
        //检测一下当前的显示归属地的服务是否开启，如果开启，我们才去显示cheked，如果没开启就显示unchec
        //ActivityManager
        boolean isRunning=false;
        boolean isapplockservice_Running=false;

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo info:runningServices) {
            String className = info.service.getClassName();
            if(className.equals("com.cskaoyan.zhao.a04mobilemanager.service.MyShowNumberLocationService")){
                isRunning=true;
            }
            if (className.equals("com.cskaoyan.zhao.a04mobilemanager.service.MyApplockedService")){
                isapplockservice_Running=true;

            }
        }

        // 被关闭 false
        si_settingactivity_showlocation.setCheck(isRunning);
        si_settingactivity_applock.setCheck(isapplockservice_Running);
        super.onResume();
    }
}
