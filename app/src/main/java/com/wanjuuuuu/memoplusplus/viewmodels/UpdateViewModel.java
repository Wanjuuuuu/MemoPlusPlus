package com.wanjuuuuu.memoplusplus.viewmodels;

import android.os.Handler;

import com.wanjuuuuu.memoplusplus.adapters.UpdateMemoAdapter;
import com.wanjuuuuu.memoplusplus.models.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;

public class UpdateViewModel extends BaseViewModel {

    public interface OnCompleteListener {
        void onAdded();

        void onUpdated(Memo memo);
    }

    private UpdateMemoAdapter mAdapter;
    private Memo mMemo;
    private List<Image> mImages;
    private List<Image> mImagesInserted = new ArrayList<>();
    private List<Image> mImagesDeleted = new ArrayList<>();

    @Override
    protected void setUpAdapter() {
        mAdapter = new UpdateMemoAdapter();
        mAdapter.setOnRemoveListener(new UpdateMemoAdapter.OnRemoveListener() {
            @Override
            public void onRemove(Image image) {
                removeImage(image);
            }
        });
    }

    public UpdateMemoAdapter getAdapter() {
        return mAdapter;
    }

    public void setMemo(Memo memo) {
        if (mMemo == null) {
            mMemo = memo;
        }
        mAdapter.setMemo(mMemo);
    }

    public void setImages(List<Image> images) {
        if (mImages == null) {
            mImages = images;
        }
        mAdapter.setImages(mImages);
    }

    public boolean isContentFilled() {
        return mAdapter.isContentFilled();
    }

    public void addImage(String imagePath) {
        Image image = new Image(0, 0, imagePath);
        mImagesInserted.add(image);
        mAdapter.addImage(image);
    }

    private void removeImage(Image image) {
        if (image.getImageId() == 0) {
            mImagesInserted.remove(image);
        } else {
            mImagesDeleted.add(image);
        }
        mAdapter.removeImage(image);
    }

    public void updateMemoAndImages(final OnCompleteListener completeListener) {
        DatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                final boolean isAdding = (mMemo == null);

                long memoId = updateMemo();
                updateImages(memoId);

                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (completeListener != null) {
                            if (isAdding) {
                                completeListener.onAdded();
                            } else {
                                completeListener.onUpdated(mMemo);
                            }
                        }
                    }
                });
            }
        });
    }

    private long updateMemo() {
        mMemo = createMemo();

        long memoId = mMemo.getId();
        if (memoId == 0) {
            memoId = mMemoDao.insertMemo(mMemo);
        } else {
            mMemoDao.updateMemo(mMemo);
        }
        return memoId;
    }

    private Memo createMemo() {
        String title = mAdapter.getTitle();
        String content = mAdapter.getContent();
        long timestamp = System.currentTimeMillis();

        if (mMemo == null) {
            return new Memo(0, title, content, timestamp);
        }
        return new Memo(mMemo.getId(), title, content, timestamp);
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
