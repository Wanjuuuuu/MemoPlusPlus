package com.wanjuuuuu.memoplusplus.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public final class PermissionManager {

    private static final int REQUEST_CODE_ERROR = -1;
    private static final int REQUEST_CODE_ALL = 0;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 3;
    private static final int REQUEST_CODE_CAMERA = 4;

    private static PermissionManager mManager;
    private static List<String> mPermissions;

    private PermissionManager() {
        mPermissions = new ArrayList<>();
    }

    public static PermissionManager getInstance() {
        if (mManager == null) {
            mManager = new PermissionManager();
        }
        return mManager;
    }

    public void requestAll(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }

        mPermissions.clear();
        checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        checkPermission(context, Manifest.permission.CAMERA);

        if (!mPermissions.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    mPermissions.toArray(new String[0]), REQUEST_CODE_ALL);
        }
    }

    public void request(Context context, String permission) {
        if (!(context instanceof Activity)) {
            return;
        }
        int requestCode = getRequestCode(permission);
        if (requestCode == REQUEST_CODE_ERROR) {
            return;
        }

        mPermissions.clear();
        checkPermission(context, permission);

        if (!mPermissions.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    mPermissions.toArray(new String[0]), requestCode);
        }
    }

    public boolean isGranted(int[] grantResults) {
        if (grantResults != null && grantResults.length > 0) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getRequestCode(String permission) {
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return REQUEST_CODE_READ_EXTERNAL_STORAGE;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return REQUEST_CODE_WRITE_EXTERNAL_STORAGE;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return REQUEST_CODE_ACCESS_FINE_LOCATION;
            case Manifest.permission.CAMERA:
                return REQUEST_CODE_CAMERA;
        }
        return REQUEST_CODE_ERROR;
    }

    private void checkPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            mPermissions.add(permission);
        }
    }

}
