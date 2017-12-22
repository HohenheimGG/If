package com.hohenheim.homepage.db;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.SparseArrayCompat;

import com.hohenheim.BuildConfig;
import com.hohenheim.common.application.IfApplication;
import com.hohenheim.common.db.DBString;
import com.hohenheim.common.db.SQLOpenHelper;
import com.hohenheim.homepage.bean.RecentModal;

/**
 * Created by hohenheim on 2017/12/19.
 */

public class HomeRecentDB {

    private SparseArrayCompat<RecentModal> mRecentArray;
    private SQLiteDatabase mDatabase;

    private static class HomeRecentDBBuilder {
        private static HomeRecentDB sInstance = new HomeRecentDB();
    }

    public static HomeRecentDB getInstance() {
        return HomeRecentDBBuilder.sInstance;
    }

    private HomeRecentDB() {
        SQLOpenHelper helper = new SQLOpenHelper(IfApplication.getContext(),
                DBString.SCAN_HISTORY_DB_NAME,
                null,
                BuildConfig.VERSION_CODE);
        mDatabase = helper.getWritableDatabase();
        mRecentArray = new SparseArrayCompat<>();
    }
}
