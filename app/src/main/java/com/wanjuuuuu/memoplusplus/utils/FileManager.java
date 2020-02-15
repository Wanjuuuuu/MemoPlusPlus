package com.wanjuuuuu.memoplusplus.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FileManager {

    private static final String TAG = FileManager.class.getSimpleName();

    private FileManager() {
    }

    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    public static File createImageFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException exception) {
            Logger.error(TAG, "Error occurred while photoFile is creating");
        }
        return imageFile;
    }

    public static String getFilePathFromUri(Context context, Uri uri) {
        String filePath = null;
        Cursor cursor = null;
        try {
            String[] projections = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, projections, null, null, null);
            if (cursor != null) {
                int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                Logger.debug(TAG, cursor.getString(index));
                filePath = cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return filePath;
    }
}
