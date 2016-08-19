package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.app.ActivityManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.bean.ProcessInfo;
import com.cskaoyan.zhao.a04mobilemanager.utils.ProcessManagerUtils;

import java.util.ArrayList;
import java.util.List;

public class ProcessManagerActivity extends AppCompatActivity {

    private static final String TAG = "ProcessManagerActivity";
    private List<ProcessInfo> allProcessInfo;
    private List<ProcessInfo> userProcessInfo;

    private ListView lv_processmanager_plist;
    private MyAdater myAdater;
    private int processNumber;

    private TextView tv_processactivity_pnumber;
    private TextView tv_processactivity_ram;
    private String availRamS;
    private String totalRamS;
    private long availRam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        getSupportActionBar().hide();

        tv_processactivity_pnumber = (TextView) findViewById(R.id.tv_processactivity_pnumber);
        tv_processactivity_ram = (TextView) findViewById(R.id.tv_processactivity_ram);

        processNumber = ProcessManagerUtils.getProcessNumber(this);
        long totalRam = ProcessManagerUtils.getTotalRam(this);
        availRam = ProcessManagerUtils.getAvailRam(this);

        totalRamS = Formatter.formatFileSize(this, totalRam);
        availRamS = Formatter.formatFileSize(this, availRam);

        tv_processactivity_pnumber.setText("当前手机的的进程数是\n"+ processNumber);
        tv_processactivity_ram.setText("可用RAM/总RAM是\n"+ availRamS +"/"+ totalRamS);

        //
        lv_processmanager_plist = (ListView) findViewById(R.id.lv_processmanager_plist);

        allProcessInfo = ProcessManagerUtils.getAllProcessInfo(this);
        userProcessInfo= new ArrayList<>();

        for (ProcessInfo p:allProcessInfo
             ) {
            if (!p.isSystem()){
                userProcessInfo.add(p);
            }
        }




                myAdater = new MyAdater();
        lv_processmanager_plist.setAdapter(myAdater);
        
        lv_processmanager_plist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ProcessManagerActivity.this,"click",Toast.LENGTH_SHORT).show();
                Log.i(TAG,"onclick");

                //

                ProcessInfo processInfo = allProcessInfo.get(position);
                CheckBox cb_settting_checkbox = (CheckBox) view.findViewById(R.id.cb_processinfo_clean);

                if (processInfo.isCheck()){
                    cb_settting_checkbox.setChecked(false);
                    processInfo.setCheck(false);

                }else{
                    cb_settting_checkbox.setChecked(true);
                    processInfo.setCheck(true);

                }


            }
        });



    }


    boolean showUser=false;
    class MyAdater extends BaseAdapter{


        @Override
        public int getCount() {

            if (showUser)
                return userProcessInfo.size();
            else
                return allProcessInfo.size();
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

            ProcessInfo processInfo=null;
            if (showUser)
                processInfo = userProcessInfo.get(position);
            else
                processInfo = allProcessInfo.get(position);

            View view = View.inflate(ProcessManagerActivity.this, R.layout.item_processinfo, null);

            ImageView icon = (ImageView) view.findViewById(R.id.iv_processlist_icon);
            TextView tv_processinfo_ram = (TextView) view.findViewById(R.id.tv_processinfo_ram);
            TextView tv_processinfo_appname = (TextView) view.findViewById(R.id.tv_processinfo_appname);
            CheckBox cb_processinfo_clean = (CheckBox) view.findViewById(R.id.cb_processinfo_clean);
            Drawable drawable=processInfo.getIcon();
            if ((drawable)!=null)
                icon.setImageDrawable(drawable);
            tv_processinfo_appname.setText(processInfo.getAppname());
            tv_processinfo_ram.setText(processInfo.getAppRam()/1024 + "KB");
            cb_processinfo_clean.setChecked(processInfo.isCheck());

            return view;
        }
    }

    public void selectAll(View v){

        for (ProcessInfo p:allProcessInfo) {
            p.setCheck(true);
        }

        //方法1
      //  lv_processmanager_plist.setAdapter(new MyAdater());

        //方法2
        //根据新的数据集刷新listview，
        myAdater.notifyDataSetChanged();

    }


    public void unselectAll(View v){
        for (ProcessInfo p:allProcessInfo) {
            p.setCheck(false);
        }
        //方法1
        //  lv_processmanager_plist.setAdapter(new MyAdater());
        //方法2
        //根据新的数据集刷新listview，
        myAdater.notifyDataSetChanged();
    }
    public void showUser(View v){

        if (showUser){
            showUser=false;
        }else{
            showUser=true;
        }

        myAdater.notifyDataSetChanged();


    }

    //清理用户选中的进程
    public void clean(View v){


        ActivityManager ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ProcessInfo> deleteProc = new ArrayList<ProcessInfo>();

        for (ProcessInfo p:allProcessInfo) {
            boolean check = p.isCheck();
            if (check&&!p.getPkgNmame().equals(getPackageName())){
                //killBackgroundProcesses 只能去清理后台进程以及空进程
                // 前台进程 可见进程 服务进程 后台进程 空进程
                ams.killBackgroundProcesses(p.getPkgNmame());
                //      Caused by: java.util.ConcurrentModificationException
                deleteProc.add(p);


            }
        }


        long releaseRam=0;
        for (ProcessInfo p:deleteProc
             ) {
            allProcessInfo.remove(p);
            processNumber--;
            releaseRam +=p.getAppRam();

        }
        long newavailRam = availRam - releaseRam * 1024;
        String newavailRamS = Formatter.formatFileSize(this, newavailRam);


        tv_processactivity_pnumber.setText("当前手机的的进程数是\n"+ processNumber);
        tv_processactivity_ram.setText("可用RAM/总RAM是\n"+newavailRamS+"/"+totalRamS);

        myAdater.notifyDataSetChanged();


    }
}
