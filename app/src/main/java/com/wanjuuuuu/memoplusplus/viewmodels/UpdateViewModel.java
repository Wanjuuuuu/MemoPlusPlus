package com.wanjuuuuu.memoplusplus.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import com.wanjuuuuu.memoplusplus.models.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.ImageDao;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.models.MemoDao;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;
import com.wanjuuuuu.memoplusplus.utils.OnCompleteListener;

import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;

public class UpdateViewModel extends ViewModel {

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

    public void updateMemoAndImages(final Context context, final Memo memo,
                                    final OnCompleteListener completeListener) {
        DatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                long memoId = updateMemo(context, memo);
                updateImages(context, memoId);

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

    private long updateMemo(Context context, Memo memo) {
        MemoDao memoDao = MemoPlusDatabase.getInstance(context).memoDao();

        long memoId = memo.getId();
        if (memoId == 0) {
            memoId = memoDao.insertMemo(memo);
        } else {
            memoDao.updateMemo(memo);
        }
        return memoId;
    }

    private void updateImages(Context context, long memoId) {
        ImageDao imageDao = MemoPlusDatabase.getInstance(context).imageDao();

        List<Image> newImages = new ArrayList<>();
        for (Image image : mImagesInserted) {
            if (image != null) {
                Image newImage = new Image(image.getImageId(), memoId, image.getPath());
                newImages.add(newImage);
            }
        }
        imageDao.insertImages(newImages);
        imageDao.deleteImages(mImagesDeleted);
    }
}
