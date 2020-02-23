package com.wanjuuuuu.memoplusplus.models;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DatabaseManager {

    private static final int KEEP_ALIVE_TIME = 1;

    private static int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor mExecutor;

    private static DatabaseManager sInstance;

    static {
        sInstance = new DatabaseManager();
    }

    private DatabaseManager() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        mExecutor = new ThreadPoolExecutor(NUMBER_OF_PROCESSORS, NUMBER_OF_PROCESSORS,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, queue);
    }

    public static DatabaseManager getInstance() {
        return sInstance;
    }

    public static void executeTransaction(Runnable transaction) {
        sInstance.mExecutor.execute(transaction);
    }
}
