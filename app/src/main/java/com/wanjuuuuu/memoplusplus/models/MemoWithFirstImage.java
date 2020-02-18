package com.wanjuuuuu.memoplusplus.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;

public class MemoWithFirstImage {

    @Embedded
    private MemoEntity memo;

    @Embedded
    private ImageEntity firstImage;

    public MemoWithFirstImage(@NonNull MemoEntity memo, @NonNull ImageEntity firstImage) {
        this.memo = memo;
        this.firstImage = firstImage;
    }

    public MemoEntity getMemo() {
        return memo;
    }

    public ImageEntity getFirstImage() {
        return firstImage;
    }
}
