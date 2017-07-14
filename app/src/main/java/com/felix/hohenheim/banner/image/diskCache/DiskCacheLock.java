package com.felix.hohenheim.banner.image.diskCache;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hohenheim on 17/7/12.
 */

final class DiskCacheLock {

    private final Map<String, WriteLock> locks = new HashMap<>();
    private final WriteLockPool pool = new WriteLockPool();

    void acquire(String key) {
        WriteLock lock;
        synchronized (this) {
            lock = locks.get(key);
            if(lock == null) {
                lock = pool.obtain();
                locks.put(key, lock);
            }
            lock.writeThread ++;
        }
        lock.lock.lock();
    }

    void release(String key) {
        WriteLock lock;
        synchronized (this) {
            lock = locks.get(key);
            if(lock == null || lock.writeThread <= 0)
                throw new IllegalStateException("The key is null or the key's writeThread is error");
            if(--lock.writeThread == 0) {
                WriteLock removed = locks.remove(key);
                if(!removed.equals(lock))
                    throw new IllegalStateException("the key is wrong");
                pool.offer(lock);
            }
        }
        lock.lock.unlock();
    }

    private static class WriteLock {
        int writeThread = 0;
        final Lock lock = new ReentrantLock();
    }

    private static class WriteLockPool {
        private static final int MAX_POOL_SIZE = 10;
        private final Queue<WriteLock> pool = new ArrayDeque<>();

        WriteLock obtain() {
            WriteLock lock;
            synchronized(pool) {
                lock = pool.poll();
            }
            if(lock == null)
                lock = new WriteLock();
            return lock;
        }

        void offer(WriteLock writeLock) {
            synchronized (pool) {
                if(pool.size() < MAX_POOL_SIZE)
                    pool.offer(writeLock);
            }
        }
    }

}
