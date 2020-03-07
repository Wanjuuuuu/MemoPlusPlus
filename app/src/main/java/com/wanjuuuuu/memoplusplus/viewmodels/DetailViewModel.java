package com.wanjuuuuu.memoplusplus.viewmodels;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wanjuuuuu.memoplusplus.models.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;
import com.wanjuuuuu.memoplusplus.utils.OnCompleteListener;

import java.util.List;

import static android.os.Looper.getMainLooper;

public class DetailViewModel extends ViewModel {

    private LiveData<List<Image>> mImages;

    public LiveData<List<Image>> getImages(Context context, long memoId) {
        if (mImages == null) {
            MemoPlusDatabase database = MemoPlusDatabase.getInstance(context);
            mImages = database.imageDao().getImages(memoId);
        }
        return mImages;
    }

    public void deleteMemoAndImages(final Context context, final Memo memo,
                           final OnCompleteListener completeListener) {
        DatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                MemoPlusDatabase database = MemoPlusDatabase.getInstance(context);
                database.imageDao().deleteImages(mImages.getValue());
                database.memoDao().deleteMemo(memo);

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
