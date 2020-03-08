package com.wanjuuuuu.memoplusplus.viewmodels;

import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.wanjuuuuu.memoplusplus.models.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.utils.OnCompleteListener;

import java.util.List;

import static android.os.Looper.getMainLooper;

public class DetailViewModel extends BaseViewModel {

    private LiveData<List<Image>> mImages;

    public LiveData<List<Image>> getImages(long memoId) {
        if (mImages == null) {
            mImages = mImageDao.getImages(memoId);
        }
        return mImages;
    }

    public void deleteMemoAndImages(final Memo memo, final OnCompleteListener completeListener) {
        DatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                mImageDao.deleteImages(mImages.getValue());
                mMemoDao.deleteMemo(memo);

                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (completeListener != null) {
                            completeListener.onComplete();
                        }
                    }
                });
            }
        });
    }
}
