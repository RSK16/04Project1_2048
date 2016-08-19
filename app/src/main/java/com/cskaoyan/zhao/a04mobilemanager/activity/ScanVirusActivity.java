package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.bean.AppInfo;
import com.cskaoyan.zhao.a04mobilemanager.dao.VirusDao;
import com.cskaoyan.zhao.a04mobilemanager.utils.AppManagerUtils;
import com.cskaoyan.zhao.a04mobilemanager.utils.MD5Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScanVirusActivity extends AppCompatActivity {

    private static final String TAG = "ScanVirusActivity";
    private ImageView iv_scanvirus_scan;
    private ProgressBar pb_scanvirus_progress;
    private RotateAnimation rotateAnimation;
    private List<AppInfo> allAppInfo;
    private PackageManager packageManager;
    private VirusDao dao;
    private ListView lv_scanvirus_applist;


    private List<Item> data;
    private MyAdpater adpater;

    class Item{

        String pkgname;
        boolean isVirus;

        public Item(String pkgname, boolean isVirus) {
            this.pkgname = pkgname;
            this.isVirus = isVirus;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_virus);
        getSupportActionBar().hide();

        iv_scanvirus_scan = (ImageView) findViewById(R.id.iv_scanvirus_scan);
        pb_scanvirus_progress = (ProgressBar) findViewById(R.id.pb_scanvirus_progress);

        lv_scanvirus_applist = (ListView) findViewById(R.id.lv_scanvirus_applist);


        data=new ArrayList<>();
        //
        adpater = new MyAdpater();
        lv_scanvirus_applist.setAdapter(adpater);


        packageManager = getPackageManager();

        dao = new VirusDao(this);
        startScanAnim();


        new AsyncTask<Void , Integer,Void>(){

            @Override
            protected void onPreExecute() {


                allAppInfo = AppManagerUtils.getAllAppInfo(ScanVirusActivity.this);
                pb_scanvirus_progress.setMax(allAppInfo.size());
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                int i=0;
                while(i<allAppInfo.size()){


                    AppInfo appInfo = allAppInfo.get(i);
                    //扫一下这个应用
                    //找到这个应用的apk，然后利用apk文件计算该文件的MD5值，最后在数据库里查询，如果查询有值，说明是病毒
                    boolean virus=false;
                    try {
                        ApplicationInfo app = packageManager.getApplicationInfo(appInfo.getPkgName(), 0);
                        String sourceDir = app.sourceDir;
                        Log.i(TAG, appInfo.getPkgName() +"----" +sourceDir);
                        String fileMD5 = MD5Utils.getFileMD5(sourceDir);
                          virus = dao.isVirus(fileMD5);
                        Log.i(TAG,  "fileMD5----" +fileMD5 +"-----"+virus);




                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //把每次新扫描的数据增加到数据源的第一个
                    data.add(0,new Item(appInfo.getPkgName(),virus));
                    publishProgress(++i);

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                //让动画停止
                rotateAnimation.cancel();
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                Integer value = values[0];
                pb_scanvirus_progress.setProgress(value.intValue());

                adpater.notifyDataSetChanged();

                super.onProgressUpdate(values);
            }
        }.execute();


    }

    private void startScanAnim() {
        //让图片开始转动

        rotateAnimation = new
                RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        iv_scanvirus_scan.startAnimation(rotateAnimation);
    }

    class MyAdpater extends BaseAdapter{


        @Override
        public int getCount() {
            return data.size();
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


            Item item = data.get(position);

            TextView textView = new TextView(ScanVirusActivity.this);

            textView.setText(item.pkgname);

            if(item.isVirus){
                textView.setTextColor(Color.RED);
            }

            return textView;
        }
    }
}
