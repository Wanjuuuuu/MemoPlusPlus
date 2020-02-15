package com.wanjuuuuu.memoplusplus.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public final class PermissionManager {

    public static final int REQUEST_CODE_ALL = 0;
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;

    private static final String[] ALL_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private PermissionManager() {
    }

    public static void requestAll(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ActivityCompat.requestPermissions((Activity) context, ALL_PERMISSIONS, REQUEST_CODE_ALL);
    }

    public static void request(Context context, String[] permissions, int requestCode) {
        if (!(context instanceof Activity)) {
            return;
        }
        if (permissions == null || permissions.length == 0) {
            return;
        }
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }

    public static boolean isDenied(int[] grantResults) {
        if (grantResults == null || grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
}
