package com.felix.hohenheim.banner.zxing.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.felix.hohenheim.banner.db.SQLOpenHelper;
import com.felix.hohenheim.banner.zxing.modal.ScanResultModal;
import com.felix.hohenheim.banner.zxing.utils.HistoryConstant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by hohenheim on 2017/10/1.
 */

public class HistoryDB {

    private static final String DB_NAME = "scan_history.db";
    private static final String TABLE_ARTICLE_LIST = "scan_history_list";
    private static final int VERSION = 1;

    private List<ScanResultModal> list = new ArrayList<>();
    private static volatile HistoryDB historyDB;
    private SQLiteDatabase database;

    private HistoryDB(Context context) {
        SQLOpenHelper helper = new SQLOpenHelper(context, DB_NAME, null, VERSION);
        database = helper.getWritableDatabase();
    }

    public static HistoryDB getInstance(Context context) {
        if(historyDB ==null) {
            synchronized (HistoryDB.class) {
                if(historyDB ==null)
                    historyDB = new HistoryDB(context);
            }
        }
        return historyDB;
    }

    public synchronized void saveHistory(String yearToDate, String hourToSecond, String content) {
        ContentValues values = new ContentValues();
        values.put(HistoryConstant.YEAR_TO_DATE, yearToDate);
        values.put(HistoryConstant.HOUR_TO_SECOND, hourToSecond);
        values.put(HistoryConstant.CONTENT, content);
        database.insert(TABLE_ARTICLE_LIST, null, values);
    }

    //加载RecyclerView展示的数据
    public synchronized List<ScanResultModal> loadListHistory() {
        list.clear();
        String lastData = "";
        Cursor cursor = database.query(TABLE_ARTICLE_LIST, null, null, null, null, null, null);
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
