package com.hohenheim.scancode.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hohenheim.BuildConfig;
import com.hohenheim.common.db.DBString;
import com.hohenheim.common.db.SQLOpenHelper;
import com.hohenheim.common.manager.DBModuleManager;
import com.hohenheim.scancode.modal.ScanResultModal;
import com.hohenheim.scancode.utils.HistoryConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by com.hohenheim on 2017/10/1.
 */

public class HistoryDB {

    private List<ScanResultModal> list = new ArrayList<>();
    private static volatile HistoryDB historyDB;
    private SQLiteDatabase database;

    private HistoryDB() {
        database = DBModuleManager.getInstance().getDataBase();
    }

    public static HistoryDB getInstance() {
        if(historyDB ==null) {
            synchronized (HistoryDB.class) {
                if(historyDB ==null)
                    historyDB = new HistoryDB();
            }
        }
        return historyDB;
    }

    public synchronized void saveHistory(String yearToDate, String hourToSecond, String content) {
        ContentValues values = new ContentValues();
        values.put(HistoryConstant.YEAR_TO_DATE, yearToDate);
        values.put(HistoryConstant.HOUR_TO_SECOND, hourToSecond);
        values.put(HistoryConstant.CONTENT, content);
        database.insert(DBString.SCAN_HISTORY_TABLE_NAME, null, values);
    }

    //加载RecyclerView展示的数据
    public synchronized List<ScanResultModal> loadListHistory() {
        list.clear();
        String lastData = "";
        Cursor cursor = database.query(DBString.SCAN_HISTORY_TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                String yearToDate = cursor.getString(cursor.getColumnIndex(HistoryConstant.YEAR_TO_DATE));
                String hourToSecond = cursor.getString(cursor.getColumnIndex(HistoryConstant.HOUR_TO_SECOND));
                String content = cursor.getString(cursor.getColumnIndex(HistoryConstant.CONTENT));

                ScanResultModal result = new ScanResultModal();
                result.setYearToDate(yearToDate);
                result.setHourToSecond(hourToSecond);
                result.setContent(content);
                result.setType(HistoryConstant.HISTORY_ITEM);

                if("".equals(lastData) || !yearToDate.equals(lastData)) {
                    lastData = yearToDate;
                    try {
                        ScanResultModal temp = (ScanResultModal)result.clone();
                        result.setType(HistoryConstant.HISTORY_TITLE);
                        list.add(temp);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                list.add(result);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
