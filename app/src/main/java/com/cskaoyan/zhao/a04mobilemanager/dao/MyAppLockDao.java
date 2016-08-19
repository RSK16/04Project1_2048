package com.cskaoyan.zhao.a04mobilemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cskaoyan.zhao.a04mobilemanager.db.MyApplockDBHelper;

/**
 * Created by zhao on 2016/8/12.
 */
public class MyAppLockDao {

    Context ctx;
    private final SQLiteDatabase readableDatabase;

    public MyAppLockDao(Context ctx) {
        this.ctx=ctx;
        MyApplockDBHelper dbHelper = new MyApplockDBHelper(ctx, "applock.db", null, 1);
        readableDatabase = dbHelper.getReadableDatabase();
    }

    //
    public long addLockedApp(String pkgName){

        //readableDatabase.execSQL("insert into lockedapp values (?,?)",new String[]{1+"",pkgName});
        ContentValues cv= new ContentValues();
        cv.put("pkgname",pkgName);
        long lockedapp = readableDatabase.insert("lockedapp", null, cv);
        return lockedapp;
    }

    //
    public int removeLockedApp(String pkgName){

        int lockedapp = readableDatabase.delete("lockedapp", "pkgName = ?", new String[]{pkgName});
        return  lockedapp;
    }


    public boolean queryAppIsLocked(String pkgName){

        //int lockedapp = readableDatabase.delete("lockedapp", "pkgName = ?", new String[]{pkgName});

        Cursor cursor = readableDatabase.rawQuery("select * from lockedapp  where pkgName = ?", new String[]{pkgName});

        boolean b = cursor.moveToNext();

        return  b;
    }
}
