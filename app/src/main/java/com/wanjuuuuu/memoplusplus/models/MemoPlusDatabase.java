package com.wanjuuuuu.memoplusplus.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class, Image.class}, version = 1)
public abstract class MemoPlusDatabase extends RoomDatabase {

    private static final String DATABASE_FILENAME = "MemoPlusPlus.db";

    private static MemoPlusDatabase mInstance;

    public static MemoPlusDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (MemoPlusDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MemoPlusDatabase.class, DATABASE_FILENAME).allowMainThreadQueries().build();
                }
            }
        }
        return mInstance;
    }

    public abstract ImageDao imageDao();

    public abstract MemoDao memoDao();
}
