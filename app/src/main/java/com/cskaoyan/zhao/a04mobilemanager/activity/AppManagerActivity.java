package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.bean.AppInfo;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyAppLockDao;
import com.cskaoyan.zhao.a04mobilemanager.utils.AppManagerUtils;
import com.cskaoyan.zhao.a04mobilemanager.utils.MyAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ppManagerActivity";

    List<AppInfo> applist;
    List<AppInfo> UserApplist;
    List<AppInfo> SysApplist;


    private ListView lv_appmanager_applist;
    private ProgressBar pb_appmanager_loading;
    private TextView tv_applist_type;
    private PopupWindow popupWindow;
    private AppInfo clickedApp;
    private MyApplistAdapter myApplistAdapter;

    private MyAppLockDao appLockDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSystemService(Context.ACTIVITY_SERVICE)



        setContentView(R.layout.activity_app_manager);
        getSupportActionBar().hide();
        appLockDao = new MyAppLockDao(this);

        TextView tv_appmanageractivity_romsize = (TextView) findViewById(R.id.tv_appmanageractivity_romsize);
        TextView tv_appmanageractivity_sdcardsize = (TextView) findViewById(R.id.tv_appmanageractivity_sdcardsize);

        pb_appmanager_loading = (ProgressBar) findViewById(R.id.pb_appmanager_loading);
        lv_appmanager_applist = (ListView) findViewById(R.id.lv_appmanager_applist);
        tv_applist_type = (TextView) findViewById(R.id.tv_applist_type);

        //计算rom 和sdcard 剩余空间
        //

        long rom_availalesize = AppManagerUtils.getRomAvailableSize();
        long sdcard_availalesize = AppManagerUtils.getSDcardAvailableSize();

        String rom = Formatter.formatFileSize(this, rom_availalesize);
        String sdcard = Formatter.formatFileSize(this, sdcard_availalesize);

        tv_appmanageractivity_romsize.setText("手机的ROM剩余空间:\n"+rom);
        tv_appmanageractivity_sdcardsize.setText("手机的SDcard剩余空间:\n"+sdcard);


        getListData();


        //ListView的单击事件
        lv_appmanager_applist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG,"onclick");
                //方法3 系统有提供一个PopupWindow

                //点击的时候不需要直接去new，如果之前有的话，把之前的dismiis

                if (position==0||position==UserApplist.size()+1){
                    return;
                }

                if (position-1<UserApplist.size()){
                    clickedApp = UserApplist.get(position-1);
                }else {
                    clickedApp = SysApplist.get(position-2-UserApplist.size());
                }

                if (popupWindow!=null){

                    popupWindow.dismiss();


                }else{

                    popupWindow = new PopupWindow();

                    //显示一个Textview
                    /*TextView tv=new TextView(AppManagerActivity.this);
                    tv.setText("hello popup");
                    tv.setBackgroundColor(Color.RED);*/
                    View popup_view = View.inflate(AppManagerActivity.this, R.layout.item_popup, null);

                    LinearLayout ll_popwindow_uninstall = (LinearLayout) popup_view.findViewById(R.id.ll_popwindow_uninstall);
                    LinearLayout ll_popwindow_launch = (LinearLayout) popup_view.findViewById(R.id.ll_popwindow_launch);
                    LinearLayout ll_popwindow_share = (LinearLayout) popup_view.findViewById(R.id.ll_popwindow_share);

                    ll_popwindow_uninstall.setOnClickListener( AppManagerActivity.this);
                    ll_popwindow_launch.setOnClickListener( AppManagerActivity.this);
                    ll_popwindow_share.setOnClickListener( AppManagerActivity.this);

                    popupWindow.setContentView(popup_view);
                    //Popupwindow必须要设置宽高
                    popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
                    popupWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);

                }



                //拿到当前的item的位置
                int[] location =new int[2];
                view.getLocationInWindow(location);

                Log.i(TAG,"x="+location[0]+"y="+location[1]);

                //第三步去
                popupWindow.showAtLocation(view, Gravity.LEFT|Gravity.TOP,100,location[1]);
            }
        });

        //listView的长安事件
        lv_appmanager_applist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position==0||position==UserApplist.size()+1){
                    return true;
                }

                ImageView iv_applist_lock = (ImageView) view.findViewById(R.id.iv_applist_lock);


                if (position-1<UserApplist.size()){
                    clickedApp = UserApplist.get(position-1);
                }else {
                    clickedApp = SysApplist.get(position-2-UserApplist.size());
                }

                Log.i(TAG,"onlongclicked -"+clickedApp.getAppName());
                //先判断下该应用是否有加锁，

                if (appLockDao.queryAppIsLocked(clickedApp.getPkgName())){
                 //有的话 去解锁，
                    appLockDao.removeLockedApp(clickedApp.getPkgName());
                    iv_applist_lock.setImageResource(R.drawable.unlock);
                }else{//没有的话 去加锁
                    //增加到数据库(加锁)
                    appLockDao.addLockedApp(clickedApp.getPkgName());
                    iv_applist_lock.setImageResource(R.drawable.lock);
                }

                return true;
            }
        });


    }

    private void getListData() {

        //解法3
        //AsyncTask 的时候，需要注意几个问题
        // 1 。三个泛型的参数代表什么意义
        // 第一个参数，代表doInBackground方法需要接受的参数。同时也是execute方法需要接受的参数类型。
        // 第三个参数，代表代表doInBackground方法需要返回的参数类型，同时AsyncTask会把这个返回值给到onPostExecute
        // 第二个参数，代表doInBackground方法内部如果需要去更新UI的的话，可以调用publishProgress去更新UI
        //  系统会在主线程内调用   onProgressUpdate，同时会把float类型的参数，传递给主线程的  onProgressUpdate方法使用

        // 2 .这里面有几个callback，他们的执行流程是怎么样的。
        // 5个callback，需要知道每个callback的意义及 什么时候执行。
        try {
            new   AsyncTask<URL,Float,String>(){

                //在启动子线程之前可以去做一些初始化工作。
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                //子线程执行
                @Override
                protected String doInBackground(URL... params) {

                     //Log.i(TAG,param.intValue()+"");
                    applist = AppManagerUtils.getAllAppInfo(AppManagerActivity.this);
                    UserApplist=new ArrayList<AppInfo>();
                    SysApplist= new ArrayList<AppInfo>();

                    for (AppInfo info:  applist) {
                        if (info.isSystemApp()){
                            SysApplist.add(info) ;
                        }
                        else{
                            UserApplist.add(info);
                        }
                    }


                    publishProgress(100f);
                    return "helloworld";
                }

                //当子线程执行完毕的时候，在主线程执行
                @Override
                protected void onPostExecute(final String s) {

                    Log.i(TAG,s);
                    pb_appmanager_loading.setVisibility(View.GONE);
                    if (myApplistAdapter==null){
                        myApplistAdapter = new MyApplistAdapter();
                        lv_appmanager_applist.setAdapter(myApplistAdapter);
                    }else {
                        //notifyDataSetChanged(，不需要重创建adpater,只会自动的使用新的数据集去刷新listview)
                        myApplistAdapter.notifyDataSetChanged();
                    }


                    lv_appmanager_applist.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            switch (scrollState){
                                case  SCROLL_STATE_IDLE:
                                    break;

                                case SCROLL_STATE_TOUCH_SCROLL:
                                    //让popup消失
                                    if (popupWindow!=null){
                                        popupWindow.dismiss();
                                     }
                                    break;

                                case SCROLL_STATE_FLING:
                                    break;


                            }


                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                            if(firstVisibleItem>UserApplist.size()+1){
                                tv_applist_type.setText("系统应用："+SysApplist.size());
                            }else{

                                tv_applist_type.setText("用户应用："+UserApplist.size());

                            }


                        }
                    });

                    super.onPostExecute(s);
                }

                //在这里可以去更新UI
                @Override
                protected void onProgressUpdate(Float... values) {

                    super.onProgressUpdate(values);
                }
            }.execute(new URL("http://ww.baidu.com"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_popwindow_uninstall:
                //Toast.makeText(AppManagerActivity.this, "uninstall", Toast.LENGTH_SHORT).show();
                uninstall();
                break;
            case R.id.ll_popwindow_launch:
                //Toast.makeText(AppManagerActivity.this, "lunach", Toast.LENGTH_SHORT).show();
                launch();
                break;
            case R.id.ll_popwindow_share:
                //Toast.makeText(AppManagerActivity.this, "share", Toast.LENGTH_SHORT).show();
                share();
                break;


        }

    }

    private void uninstall() {

        String pkgName = clickedApp.getPkgName();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:"+pkgName));
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG,"resultCode="+resultCode);

        //RESULT_CANCELED;
       /* if (requestCode==100&&resultCode==RESULT_OK){
            Log.i(TAG,"need refresh list");
        }else{
            Log.i(TAG,"do not need refresh list");
        }*/

        if (requestCode==100){
            //刷新一下当前的applist
            if (popupWindow!=null){
                popupWindow.dismiss();
            }
            getListData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void launch() {

        String pkgName = clickedApp.getPkgName();
        //
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(pkgName);
        startActivity(launchIntentForPackage);

    }
    private void share() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"分享一个比较好玩的应用给你"+clickedApp.getAppName());
        startActivity(intent);

    }

    class MyApplistAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return applist.size();
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


            //AppInfo appInfo = applist.get(position);

            AppInfo appInfo=null;
            //
            if (position==0){

                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("用户应用："+UserApplist.size());
                tv.setTextColor(Color.RED);
                return  tv;
            }else if(position==(UserApplist.size()+1)){
                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("系统应用："+SysApplist.size());
                tv.setTextColor(Color.RED);
                return  tv;
            }

            if (position-1<UserApplist.size()){
                appInfo= UserApplist.get(position-1);
            }else {
                appInfo= SysApplist.get(position-2-UserApplist.size());
            }

            //返回给系统，让系统显示在当前的position内
            View view=null;



            Holder holder=null;



            //listview的复用 如果listview里有几个不同类型的view，在复用的时候就要小心。
            if (convertView!=null&&  convertView instanceof RelativeLayout){
                 view =convertView;
                 holder = (Holder) convertView.getTag();
            }else{
                view = View.inflate(AppManagerActivity.this, R.layout.item_applist, null);
                ImageView iv_applist_icon = (ImageView) view.findViewById(R.id.iv_applist_icon);
                ImageView iv_applist_lock = (ImageView) view.findViewById(R.id.iv_applist_lock);
                TextView tv_applist_appname = (TextView) view.findViewById(R.id.tv_applist_appname);
                TextView tv_applist_applocation = (TextView) view.findViewById(R.id.tv_applist_applocation);
                holder =new Holder(iv_applist_icon,iv_applist_lock,tv_applist_appname,tv_applist_applocation);
                view.setTag(holder);
            }


            //根据appinfo给对应item里的控件赋值
            holder.iv_applist_icon.setImageDrawable(appInfo.getIcon());
            if (appInfo.isLocked()){
                holder.iv_applist_lock.setImageResource(R.drawable.lock);
            }
            if (appInfo.isSdcard())
                holder.tv_applist_applocation.setText("安装在SDcard上");


            holder.tv_applist_appname.setText(appInfo.getAppName());

            return view;
        }

        class Holder{

             public      ImageView iv_applist_icon ;
             public      ImageView iv_applist_lock ;
             public      TextView tv_applist_appname ;
             public      TextView tv_applist_applocation ;

            public Holder(ImageView iv_applist_icon, ImageView iv_applist_lock, TextView tv_applist_appname, TextView tv_applist_applocation) {
                this.iv_applist_icon = iv_applist_icon;
                this.iv_applist_lock = iv_applist_lock;
                this.tv_applist_appname = tv_applist_appname;
                this.tv_applist_applocation = tv_applist_applocation;
            }
        }
    }

}
