package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyAppLockDao;

public class AppLockedActivity extends AppCompatActivity {

    private EditText et_applockactivity_pswd;

    private MyAppLockDao dao;
    private String pkgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_locked);
        et_applockactivity_pswd = (EditText) findViewById(R.id.et_applockactivity_pswd);

        TextView tv_applock_lockedappname = (TextView) findViewById(R.id.tv_applock_lockedappname);

        Intent intent = getIntent();
        pkgName = intent.getStringExtra("pkgName");
        tv_applock_lockedappname.setText(pkgName);

        dao= new MyAppLockDao(this);
    }

    public void unlock(View v){
        String psw = et_applockactivity_pswd.getText().toString();

        if (psw.equals("123")){
            finish();
            //同时把已经解锁的app 从加锁app数据库里删除。 这样就不会反复重新进入加锁页面
            dao.removeLockedApp(pkgName);
        }else {
            Toast.makeText(this,"需要密码才能解锁该app！",Toast.LENGTH_LONG).show();
        }

    }

    //重写下当前Activity的back键的处理方式，让用户点击back之后，直接回到home。
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
