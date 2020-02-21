package com.wanjuuuuu.memoplusplus.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "image", foreignKeys = @ForeignKey(entity = Memo.class, parentColumns = "id",
        childColumns = "memoid", onDelete = CASCADE))
public final class Image implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageid")
    private long imageId;

    @ColumnInfo(name = "memoid")
    private long memoId;

    private String path;

    public Image(long imageId, long memoId, String path) {
        this.imageId = imageId;
        this.memoId = memoId;
        this.path = path;
    }

    public void setMemoId(long memoId) {
        this.memoId = memoId;
    }

    public long getImageId() {
        return imageId;
    }

    public long getMemoId() {
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
        dest.writeLong(imageId);
        dest.writeLong(memoId);
        dest.writeString(path);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    private Image(Parcel source) {
        imageId = source.readLong();
        memoId = source.readLong();
        path = source.readString();
    }
}