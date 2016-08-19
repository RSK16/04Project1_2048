package com.cskaoyan.zhao.a04mobilemanager.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by zhao on 2016/8/11.
 */
public abstract class MyAsyncTask {


    public abstract  void runOnSubThread() ;

    public abstract void runAfterOnMainThread() ;

    public void start(){

        new Thread(){
            @Override
            public void run() {
                runOnSubThread();
                myhandler.sendEmptyMessage(0);

            }
        }.start();

    }

    Handler myhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if (msg.what==0){

                runAfterOnMainThread();

            }
            super.handleMessage(msg);
        }
    };


}
