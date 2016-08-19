package com.cskaoyan.zhao.a04mobilemanager.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyLocationQueryDao;

import org.w3c.dom.Text;

public class MyShowNumberLocationService extends Service {
    private static final String TAG = "NumberLocationService";

    public MyShowNumberLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        telephonyManager.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);


        Log.i(TAG,"oncreate");

        super.onCreate();
    }

    class MyPhoneStateListener extends PhoneStateListener {

        private WindowManager windowManager;
       // private TextView tv;

        private View view;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state){

                case  TelephonyManager.CALL_STATE_IDLE:

                    if (windowManager!=null){

                        windowManager.removeView(view);
                        view=null;
                    }

                    break;

                case  TelephonyManager.CALL_STATE_OFFHOOK:
                    break;

                case  TelephonyManager.CALL_STATE_RINGING:
                    String s = MyLocationQueryDao.queryLocation(MyShowNumberLocationService.this, incomingNumber);

                    Log.i(TAG,"location="+s);

                    //第一步拿到windowManager
                    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    //可以在当前的Window内添加一个view，显示出来
                    //也可以在当前的window内去删除一个view，使之消失

                    //第二步，去初始化一个我们要显示的view
                   /* tv = new TextView(MyShowNumberLocationService.this);
                    tv.setText(s);
                    tv.setBackgroundColor(Color.RED);*/

                    view = View.inflate(MyShowNumberLocationService.this, R.layout.mytoast, null);

                    view.setBackgroundResource(R.drawable.call_locate_green);
                    TextView tv_toast_message = (TextView) view.findViewById(R.id.tv_toast_message);
                    tv_toast_message.setText( s);

                    //第三步，mParams 搞定
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.format = PixelFormat.TRANSLUCENT;
                    params.windowAnimations = android.R.style.Animation_Toast;
                    params.type = WindowManager.LayoutParams.TYPE_TOAST;
                    params.setTitle("Toast");
                    params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

                    //用来控制我们要显示在window上的view的位置
                    params.gravity = Gravity.LEFT | Gravity.TOP;

                    String toastx = MyApplication.getStringFromSp("toastx");
                    String toasty = MyApplication.getStringFromSp("toasty");

                    params.x=Integer.parseInt(toastx);
                    params.y=Integer.parseInt(toasty);;
                    //最后一步
                    windowManager.addView(view, params);

                    //Toast.makeText(MyShowNumberLocationService.this,s,Toast.LENGTH_LONG).show();

                    break;
            }


            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {

        Log.i(TAG,"onDestroy");

        super.onDestroy();
    }
}
