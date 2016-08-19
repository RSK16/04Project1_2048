package com.cskaoyan.zhao.a04mobilemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhao on 2016/8/12.
 */
public class MyApplockDBHelper  extends SQLiteOpenHelper{


    public MyApplockDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table lockedapp (id integer primary key autoincrement, pkgname varch(100));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
