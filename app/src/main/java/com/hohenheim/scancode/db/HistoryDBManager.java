package com.hohenheim.scancode.db;

import com.hohenheim.common.db.DBController;
import com.hohenheim.scancode.modal.ScanResultModal;
import java.util.List;

/**
 * Created by com.hohenheim on 2017/10/2.
 */

public class HistoryDBManager {

    private static final HistoryDBManagerImpl db = HistoryDBManagerImpl.getInstance();

    public void saveHistory(final String yearToDate, final String hourToSecond, final String content) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                db.saveHistory(yearToDate, hourToSecond, content);
            }
        };
        DBController.executor(runnable);
    }

    public void loadHistory() {
        DBController.executor(new Runnable() {
            @Override
            public void run() {
                List<ScanResultModal> list =  db.loadListHistory();
            }
        });

    }

}
