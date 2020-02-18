package com.wanjuuuuu.memoplusplus.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoDao {

    @Transaction
    @Query("SELECT memo.*, image.* FROM memo " +
            "INNER JOIN image ON image.imageid " +
            "= (SELECT id FROM image where memo.id=image.memoid LIMIT 1) " +
            "ORDER BY modifytimestamp DESC")
    List<MemoWithFirstImage> getAllMemoWithFirstImage();

    @Query("SELECT * FROM memo WHERE memo.id = :id")
    MemoEntity getMemo(int id);

    @Insert
    void insertMemo(MemoEntity memo);

    @Delete
    void deleteMemo(MemoEntity memo);

    @Update
    void updateMemo(MemoEntity memo);
}
