package com.wanjuuuuu.memoplusplus.viewmodels;

import android.os.Handler;

import com.wanjuuuuu.memoplusplus.models.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.utils.OnCompleteListener;

import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;

public class UpdateViewModel extends BaseViewModel {

    private List<Image> mImagesInserted = new ArrayList<>();
    private List<Image> mImagesDeleted = new ArrayList<>();

    public void addImage(Image image) {
        mImagesInserted.add(image);
    }

    public void deleteImage(Image image) {
        if (image.getImageId() == 0) {
            mImagesInserted.remove(image);
        } else {
            mImagesDeleted.add(image);
        }
    }

    public void updateMemoAndImages(final Memo memo, final OnCompleteListener completeListener) {
        DatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                long memoId = updateMemo(memo);
                updateImages(memoId);

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

    private long updateMemo(Memo memo) {
        long memoId = memo.getId();
        if (memoId == 0) {
            memoId = mMemoDao.insertMemo(memo);
        } else {
            mMemoDao.updateMemo(memo);
        }
        return memoId;
    }

    private void updateImages(long memoId) {
        List<Image> newImages = new ArrayList<>();
        for (Image image : mImagesInserted) {
            if (image != null) {
                Image newImage = new Image(image.getImageId(), memoId, image.getPath());
                newImages.add(newImage);
            }
        }
        mImageDao.insertImages(newImages);
        mImageDao.deleteImages(mImagesDeleted);
    }
}
