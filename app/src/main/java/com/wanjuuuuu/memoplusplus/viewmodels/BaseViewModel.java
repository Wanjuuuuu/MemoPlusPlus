package com.wanjuuuuu.memoplusplus.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.wanjuuuuu.memoplusplus.models.ImageDao;
import com.wanjuuuuu.memoplusplus.models.MemoDao;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;

public abstract class BaseViewModel extends ViewModel {

    protected MemoDao mMemoDao;
    protected ImageDao mImageDao;

    public void init(Context context) {
        setUpAdapter();
        setUpDatabase(context);
    }

    protected abstract void setUpAdapter();

    private void setUpDatabase(Context context) {
        MemoPlusDatabase database = MemoPlusDatabase.getInstance(context);
        mMemoDao = database.memoDao();
        mImageDao = database.imageDao();
    }
}
