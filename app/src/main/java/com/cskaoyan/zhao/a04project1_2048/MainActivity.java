package com.cskaoyan.zhao.a04project1_2048;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cskaoyan.zhao.a04project1_2048.view.GameView;
import com.cskaoyan.zhao.a04project1_2048.view.MyTextView;
import com.cskaoyan.zhao.a04project1_2048.view.NumberItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rl_mainactivity_center = (RelativeLayout) findViewById(R.id.rl_mainactivity_center);

        GameView gameView = new GameView(this);

        rl_mainactivity_center.addView(gameView);


      /*  GridLayout gl_mainactivity_container = (GridLayout) findViewById(R.id.gl_mainactivity_container);

        gl_mainactivity_container.setRowCount(4);
        gl_mainactivity_container.setColumnCount(4);

        //通过代码来得到屏幕宽度 480 120
        WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new  DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        int window_width= metrics.widthPixels;

        Log.i(TAG,window_width+"");


        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){

                //封装的思想。
                *//*MyTextView textView = new MyTextView(this);
                textView.setText(4*i+j + "");
                textView.setBackgroundColor(Color.BLUE);
                gl_mainactivity_container.addView(textView,window_width/4,window_width/4 );
                //注意：在android代码中出现的控件不带单位的宽高，默认都是像素单位px*//*

                NumberItem numberItem = new NumberItem(this);

                gl_mainactivity_container.addView(numberItem,window_width/4,window_width/4 );


            }


        }*/
    }
}
