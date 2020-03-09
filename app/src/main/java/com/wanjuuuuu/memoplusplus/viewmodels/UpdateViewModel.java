package com.wanjuuuuu.memoplusplus.viewmodels;

import com.wanjuuuuu.memoplusplus.adapters.UpdateMemoAdapter;
import com.wanjuuuuu.memoplusplus.utils.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class UpdateViewModel extends BaseViewModel {

    public interface OnCompleteListener {
        void onAdded();

        void onUpdated(Memo memo);
    }

    private static DatabaseManager sDatabaseManager = DatabaseManager.getInstance();

    private UpdateMemoAdapter mAdapter;
    private Memo mMemo;
    private List<Image> mImages;
    private List<Image> mImagesInserted = new ArrayList<>();
    private List<Image> mImagesDeleted = new ArrayList<>();
    private boolean mIsNew;

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
        sDatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                long memoId = updateMemo();
                updateImages(memoId);

                onComplete(completeListener);
            }
        });
    }

    private long updateMemo() {
        mMemo = createMemo();

        long memoId = mMemo.getId();
        if (memoId == 0) {
            mIsNew = true;
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

    private void onComplete(final OnCompleteListener completeListener) {
        sDatabaseManager.finishWith(new Runnable() {
            @Override
            public void run() {
                if (completeListener != null) {
                    if (mIsNew) {
                        completeListener.onAdded();
                    } else {
                        completeListener.onUpdated(mMemo);
                    }
                }
            }
        });
    }
}
