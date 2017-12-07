package com.hohenheim.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by com.hohenheim on 2017/10/1.
 */

public class SQLOpenHelper extends SQLiteOpenHelper {


    private static final String CREATE_ARTICLE_DETAIL_LIST = "create table scan_history_list("
            + "id integer primary key autoincrement,"
            + "year_to_date text,"
            + "hour_to_second text, "
            + "content text)";

    public SQLOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE_DETAIL_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
