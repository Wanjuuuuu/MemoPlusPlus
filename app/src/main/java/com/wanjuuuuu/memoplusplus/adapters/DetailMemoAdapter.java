package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.databinding.DetailHeaderViewBinding;
import com.wanjuuuuu.memoplusplus.databinding.DetailPhotoViewBinding;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.util.ArrayList;
import java.util.List;

public class DetailMemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTO = 1;

    private Context mContext;
    private Memo mMemo;
    private List<Image> mImageList;

    public DetailMemoAdapter(Context context) {
        mContext = context;
        mImageList = new ArrayList<>();
    }

    @BindingAdapter({"bindMemo", "bindImages"})
    public static void bindItem(RecyclerView recyclerView, Memo memo, List<Image> images) {
        DetailMemoAdapter adapter = (DetailMemoAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setMemo(memo);
            adapter.setImages(images);
        }
    }

    private void setMemo(Memo memo) {
        if (memo == null) {
            return;
        }
        mMemo = memo;
        notifyItemChanged(TYPE_HEADER);
    }

    private void setImages(List<Image> images) {
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

        private DetailPhotoViewBinding binding;

        private PhotoHolder(DetailPhotoViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Image image) {
            if (image == null) {
                return;
            }
            binding.setImage(image);

            String path = "";
            if (image != null && image.getPath() != null) {
                path = image.getPath();
            }
            Glide.with(mContext).load(path)
                    .error(mContext.getDrawable(R.drawable.ic_fallback)).into(binding.detailImageView);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        private DetailHeaderViewBinding binding;

        private HeaderHolder(DetailHeaderViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Memo memo) {
            if (memo == null) {
                return;
            }
            binding.setMemo(memo);
        }
    }
}
