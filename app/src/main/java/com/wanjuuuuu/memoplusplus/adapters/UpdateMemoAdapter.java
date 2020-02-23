package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.utils.Logger;
import com.wanjuuuuu.memoplusplus.views.ClearEditText;

import java.util.ArrayList;
import java.util.List;

public class UpdateMemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnRemoveListener {
        void onRemove(Image image);
    }

    private static final String TAG = UpdateMemoAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTO = 1;

    private Context mContext;
    private HeaderHolder mHeaderHolder;
    private Memo mMemo;
    private List<Image> mImageList;
    private OnRemoveListener mRemoveListener;

    public UpdateMemoAdapter(Context context) {
        mContext = context;
        mImageList = new ArrayList<>();
    }

    public void setOnRemoveListener(OnRemoveListener removeListener) {
        mRemoveListener = removeListener;
    }

    public void initItems(Memo memo, List<Image> images) {
        if (memo == null) {
            return;
        }
        mMemo = memo;

        if (images == null || images.isEmpty()) {
            return;
        }
        mImageList.addAll(images);
    }

    public void addImage(Image image) {
        if (image == null) {
            return;
        }
        mImageList.add(image);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeImage(Image image) {
        if (image == null) {
            return;
        }
        int position = mImageList.indexOf(image);
        mImageList.remove(position);
        notifyItemRemoved(position + 1);
    }

    public boolean isContentFilled() {
        return (getTitle().length() > 0 || getContent().length() > 0);
    }

    public String getTitle() {
        if (mHeaderHolder == null) {
            return "";
        }
        return mHeaderHolder.getTitle();
    }

    public String getContent() {
        if (mHeaderHolder == null) {
            return "";
        }
        return mHeaderHolder.getContent();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            mHeaderHolder = new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.edit_header_view, parent, false));
            return mHeaderHolder;
        }
        return new PhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.edit_photo_view,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).bind(mMemo);
        } else if (holder instanceof PhotoHolder) {
            Image image = mImageList.get(position - 1);
            ((PhotoHolder) holder).bind(image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == TYPE_HEADER) ? TYPE_HEADER : TYPE_PHOTO;
    }

    @Override
    public int getItemCount() {
        return mImageList.size() + 1;
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoView;
        private Image mImage;

        private PhotoHolder(View view) {
            super(view);
            mPhotoView = view.findViewById(R.id.update_image_view);
            ImageButton clearButton = view.findViewById(R.id.clear_image_button);
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRemoveListener != null) {
                        mRemoveListener.onRemove(mImage);
                    }
                }
            });
        }

        private void bind(Image image) {
            if (image == null) {
                return;
            }
            mImage = image;

            Glide.with(mContext).load(mImage.getPath()).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    if (e != null) {
                        Logger.debug(TAG, e.getMessage());
                    }
                    if (mRemoveListener != null) {
                        mRemoveListener.onRemove(mImage);
                    }

                    Toast.makeText(mContext, mContext.getString(R.string.toast_load_photo_error),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model,
                                               Target<Drawable> target,
                                               DataSource dataSource,
                                               boolean isFirstResource) {
                    return false;
                }
            }).into(mPhotoView);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        private ClearEditText mTitleEditText;
        private ClearEditText mContentEditText;

        private HeaderHolder(View view) {
            super(view);
            mTitleEditText = view.findViewById(R.id.update_title_edit_text);
            mContentEditText = view.findViewById(R.id.update_content_edit_text);
        }

        private void bind(Memo memo) {
            if (memo == null) {
                return;
            }
            mTitleEditText.setText(memo.getTitle());
            mContentEditText.setText(memo.getContent());
        }

        private String getTitle() {
            if (mTitleEditText.getText() == null) {
                return "";
            }
            return mTitleEditText.getText().toString();
        }

        private String getContent() {
            if (mContentEditText.getText() == null) {
                return "";
            }
            return mContentEditText.getText().toString();
        }
    }
}
