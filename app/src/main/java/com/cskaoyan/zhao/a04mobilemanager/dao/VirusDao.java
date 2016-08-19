package com.cskaoyan.zhao.a04mobilemanager.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zhao on 2016/8/16.
 */
public class VirusDao {

    Context ctx;
    public VirusDao(Context ctx) {

        this.ctx=ctx;
    }

    public   boolean isVirus(String fileMD5){
        boolean b=false;
        String dbPath="data/data/"+ctx.getPackageName()+"/"+"antivirus.db";
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, 0);

        if (sqLiteDatabase!=null){
            Cursor cursor = sqLiteDatabase.rawQuery("select * from datable where md5 = ? ;", new String[]{fileMD5});
            b = cursor.moveToNext();
        }
        return  b;

    }
}
