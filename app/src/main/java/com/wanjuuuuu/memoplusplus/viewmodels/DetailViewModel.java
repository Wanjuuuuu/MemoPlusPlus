package com.wanjuuuuu.memoplusplus.viewmodels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.wanjuuuuu.memoplusplus.adapters.DetailMemoAdapter;
import com.wanjuuuuu.memoplusplus.utils.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.util.ArrayList;
import java.util.List;

public class DetailViewModel extends BaseViewModel {

    public interface OnCompleteListener {
        void onRemoved();
    }

    private static DatabaseManager sDatabaseManager = DatabaseManager.getInstance();

    private DetailMemoAdapter mAdapter;
    private Memo mMemo;
    private LiveData<List<Image>> mImages;

    @Override
    protected void setUpAdapter() {
        mAdapter = new DetailMemoAdapter();
    }

    public DetailMemoAdapter getAdapter() {
        return mAdapter;
    }

    public ArrayList<Image> getImages() {
        ArrayList<Image> images = new ArrayList<>();
        if (mImages.getValue() != null && !mImages.getValue().isEmpty()) {
            images.addAll(mImages.getValue());
        }
        return images;
    }

    public Memo getMemo() {
        return mMemo;
    }

    public void setMemo(Memo memo) {
        if (mMemo == null) {
            mMemo = memo;
        }
        mAdapter.setMemo(memo);
    }

    public void loadMemo(long memoId) {
        if (mMemo == null) {
            mMemo = mMemoDao.getMemo(memoId).getValue();
        }
        mAdapter.setMemo(mMemo);
    }

    public void loadImages(LifecycleOwner owner, long memoId) {
        if (mImages == null) {
            mImages = mImageDao.getImages(memoId);
        }
        mImages.observe(owner, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                mAdapter.setImages(images);
            }
        });
    }

    public void deleteMemoAndImages(final OnCompleteListener completeListener) {
        sDatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                mImageDao.deleteImages(mImages.getValue());
                mMemoDao.deleteMemo(mMemo);

                onComplete(completeListener);
            }
        });
    }

    private void onComplete(final OnCompleteListener completeListener) {
        sDatabaseManager.finishWith(new Runnable() {
            @Override
            public void run() {
                if (completeListener != null) {
                    completeListener.onRemoved();
                }
            }
        });
    }
}
