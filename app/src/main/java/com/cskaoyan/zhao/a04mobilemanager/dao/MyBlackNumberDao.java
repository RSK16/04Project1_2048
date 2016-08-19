package com.cskaoyan.zhao.a04mobilemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cskaoyan.zhao.a04mobilemanager.activity.PhoneManagerActivity;
import com.cskaoyan.zhao.a04mobilemanager.bean.BlackNumberInfo;
import com.cskaoyan.zhao.a04mobilemanager.db.MyBlackNumberDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2016/8/16.
 */
public class MyBlackNumberDao {

    //

    SQLiteDatabase db;



    public MyBlackNumberDao(Context ctx) {
        MyBlackNumberDBHelper helper = new MyBlackNumberDBHelper(ctx,"BlackNum.db",null,1);
        db = helper.getReadableDatabase();
    }

    //insert
    public long insertNumber(String number, int mode){

        ContentValues cv= new ContentValues();
        cv.put("number",number);
        cv.put("mode",mode);

       return db.insert("blacknum",null,cv);

    }

    //delete
    public int  delelteNumber(String number){

        return db.delete("blacknum","number = ?",new String[]{number});

    }

     //update

    //
    public int  updateMode(String number,int mode){

        ContentValues cv=new ContentValues();
        cv.put("mode",mode);

        return db.update("blacknum", cv, "number = ?",new String[]{number});

    }
    //query
    public List<BlackNumberInfo> getAllBlackNumber(){

        List< BlackNumberInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from blacknum;", null);
        while (cursor.moveToNext()){

            String num = cursor.getString(0);
            int mode = cursor.getInt(1);

             BlackNumberInfo info= new BlackNumberInfo();
             info.number = num;
             info.mode=mode;
            list.add(info);
        }


        return  list;
    }


    //query
    public List<BlackNumberInfo> getPartBlackNumber(int limit,int offset){

        List< BlackNumberInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from blacknum limit ? offset ?;", new String[]{limit+"",offset+""});
        while (cursor.moveToNext()){

            String num = cursor.getString(0);
            int mode = cursor.getInt(1);

            BlackNumberInfo info= new BlackNumberInfo();
            info.number = num;
            info.mode=mode;
            list.add(info);
        }


        return  list;
    }

    //提供一个根据电话号码 ，去数据库里查询拦截模式的api


    public int queryMode(String nubmer){

        int ret =-1;
        Cursor cursor = db.rawQuery("select mode from blacknum where number = ? ;", new String[]{nubmer});

        if (cursor.moveToNext()){

            ret = cursor.getInt(0);
        }

        return  ret;
    }

    //提供一个获取数据库里所有数据的条目数的api

    public int getAllNumber(){

        Cursor cursor = db.rawQuery("select count(*) from blacknum  ;",null);
        cursor.moveToNext();
        return  cursor.getInt(0);

    }
}
