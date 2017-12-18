package com.hohenheim.scancode.db;

import android.database.sqlite.SQLiteDatabase;

import com.hohenheim.common.db.DBInterface;

/**
 * Created by hohenheim on 2017/12/18.
 */

public class HistoryDBInterface extends DBInterface {

    private static final String CREATE_HISTORY_LIST = "create table scan_history_list("
            + "id integer primary key autoincrement,"
            + "year_to_date text,"
            + "hour_to_second text, "
            + "content text)";

    @Override
    protected void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY_LIST);
    }

    @Override
    protected void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
