package com.hohenheim.common.deprecated.image;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by com.hohenheim on 13/07/2017.
 */

public class ImageThreadPoolExecutor extends ThreadPoolExecutor {


    public ImageThreadPoolExecutor(int poolSize) {
        this(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new DefaultThreadFactory());
    }

    public ImageThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAlive, TimeUnit timeUnit,
                                          ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAlive, timeUnit, new PriorityBlockingQueue<Runnable>(), threadFactory);

    }

    public static class DefaultThreadFactory implements ThreadFactory {
        int threadNum = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            final Thread result = new Thread(runnable, "image_pool_thread_" + threadNum) {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    super.run();
                }
            };
            threadNum++;
            return result;
        }
    }
}


