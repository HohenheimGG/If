package com.hohenheim.common.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hohenheim.BuildConfig;
import com.hohenheim.common.application.IfApplication;
import com.hohenheim.common.db.DBInterface;
import com.hohenheim.common.db.DBString;
import com.hohenheim.common.db.SQLOpenHelper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hohenheim on 2017/12/17.
 */

public class DBModuleManager {

    private static volatile DBModuleManager sInstance;

    private List<DBInterface> moduleInterfaces;
    private SQLOpenHelper mHelper;
    private SQLiteDatabase mDatabase;

    private DBModuleManager() {
        moduleInterfaces = new CopyOnWriteArrayList<>();
    }

    public static DBModuleManager getInstance() {
        if(sInstance == null)
            synchronized (DBModuleManager.class) {
                if(sInstance == null)
                    sInstance = new DBModuleManager();
            }
        return sInstance;
    }

    public void registerModuleInterface(DBInterface moduleInterface) {
        if (moduleInterface != null && !moduleInterfaces.contains(moduleInterface)) {
            moduleInterfaces.add(moduleInterface);
        }
    }

    public List<DBInterface> getModuleInterfaces() {
        return Collections.unmodifiableList(moduleInterfaces);
    }

    public void initDB(Context context,
                       String dbName,
                       SQLiteDatabase.CursorFactory factory,
                       int versionCode) {
        mHelper = new SQLOpenHelper(context, dbName, factory, versionCode);
        mDatabase = mHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDataBase() {
        return mDatabase;
    }

}
