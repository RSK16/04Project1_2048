package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.utils.MD5Utils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    int[] images_resouce_id ={R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
            R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
            R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
    };

    String[] titles={

            "手机防盗","通信卫士","手机管理",
            "进程管理","流量统计","手机杀毒",
            "缓存管理","高级工具","应用设置"

    };
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
        TextView tv_mainactivity_welcome = (TextView) findViewById(R.id.tv_mainactivity_welcome);
        tv_mainactivity_welcome.setSelected(true);

        GridView gv_mainactivity_nine = (GridView) findViewById(R.id.gv_mainactivity_nine);

        //ListAdapter adapter;
        gv_mainactivity_nine.setAdapter(new MyAdapter());

        gv_mainactivity_nine.setOnItemClickListener(this);

        application = (MyApplication) getApplication();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
        case 0:

            //进入手机防盗设置向导
            //如果用户第一次使用该功能，则提示用户设置一个密码
            // AlertDialog 里面有一些默认样式的Dialog，信息提示 单选，多选。。
            // 其他一些样式的dialog，系统提供了对应的方法，可以人angry用户自己去设置样式。
            if(application.getStringFromSp("phonesafepwd").isEmpty()){
                showSetupPwdDialog();
            }
            else {
                showInputPwdDialog();
            }

            break;
        case 1:
            startActivity(new Intent(this,PhoneManagerActivity.class));
            break;


        case 2:

            startActivity(new Intent(this,AppManagerActivity.class));

            break;
        case 3:
            startActivity(new Intent(this,ProcessManagerActivity.class));
            break;
        case 4:
            startActivity(new Intent(this,DataUsageActivity.class));

            break;
        case 5:
            startActivity(new Intent(this,ScanVirusActivity.class));
            break;
        case 6:
            startActivity(new Intent(this,ClearCacheActivity.class));
            break;
        case 7:

            startActivity(new Intent(this,AdvanceToolActivity.class));
            break;
        case 8:
            //Toast.makeText(this,"setting",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,SettingActivity.class));
            break;

    }




    }

    private void showInputPwdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //通过用户指定的view 去设定 dialog显示的内容。
        final View dialogView = View.inflate(this, R.layout.dialog_inputpwd, null);

        final EditText et_dialoginputpwd_input = (EditText) dialogView.findViewById(R.id.et_dialoginputpwd_input);
        Button bt_dialoginputpwd_confirm = (Button) dialogView.findViewById(R.id.bt_dialoginputpwd_confirm);
        Button bt_dialoginputpwd_cancle = (Button) dialogView.findViewById(R.id.bt_dialoginputpwd_cancle);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        bt_dialoginputpwd_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //要跟sp里保存的密码去对比，如果一样的话，就应用要进入到防盗设置页面
                String inputPwd = et_dialoginputpwd_input.getText().toString();


                if(!inputPwd.isEmpty()){
                    String phonesafepwd = application.getStringFromSp("phonesafepwd");

                    String md5 = MD5Utils.getMD5(inputPwd);
                    if(md5.equals(phonesafepwd)) {
                        alertDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, PhoneSafeActivity.class));
                    }
                    else
                        Toast.makeText(MainActivity.this, "密码不正确，请重新输入！", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "密码不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_dialoginputpwd_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //显示设置pwd的dialog对话框
    private void showSetupPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //通过用户指定的view 去设定 dialog显示的内容。
        View dialogView = View.inflate(this, R.layout.dialog_setpwd, null);

        final EditText et_dialogsetpwd_set1 = (EditText) dialogView.findViewById(R.id.et_dialogsetpwd_set1);
        final EditText et_dialogsetpwd_set2 = (EditText) dialogView.findViewById(R.id.et_dialogsetpwd_set2);
        Button bt_dialogsetpwd_confirm = (Button) dialogView.findViewById(R.id.bt_dialogsetpwd_confirm);
        Button bt_dialogsetpwd_cancle = (Button) dialogView.findViewById(R.id.bt_dialogsetpwd_cancle);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        bt_dialogsetpwd_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //edittext没有输入的情况下，获取到的字符串是“”
                String s = et_dialogsetpwd_set1.getText().toString();
                String s1 = et_dialogsetpwd_set2.getText().toString();
                //判断两次输入的用户名和密码是否一致
                if (!s.isEmpty()&&!s1.isEmpty()){
                    if (s.equals(s1)){//一致，则保持起来
                        application.saveStringToSp("phonesafepwd",MD5Utils.getMD5(s));
                        alertDialog.dismiss();
                    }else{//不一致，则
                        Toast.makeText(MainActivity.this, "两次密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "密码不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_dialogsetpwd_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            /* TextView tv= new TextView(MainActivity.this);

            tv.setText(position+"");*/

            //这里也可以优化。
            // 1 复用convertView
            // 2 viewHolder

            View view = View.inflate(MainActivity.this, R.layout.item_gridview, null);

            ImageView iv_gridview_icon = (ImageView) view.findViewById(R.id.iv_gridview_icon);
            TextView tv_gridview_name = (TextView) view.findViewById(R.id.tv_gridview_name);


            iv_gridview_icon.setImageResource(images_resouce_id[position]);
            tv_gridview_name.setText(titles[position]);


            return view;
        }
    }
}
