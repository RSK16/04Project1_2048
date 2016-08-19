package com.cskaoyan.zhao.a04mobilemanager.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by zhao on 2016/8/9.
 */
public class MyLocationQueryDao {

    public static String queryLocation(Context ctx, String number){

        File dbfile = new File("data/data/"+ctx.getPackageName()+"/telocation.db");
        SQLiteDatabase db =SQLiteDatabase.openOrCreateDatabase(dbfile,null);

        //方法1
        /*if (number.length()<11){
            return "";
        }*/
        //方法2
        //正则表达式(电话号码)

        if (number.matches("1(3[0-9]|147|15\\d|17[01]|18[0-35-9])\\d{8}"))
        {

            String substring = number.substring(0, 7);

            Cursor cursor = db.rawQuery("select location from mob_location where _id = ?", new String[]{substring});

            cursor.moveToNext();

            String location = cursor.getString(0);


            return location;

        }
        return "";
    }
}
