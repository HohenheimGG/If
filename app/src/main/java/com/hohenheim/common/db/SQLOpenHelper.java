package com.hohenheim.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hohenheim.common.manager.DBModuleManager;

import java.util.List;

/**
 * Created by com.hohenheim on 2017/10/1.
 */

public class SQLOpenHelper extends SQLiteOpenHelper {

    private List<DBInterface> mDBList;

    public SQLOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mDBList = DBModuleManager.getInstance().getModuleInterfaces();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(DBInterface dbInterface: mDBList) {
            dbInterface.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(DBInterface dbInterface: mDBList) {
            dbInterface.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
