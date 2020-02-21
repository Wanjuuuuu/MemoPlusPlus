package com.wanjuuuuu.memoplusplus.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM image where image.memoid = :memoId")
    List<Image> getImages(long memoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertImages(List<Image> images);

    @Delete
    void deleteImages(List<Image> images);
}
