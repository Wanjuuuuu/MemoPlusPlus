package com.wanjuuuuu.memoplusplus.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "image", foreignKeys = @ForeignKey(entity = MemoEntity.class, parentColumns = "id",
        childColumns = "memoid", onDelete = CASCADE))
public final class ImageEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageid")
    private int imageId;

    @ColumnInfo(name = "memoid")
    private int memoId;

    private String path;

    public ImageEntity(int imageId, int memoId, String path) {
        this.imageId = imageId;
        this.memoId = memoId;
        this.path = path;
    }

    public int getImageId() {
        return imageId;
    }

    public int getMemoId() {
        return memoId;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeInt(memoId);
        dest.writeString(path);
    }

    public static final Parcelable.Creator<ImageEntity> CREATOR = new Parcelable.Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel source) {
            return new ImageEntity(source);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };

    private ImageEntity(Parcel source) {
        imageId = source.readInt();
        memoId = source.readInt();
        path = source.readString();
    }
}
