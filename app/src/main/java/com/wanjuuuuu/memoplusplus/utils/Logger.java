package com.wanjuuuuu.memoplusplus.utils;

import android.util.Log;

public final class Logger {

    private static final String TAG = "MemoPlusPlus";

    private Logger() {
    }

    public static void debug(String className, String message) {
        Log.d("[" + TAG + " - " + className + "]", message);
    }

    public static void error(String className, String message) {
        Log.e("[" + TAG + " - " + className + "]", message);
    }
}
