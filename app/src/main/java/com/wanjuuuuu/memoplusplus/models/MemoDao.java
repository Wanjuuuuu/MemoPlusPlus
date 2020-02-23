package com.wanjuuuuu.memoplusplus.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoDao {

    @Transaction
    @Query("SELECT * FROM (\n" +
            "SELECT memo.*, image.* FROM memo\n" +
            "INNER JOIN image ON image.imageid = (SELECT imageid FROM image WHERE memoid = memo.id " +
            "LIMIT 1)\n" +
            "UNION ALL\n" +
            "SELECT memo.*, CAST(NULL AS INT), CAST(NULL AS INT), CAST(NULL AS STRING) FROM " +
            "memo\n" +
            "WHERE NOT EXISTS (SELECT imageid FROM image WHERE memoid = memo.id)\n" +
            ") AS together GROUP BY together.id ORDER BY together.modifytimestamp")
    LiveData<List<MemoWithFirstImage>> getAllMemoWithFirstImage();

    @Transaction
    @Query("SELECT * FROM (\n" +
            "SELECT memo.*, image.* FROM memo\n" +
            "JOIN image ON image.imageid = (SELECT imageid FROM image WHERE memoid = :id LIMIT 1)" +
            "\n" +
            "WHERE memo.id = :id\n" +
            "UNION\n" +
            "SELECT memo.*, CAST(NULL AS INT), CAST(NULL AS INT), CAST(NULL AS STRING) FROM " +
            "memo\n" +
            "WHERE memo.id = :id\n" +
            ") AS together GROUP BY together.id")
    LiveData<MemoWithFirstImage> getMemoWithFirstImage(long id);

    @Query("SELECT * FROM memo WHERE memo.id = :id")
    LiveData<Memo> getMemo(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMemo(Memo memo);

    @Delete
    void deleteMemo(Memo memo);

    @Update
    void updateMemo(Memo memo);
}
