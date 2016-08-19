package com.cskaoyan.zhao.a04project1_2048.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2016/8/1.
 */
public class GameView extends GridLayout {
    private static final String TAG = "GameView";
    public GameView(Context context) {
        super(context);
        init(context);
    }
    private  int row_number =4;
    private int column_number=4;

    //记录当前棋盘上，空白位置的一个数组。

    //放一个 （x,y） 表示空白的棋盘的位置 行 和 列 。
    List<Point> blankItemList;

    NumberItem[][] itemMatrix;



    public void set_rowNUmber(int row){
        this.row_number =row;
    }

    public void set_columnNUmber(int column){
        this.column_number =column;
    }

    private void init(Context ctx ) {


        setColumnCount(column_number);
        setRowCount(row_number);

        itemMatrix= new NumberItem[row_number][column_number];
        blankItemList =new ArrayList<Point>();


        //通过代码来得到屏幕宽度 480 120
        WindowManager windowManager = (WindowManager) ctx.getSystemService(ctx.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int window_width = metrics.widthPixels;


        for (int i = 0; i < row_number; i++) {
            for (int j = 0; j < column_number; j++) {
                NumberItem numberItem = new NumberItem(ctx);

                itemMatrix[i][j]=numberItem;

                //初始化 blanklist
                Point p= new Point();
                p.x=i;
                p.y=j;
                blankItemList.add(p);

                 addView(numberItem, window_width / 4, window_width / 4);
            }

        }

        //随机找个位置，去显示一个不为0的数字
        addRandomNumber();
        addRandomNumber();

    }

    private void addRandomNumber() {

        //随机产生一个i  随机产生一个j  i 0-3 j 0-3
        //要求是在棋盘上，没有被占用的地方随机找一个位置。
        int blankLength =blankItemList.size();//16
        int random_location = (int)  Math.floor(Math.random()*16) ;
        Log.i(TAG,"random_location="+random_location );
        Point point = blankItemList.get(random_location);
       /* point.x
          point.y*/
        Log.i(TAG,"x="+point.x+"y="+point.y);
        NumberItem numberItem = itemMatrix[point.x][point.y];
        numberItem.setNumber(2);


    }

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

}
