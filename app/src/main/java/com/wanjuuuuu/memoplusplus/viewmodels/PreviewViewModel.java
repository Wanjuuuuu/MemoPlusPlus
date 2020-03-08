package com.wanjuuuuu.memoplusplus.viewmodels;

import androidx.lifecycle.LiveData;

import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;

import java.util.List;

public class PreviewViewModel extends BaseViewModel {

    private LiveData<List<MemoWithFirstImage>> mMemos;

    public LiveData<List<MemoWithFirstImage>> getAllMemos() {
        if (mMemos == null) {
            mMemos = mMemoDao.getAllMemoWithFirstImage();
        }
        return mMemos;
    }
}
