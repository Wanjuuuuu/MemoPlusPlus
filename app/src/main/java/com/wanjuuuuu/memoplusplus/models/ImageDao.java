package com.wanjuuuuu.memoplusplus.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM image where image.memoid = :memoId")
    List<ImageEntity> getImages(int memoId);

    @Insert
    void insertImages(List<ImageEntity> images);

    @Delete
    void deleteImages(List<ImageEntity> images);

    @Delete
    void deleteImage(ImageEntity image);
}
