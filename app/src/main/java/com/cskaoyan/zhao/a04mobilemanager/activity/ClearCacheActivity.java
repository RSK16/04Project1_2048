package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.bean.AppInfo;
import com.cskaoyan.zhao.a04mobilemanager.utils.AppManagerUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClearCacheActivity extends AppCompatActivity {

    private static final String TAG = "ClearCacheActivity";
    private ProgressBar pb_clearcacheactivity_scan;
    private TextView tv_clearcacheactivity_appanme;
    private PackageManager packageManager;
    private ListView lv_clearcacheractivity_cachelist;
    private Button bt_clearcache_clearall;

    class CacheINfo{

        Drawable icon;
        String pkgname;
        String cacheSize;

        public CacheINfo(Drawable icon, String pkgname, String cacheSize) {
            this.icon = icon;
            this.pkgname = pkgname;
            this.cacheSize = cacheSize;
        }
    }

    ArrayList<CacheINfo>  cachelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);
        getSupportActionBar().hide();
        cachelist= new ArrayList<CacheINfo>();
        pb_clearcacheactivity_scan = (ProgressBar) findViewById(R.id.pb_clearcacheactivity_scan);
        tv_clearcacheactivity_appanme = (TextView) findViewById(R.id.tv_clearcacheactivity_appanme);

        bt_clearcache_clearall = (Button) findViewById(R.id.bt_clearcache_clearall);

        lv_clearcacheractivity_cachelist = (ListView) findViewById(R.id.lv_clearcacheractivity_cachelist);

        packageManager = getPackageManager();


        new AsyncTask<Void,Integer,Void>(){

            private List<AppInfo> allAppInfo;

            @Override
            protected void onPreExecute() {

                allAppInfo = AppManagerUtils.getAllAppInfo(ClearCacheActivity.this);
                pb_clearcacheactivity_scan.setMax(allAppInfo.size());
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                for (int i=0;i<=allAppInfo.size();i++){

                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //如何获取每个应用的缓存大小
                    if (i!=allAppInfo.size()) {
                        AppInfo appInfo = allAppInfo.get(i);


                        // mPm.getPackageSizeInfo(packageName, mBackgroundHandler.mStatsObserver);
                        //使用反射来调用
                        // packageManager.getPackageSizeInfo(packageName, mBackgroundHandler.mStatsObserver)

                        Class<PackageManager> packageManagerClass = PackageManager.class;
                        try {
                            Method getPackageSizeInfo = packageManagerClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

                            getPackageSizeInfo.invoke(packageManager,appInfo.getPkgName(),new MyIPackageStatsObserver());

                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }
                    publishProgress(i);
                }



                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                lv_clearcacheractivity_cachelist.setAdapter(new MyAdapter());
                bt_clearcache_clearall.setVisibility(View.VISIBLE);

                super.onPostExecute(aVoid);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {

                Integer value = values[0];
                pb_clearcacheactivity_scan.setProgress(value.intValue());
                pb_clearcacheactivity_scan.setSecondaryProgress(value.intValue()*2);

                if (value!=allAppInfo.size()){
                    AppInfo appInfo = allAppInfo.get(value);
                    tv_clearcacheactivity_appanme.setText("当前正在扫描的应用："+appInfo.getAppName());
                }else{
                    tv_clearcacheactivity_appanme.setText("扫描完成");

                }

                super.onProgressUpdate(values);
            }
        }.execute();


    }


    //清楚所有的缓存 去申请一个很大的空间。让系统自己去把所有应用的缓存清理掉
    public  void clearall(View v){

        //    public abstract void freeStorageAndNotify(long freeStorageSize, IPackageDataObserver observer);

        Class<PackageManager> packageManagerClass = PackageManager.class;
        try {
            Method freeStorageAndNotify = packageManagerClass.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);

           freeStorageAndNotify.invoke(packageManager,Long.MAX_VALUE,new MyIPackageDataObserver());


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return cachelist.size();
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


            View view = View.inflate(ClearCacheActivity.this, R.layout.item_cachelist, null);

            ImageView iv_processlist_icon = (ImageView) view.findViewById(R.id.iv_itemcachelist_appcion);
            TextView tv_itemcachelist_apppackage = (TextView) view.findViewById(R.id.tv_itemcachelist_apppackage);
            TextView tv_itemcachelist_appcachesize =  (TextView)view.findViewById(R.id.tv_itemcachelist_appcachesize);

            CacheINfo cacheINfo = cachelist.get(position);
            iv_processlist_icon.setImageDrawable(cacheINfo.icon);
            tv_itemcachelist_apppackage.setText(cacheINfo.pkgname);
            tv_itemcachelist_appcachesize.setText(cacheINfo.cacheSize);

            return view;
        }
    }

    class MyIPackageStatsObserver extends     IPackageStatsObserver.Stub{


       //GetStatsCompleted ,当缓存数据计算完毕之后，会调用到这个函数，把计算的缓存结果传递进来
       @Override
       public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
           
           if (succeeded){
               long cacheSize = pStats.cacheSize;
               String packageName = pStats.packageName;
               Log.i(TAG,packageName +"--" +cacheSize);

               if (cacheSize>12288){
                   String s = Formatter.formatFileSize(ClearCacheActivity.this, cacheSize);

                   try {
                       ApplicationInfo appinfo = packageManager.getApplicationInfo(packageName, 0);
                       Drawable icon = appinfo.loadIcon(packageManager);

                       CacheINfo cacheINfo = new CacheINfo(icon,packageName, s);
                       cachelist.add(cacheINfo);


                   } catch (PackageManager.NameNotFoundException e) {
                       e.printStackTrace();
                   }



               }
           }
           

       }
   }
    class MyIPackageDataObserver extends  IPackageDataObserver.Stub{

        //所有缓存都清空之后，会回调。
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

              //  Log.i(TAG,packageName);


            cachelist.clear();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lv_clearcacheractivity_cachelist.setAdapter(new MyAdapter());
                    Toast.makeText(ClearCacheActivity.this,"缓存清理完毕",Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
