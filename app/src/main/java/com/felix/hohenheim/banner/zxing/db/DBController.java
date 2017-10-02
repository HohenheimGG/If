package com.felix.hohenheim.banner.zxing.db;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.felix.hohenheim.banner.IfApplication;
import com.felix.hohenheim.banner.zxing.modal.ScanResultModal;
import com.felix.hohenheim.banner.zxing.utils.HistoryConstant;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by hohenheim on 2017/10/2.
 */

public class DBController {

    private static final HistoryDB db = HistoryDB.getInstance(IfApplication.getContext());
    private static Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler;

    public DBController(Handler handler) {
        if(handler.getLooper() != Looper.getMainLooper())
            throw new IllegalStateException();
        this.handler = handler;
    }

    public DBController(){

    }

    public void saveHistory(final String yearToDate, final String hourToSecond, final String content) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.saveHistory(yearToDate, hourToSecond, content);
            }
        });
    }

    public void loadHistory() {
        if(handler == null) {
            Log.d("DBController", "请先初始化handler");
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<ScanResultModal> list =  db.loadHistory();
                Message.obtain(handler, HistoryConstant.HANDLER_LOAD_HISTORY, list).sendToTarget();
            }
        });
    }

}
