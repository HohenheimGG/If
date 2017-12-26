package com.hohenheim.common.db;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by hohenheim on 2017/12/26.
 */

public class DBController {

    private static Executor executor = Executors.newFixedThreadPool(2);

    public static synchronized void executor(Runnable runnable) {
        executor.execute(runnable);
    }
}
