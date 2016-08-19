package com.cskaoyan.zhao.a04project1_2048.view;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by zhao on 2016/8/1.
 */
public class NumberItem extends FrameLayout {

    private  TextView tv_numberItem;
    private  int number;

    public NumberItem(Context context) {
        super(context);

        init(context);

    }

    private void init(Context context) {
        tv_numberItem = new TextView(context);
        //match wrap_content
        tv_numberItem.setText("");

        tv_numberItem.setGravity(Gravity.CENTER);
        tv_numberItem.setTextSize(20);

        setNumber(0);
        tv_numberItem.setBackgroundColor(Color.GRAY);

        //给每个textView设置一个margin
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5,5,5,5);

        //要把textview 增加到FrameLayout，所以必须给一个FrameLayout 的layoutparameter
        addView(tv_numberItem,params);
    }

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNumber(int number){

        this.number=number;

        if (number==2){
            setText("2");
            tv_numberItem.setBackgroundColor(Color.BLUE);
        }
    }

    public void setText(String numberString){

        tv_numberItem.setText(numberString);
    }

    public int getNumber(){

        return number;
    }
}
