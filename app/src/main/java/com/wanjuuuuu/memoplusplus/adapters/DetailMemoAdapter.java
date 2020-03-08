package com.wanjuuuuu.memoplusplus.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.databinding.DetailHeaderViewBinding;
import com.wanjuuuuu.memoplusplus.databinding.DetailPhotoViewBinding;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.util.ArrayList;
import java.util.List;

public class DetailMemoAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTO = 1;

    private Memo mMemo;
    private List<Image> mImageList;

    public DetailMemoAdapter() {
        mImageList = new ArrayList<>();
    }

    public void setMemo(Memo memo) {
        if (memo == null) {
            return;
        }
        mMemo = memo;
        notifyItemChanged(TYPE_HEADER);
    }

    public void setImages(List<Image> images) {
        mImageList.clear();
        if (images != null && !images.isEmpty()) {
            mImageList.addAll(images);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            DetailHeaderViewBinding binding =
                    DetailHeaderViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent, false);
            return new HeaderHolder(binding);
        }
        DetailPhotoViewBinding binding =
                DetailPhotoViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
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

        private DetailPhotoViewBinding mBinding;

        private PhotoHolder(DetailPhotoViewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(Image image) {
            if (image == null) {
                return;
            }
            mBinding.setImage(image);

            String path = "";
            if (image.getPath() != null) {
                path = image.getPath();
            }

            Glide.with(mBinding.getRoot()).load(path)
                    .error(R.drawable.ic_fallback).into(mBinding.detailImageView);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        private DetailHeaderViewBinding mBinding;

        private HeaderHolder(DetailHeaderViewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(Memo memo) {
            if (memo == null) {
                return;
            }
            mBinding.setMemo(memo);
        }
    }
}
