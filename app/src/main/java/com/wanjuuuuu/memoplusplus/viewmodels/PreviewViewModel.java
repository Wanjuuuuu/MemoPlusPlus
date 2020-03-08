package com.wanjuuuuu.memoplusplus.viewmodels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.wanjuuuuu.memoplusplus.adapters.PreviewMemoAdapter;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;

import java.util.List;

public class PreviewViewModel extends BaseViewModel {

    public interface OnUpdateListener {
        void onUpdated(int itemCount);
    }

    private PreviewMemoAdapter mAdapter;
    private LiveData<List<MemoWithFirstImage>> mMemos;
    private OnUpdateListener mUpdateListener;

    @Override
    protected void setUpAdapter() {
        mAdapter = new PreviewMemoAdapter();
    }

    public void setUpdateListener(OnUpdateListener updateListener) {
        mUpdateListener = updateListener;
    }

    public PreviewMemoAdapter getAdapter() {
        return mAdapter;
    }

    public void loadAllMemos(LifecycleOwner owner) {
        if (mMemos == null) {
            mMemos = mMemoDao.getAllMemoWithFirstImage();
        }
        mMemos.observe(owner, new Observer<List<MemoWithFirstImage>>() {
            @Override
            public void onChanged(List<MemoWithFirstImage> memoWithFirstImages) {
                int previousItemCount = mAdapter.getItemCount();
                mAdapter.setMemos(memoWithFirstImages);

                // memo is added or updated
                int currentItemCount = mAdapter.getItemCount();
                if (currentItemCount > 0 && currentItemCount >= previousItemCount) {
                    if (mUpdateListener != null) {
                        mUpdateListener.onUpdated(currentItemCount);
                    }
                }
            }
        });
    }
}
