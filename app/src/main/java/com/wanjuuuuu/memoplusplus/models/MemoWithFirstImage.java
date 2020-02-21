package com.wanjuuuuu.memoplusplus.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;

public class MemoWithFirstImage {

    @Embedded
    private Memo memo;

    @Embedded
    private Image firstImage;

    public MemoWithFirstImage(@NonNull Memo memo, Image firstImage) {
        this.memo = memo;
        this.firstImage = firstImage;
    }

    public Memo getMemo() {
        return memo;
    }

    public Image getFirstImage() {
        return firstImage;
    }
}
