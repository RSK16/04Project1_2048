package com.cskaoyan.zhao.a04project1_2048.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zhao on 2016/8/1.
 */
public class MyTextView extends TextView{

    //在代码中 传入上下午 去new textview
    public MyTextView(Context context) {
        super(context);
    }

    //系统再new textview的时候调用，AttributeSet 属性集合，系统解析xml文件里写的textview的属性
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重新定义一个新的textview，让textview除了显示文字之外，在显示边框
    @Override
    protected void onDraw(Canvas canvas) {

        //可以在显示文字之前，画一个红色边框

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        canvas.drawRect(0,0 ,getMeasuredWidth(),getMeasuredHeight(),paint);


        //系统的TextView 会去做原本textview应该显示的东西
        super.onDraw(canvas);
    }
}
