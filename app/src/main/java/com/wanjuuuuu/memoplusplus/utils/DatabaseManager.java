package com.wanjuuuuu.memoplusplus.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DatabaseManager {

    private static final int COMPLETE = 0;

    private static final int KEEP_ALIVE_TIME = 1;

    private static int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor mExecutor;

    private static DatabaseManager sInstance;

    private Handler mHandler;

    static {
        sInstance = new DatabaseManager();
    }

    private DatabaseManager() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        mExecutor = new ThreadPoolExecutor(NUMBER_OF_PROCESSORS, NUMBER_OF_PROCESSORS,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, queue);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == COMPLETE) {
                    Runnable runnable = (Runnable) msg.obj;
                    post(runnable);
                    return;
                }
                super.handleMessage(msg);
            }
        };
    }

    public static DatabaseManager getInstance() {
        return sInstance;
    }

    public void executeTransaction(Runnable transaction) {
        sInstance.mExecutor.execute(transaction);
    }

    public void finishWith(Runnable runnable) {
        handleState(runnable, COMPLETE);
    }

    private void handleState(Runnable runnable, int state) {
        mHandler.obtainMessage(state, runnable).sendToTarget();
    }
}
