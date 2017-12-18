package com.hohenheim.homepage.db;

import android.database.sqlite.SQLiteDatabase;

import com.hohenheim.common.db.DBInterface;

/**
 * Created by hohenheim on 2017/12/17.
 */

public class RecentDBInterface extends DBInterface {

    private static final String CREATE_RECENT_LIST = "create table recent_used_list("
            + "id integer primary key autoincrement,"
            + "time text,"
            + "content text)";

    @Override
    protected void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECENT_LIST);
    }

    @Override
    protected void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL(CREATE_RECENT_LIST);
        }
    }
}
