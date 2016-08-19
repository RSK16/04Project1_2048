package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;

public class PhoneSafeActivity extends AppCompatActivity {

    private static final String TAG = "PhoneSafeActivity";
    private TextView tv_phonesafeactivity_safenum;
    private ImageView iv_phonesafeactivity_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe);
        getSupportActionBar().hide();

        tv_phonesafeactivity_safenum = (TextView) findViewById(R.id.tv_phonesafeactivity_safenum);

        iv_phonesafeactivity_status = (ImageView) findViewById(R.id.iv_phonesafeactivity_status);

        //确认用户是否之前已经有设置过
        //如果没有，让用户先设置
        String imsi = MyApplication.getStringFromSp("imsi");
        if (imsi.isEmpty()){
            startActivity(new Intent(this,Setup1Activity.class));
        }else {
            //如果有，则显示设置完成的页面
            String safenum = MyApplication.getStringFromSp("safenum");
            Log.i(TAG,"safenum="+safenum);

            tv_phonesafeactivity_safenum.setText(safenum);
            boolean phonesafe = MyApplication.getStateFromSp("phonesafe");
            if (phonesafe){
                //表示功能开启
                iv_phonesafeactivity_status.setImageResource(R.drawable.lock);

            }else{
                iv_phonesafeactivity_status.setImageResource(R.drawable.unlock);
            }
        }

    }

    @Override
    protected void onResume() {

        String safenum = MyApplication.getStringFromSp("safenum");
        tv_phonesafeactivity_safenum.setText(safenum);
        Log.i(TAG,"safenum="+safenum);

        super.onResume();
    }

    public void resetup(View v){

        startActivity(new Intent(this,Setup1Activity.class));

    }
}
