package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;

public class SetLocationActivity extends AppCompatActivity {

    private LinearLayout ll_setlocationactivity_toast;
    private TextView tv_setlocationactivity_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        getSupportActionBar().hide();

        ll_setlocationactivity_toast = (LinearLayout) findViewById(R.id.ll_setlocationactivity_toast);

        tv_setlocationactivity_title = (TextView) findViewById(R.id.tv_setlocationactivity_title);

        //直接在此去设定layout的位置是不行的。
        //ll_setlocationactivity_toast.layout();


        String toastx = MyApplication.getStringFromSp("toastx");
        String toasty = MyApplication.getStringFromSp("toasty");
        if (!toastx.isEmpty()||!toasty.isEmpty()){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_setlocationactivity_toast.getLayoutParams();
            layoutParams.leftMargin=Integer.parseInt(toastx);
            int margintop=Integer.parseInt(toasty);
            layoutParams.topMargin=Integer.parseInt(toasty);
            ll_setlocationactivity_toast.setLayoutParams(layoutParams);
        }




        ll_setlocationactivity_toast.setOnTouchListener(new View.OnTouchListener() {

            float startx;
            float starty;

            float endx;
            float endy;

            int finalx ;
            int finaly ;


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case  MotionEvent.ACTION_UP:

                        Log.i(TAG,"action up");
                        MyApplication.saveStringToSp("toastx",(finalx)+"");
                        MyApplication.saveStringToSp("toasty",(finaly)+"");

                        break;
                    case  MotionEvent.ACTION_DOWN:
                        startx= event.getRawX();
                        starty= event.getRawY();
                        Log.i(TAG,"action down");

                        break;
                    case  MotionEvent.ACTION_MOVE:
                        endx= event.getRawX();
                        endy= event.getRawY();
                        int dx = (int)(endx-startx);
                        int dy= (int) (endy-starty);
                        //之前的位置
                       // ll_setlocationactivity_toast.layout(l,t,r,b);
                        int left = ll_setlocationactivity_toast.getLeft();
                        int top = ll_setlocationactivity_toast.getTop();
                        int right = ll_setlocationactivity_toast.getRight();
                        int bottom = ll_setlocationactivity_toast.getBottom();
                        //新位置
                        ll_setlocationactivity_toast.layout(left+dx,top+dy,right+dx,bottom+dy);

                        //保存当前自己移动Toast的位置
                        finalx=left+dx;
                        finaly=top+dy;

                        startx=endx;
                        starty=endy;
                        Log.i(TAG,"action move");

                        break;
                }

                return false;
            }
        });


        ll_setlocationactivity_toast.setOnClickListener(new View.OnClickListener() {

            boolean isFisrtCilck=true;
            long firstclicktime;
            @Override
            public void onClick(View v) {
                //检测双击事件：
                Log.i(TAG,"onclick");

                //记录第一次点击的时间
                if (isFisrtCilck){
                    firstclicktime = System.currentTimeMillis();
                    isFisrtCilck=false;
                }else {
                    long now = System.currentTimeMillis();
                    if (now-firstclicktime<500){
                        Log.i(TAG,"double click");
                        isFisrtCilck=true;
                        ll_setlocationactivity_toast.layout(200,
                                400,
                                200+ll_setlocationactivity_toast.getWidth(),
                                400+ ll_setlocationactivity_toast.getHeight());

                    }else{
                        firstclicktime = System.currentTimeMillis();
                        isFisrtCilck=false;
                    }

                }


            }
        });




    }
    public static final String TAG ="SetLocationActivity";


   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        //另一种方式:提前给他layout传递一些参数，以便他layout到时候跟剧我们的设置去摆放位置
        String toastx = MyApplication.getStringFromSp("toastx");
        String toasty = MyApplication.getStringFromSp("toasty");
        if (!toastx.isEmpty()||!toasty.isEmpty()){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_setlocationactivity_toast.getLayoutParams();
            layoutParams.leftMargin=Integer.parseInt(toastx);
            int margintop=Integer.parseInt(toasty);
            layoutParams.topMargin=Integer.parseInt(toasty)-tv_setlocationactivity_title.getMeasuredHeight();

            ll_setlocationactivity_toast.setLayoutParams(layoutParams);
        }

        super.onWindowFocusChanged(hasFocus);
    }*/
}
