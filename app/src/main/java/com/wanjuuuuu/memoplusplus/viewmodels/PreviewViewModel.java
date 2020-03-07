package com.wanjuuuuu.memoplusplus.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wanjuuuuu.memoplusplus.models.MemoDao;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;

import java.util.List;

public class PreviewViewModel extends ViewModel {

    private LiveData<List<MemoWithFirstImage>> mMemos;

    public LiveData<List<MemoWithFirstImage>> getAllMemos(Context context) {
        if (mMemos == null) {
            MemoDao memoDao = MemoPlusDatabase.getInstance(context).memoDao();
            mMemos = memoDao.getAllMemoWithFirstImage();
        }
        return mMemos;
    }
}
