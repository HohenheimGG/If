package com.hohenheim.homepage.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.SparseArrayCompat;

import com.hohenheim.BuildConfig;
import com.hohenheim.common.application.IfApplication;
import com.hohenheim.common.db.DBString;
import com.hohenheim.common.db.SQLOpenHelper;
import com.hohenheim.common.manager.DBModuleManager;
import com.hohenheim.homepage.bean.RecentModal;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hohenheim on 2017/12/19.
 */

public class HomeRecentDB {

    private LinkedList<String> mRecentArray;
    private SQLiteDatabase mDatabase;

    private static class HomeRecentDBBuilder {
        private static HomeRecentDB sInstance = new HomeRecentDB();
    }

    public static HomeRecentDB getInstance() {
        return HomeRecentDBBuilder.sInstance;
    }

    private HomeRecentDB() {
        mDatabase = DBModuleManager.getInstance().getDataBase();
        mRecentArray = new LinkedList<>();
    }

    public synchronized void saveModal(String time, String content) {
        ContentValues values = new ContentValues();
        values.put(RecentDBString.TIME, time);
        values.put(RecentDBString.CONTENT, content);
        mDatabase.insert(DBString.HOME_RECENT_TABLE_NAME, null, values);
    }

    public synchronized List<String> getContent() {
        mRecentArray.clear();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM "
                + DBString.HOME_RECENT_TABLE_NAME
                + " ORDER BY 0+time ASC", null);
        while(cursor.moveToNext()) {
            mRecentArray.push(cursor.getString(cursor.getColumnIndex(RecentDBString.TIME)));
        }
        cursor.close();
        return mRecentArray;
    }
}
