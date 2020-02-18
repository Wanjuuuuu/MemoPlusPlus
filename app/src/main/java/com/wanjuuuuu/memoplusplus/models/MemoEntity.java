package com.wanjuuuuu.memoplusplus.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memo")
public final class MemoEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String content;

    @ColumnInfo(name = "modifytimestamp")
    private long modifyTimestamp;

    public MemoEntity(int id, String title, String content, long modifyTimestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modifyTimestamp = modifyTimestamp;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getModifyTimestamp() {
        return modifyTimestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(modifyTimestamp);
    }

    public static final Parcelable.Creator<MemoEntity> CREATOR = new Parcelable.Creator<MemoEntity>() {
        @Override
        public MemoEntity createFromParcel(Parcel source) {
            return new MemoEntity(source);
        }

        @Override
        public MemoEntity[] newArray(int size) {
            return new MemoEntity[size];
        }
    };

    private MemoEntity(Parcel source) {
        id = source.readInt();
        title = source.readString();
        content = source.readString();
    }
}
