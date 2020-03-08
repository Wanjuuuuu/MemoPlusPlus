package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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
import com.wanjuuuuu.memoplusplus.databinding.EditHeaderViewBinding;
import com.wanjuuuuu.memoplusplus.databinding.EditPhotoViewBinding;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class UpdateMemoAdapter extends BaseAdapter {

    public interface OnRemoveListener {
        void onRemove(Image image);
    }

    private static final String TAG = UpdateMemoAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTO = 1;

    private HeaderHolder mHeaderHolder;
    private Memo mMemo;
    private List<Image> mImageList;
    private OnRemoveListener mRemoveListener;

    public UpdateMemoAdapter() {
        mImageList = new ArrayList<>();
    }

    public void setOnRemoveListener(OnRemoveListener removeListener) {
        mRemoveListener = removeListener;
    }

    public void setMemo(Memo memo) {
        if (memo == null) {
            return;
        }
        mMemo = memo;
    }

    public void setImages(List<Image> images) {
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
            EditHeaderViewBinding binding =
                    EditHeaderViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent, false);
            mHeaderHolder = new HeaderHolder(binding);
            return mHeaderHolder;
        }
        EditPhotoViewBinding binding =
                EditPhotoViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new PhotoHolder(binding);
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

        private EditPhotoViewBinding mBinding;

        private PhotoHolder(EditPhotoViewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(final Image image) {
            if (image == null) {
                return;
            }
            mBinding.setClickListener(mRemoveListener);
            mBinding.setImage(image);

            String path = "";
            if (image.getPath() != null) {
                path = image.getPath();
            }

            Glide.with(mBinding.getRoot()).load(path).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    if (e != null) {
                        Logger.debug(TAG, e.getMessage());
                    }
                    if (mRemoveListener != null) {
                        mRemoveListener.onRemove(image);
                    }

                    Context context = mBinding.getRoot().getContext();
                    Toast.makeText(context, context.getString(R.string.toast_load_photo_error),
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
            }).into(mBinding.updateImageView);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        private EditHeaderViewBinding mBinding;

        private HeaderHolder(EditHeaderViewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(Memo memo) {
            if (memo == null) {
                return;
            }
            mBinding.setMemo(memo);
        }

        private String getTitle() {
            if (mBinding.updateTitleEditText.getText() == null) {
                return "";
            }
            return mBinding.updateTitleEditText.getText().toString();
        }

        private String getContent() {
            if (mBinding.updateContentEditText.getText() == null) {
                return "";
            }
            return mBinding.updateContentEditText.getText().toString();
        }
    }
}
