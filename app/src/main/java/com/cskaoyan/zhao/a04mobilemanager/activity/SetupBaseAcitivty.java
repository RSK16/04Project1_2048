package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhao on 2016/8/9.
 */
public abstract class SetupBaseAcitivty extends AppCompatActivity {

    private static final String TAG = "SetupBaseAcitivty";
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        gestureDetector = new GestureDetector(this,new MyOnGestureDetector());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

        gestureDetector = new GestureDetector(this,new MyOnGestureDetector());

        super.onCreate(savedInstanceState, persistentState);
    }

    class MyOnGestureDetector extends GestureDetector.SimpleOnGestureListener{

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i(TAG,"onLongPress");
            super.onLongPress(e);
        }
        //手指在屏幕上滑动的时候调用
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG,"onScroll");
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        //手指在屏幕上（快速）滑动的时候调用
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i(TAG,"onFling");

            float startx = e1.getX();
            float endx = e2.getX();

            //表面在往右滑，显示前一个页面
            if (endx-startx>200){
                previous(null);
            }else if (startx-endx>200){
                next(null);
            }


            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    //2048的时候有做过一种 判断用户滑动方向的功能。
    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    public abstract void previous(View v);
    public abstract void next(View v);

}
