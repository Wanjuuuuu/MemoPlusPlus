package com.wanjuuuuu.memoplusplus.utils;

import android.content.Context;
import android.os.Build;

public final class ResourceManager {

    public static int getColor(Context context, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(color, null);
        } else {
            return context.getResources().getColor(color);
        }
    }
}
