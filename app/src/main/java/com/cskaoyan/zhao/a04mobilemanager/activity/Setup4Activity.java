package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.receiver.MyDeviceAdminReceiver;

public class Setup4Activity extends SetupBaseAcitivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        getSupportActionBar().hide();

    }

    public void previous(View v){

        startActivity(new Intent(Setup4Activity.this,Setup3Activity.class));
        overridePendingTransition(R.anim.slidein_left,R.anim.sildeout_rigth );

    }

    public void next(View v){


        boolean phonesafe = MyApplication.getStateFromSp("phonesafe");
        if (!phonesafe){
           // Toast.makeText(Setup4Activity.this, "防盗功能未开启", Toast.LENGTH_SHORT).show();
        
            new AlertDialog.Builder(this)
                    .setTitle("注意")
                    .setMessage("防盗功能未开启,确定不开启吗？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Setup4Activity.this,PhoneSafeActivity.class));
                        }
                    })
                    .setNegativeButton("否",null)
                    .show();
            
        }else {

            startActivity(new Intent(Setup4Activity.this,PhoneSafeActivity.class));
            overridePendingTransition(R.anim.slidein_rigth,R.anim.slideout_left);

        }
    }


    public void active (View v){

        //发送广播，让系统弹出类似的页面，让用户去在我们的应用里激活
        ComponentName mDeviceAdminSample = new ComponentName(this, MyDeviceAdminReceiver.class);
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "hello my admin");
        startActivityForResult(intent, 100);

    }
}
