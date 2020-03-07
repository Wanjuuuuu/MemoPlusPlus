package com.wanjuuuuu.memoplusplus.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

@Entity(tableName = "memo")
public final class Memo implements Parcelable {

    @Ignore
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Seoul");
    @Ignore
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 " +
            "mm분");

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;

    private String content;

    @ColumnInfo(name = "modifytimestamp")
    private long modifyTimestamp;

    public Memo(long id, String title, String content, long modifyTimestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modifyTimestamp = modifyTimestamp;
    }

    public long getId() {
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

    public String getModifyDatetime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TIME_ZONE);
        calendar.setTimeInMillis(modifyTimestamp);
        return DATE_FORMAT.format(calendar.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(modifyTimestamp);
    }

    public static final Parcelable.Creator<Memo> CREATOR = new Parcelable.Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel source) {
            return new Memo(source);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };

    private Memo(Parcel source) {
        id = source.readLong();
        title = source.readString();
        content = source.readString();
        modifyTimestamp = source.readLong();
    }
}