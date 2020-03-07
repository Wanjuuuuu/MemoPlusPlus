package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.databinding.PreviewMemoViewBinding;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;

import java.util.ArrayList;
import java.util.List;

public class PreviewMemoAdapter extends RecyclerView.Adapter<PreviewMemoAdapter.MemoHolder> {

    public interface OnClickListener {
        void onClick(MemoWithFirstImage memo);
    }

    private Context mContext;
    private List<MemoWithFirstImage> mMemoList;
    private OnClickListener mClickListener;

    public PreviewMemoAdapter(Context context) {
        mContext = context;
        mMemoList = new ArrayList<>();
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @BindingAdapter({"bindItem"})
    public static void bindItem(RecyclerView recyclerView, List<MemoWithFirstImage> memos) {
        PreviewMemoAdapter adapter = (PreviewMemoAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setMemos(memos);
        }
    }

    private void setMemos(List<MemoWithFirstImage> memos) {
        mMemoList.clear();
        if (memos == null || memos.isEmpty()) {
            return;
        }
        mMemoList.addAll(memos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PreviewMemoViewBinding binding =
                PreviewMemoViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new MemoHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoHolder holder, int position) {
        MemoWithFirstImage memo = mMemoList.get(position);
        holder.bind(memo);
    }

    @Override
    public int getItemCount() {
        return mMemoList.size();
    }

    class MemoHolder extends RecyclerView.ViewHolder {

        private PreviewMemoViewBinding binding;

        private MemoHolder(PreviewMemoViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(MemoWithFirstImage memo) {
            if (memo == null || memo.getMemo() == null) {
                return;
            }
            binding.setMemoWithFirstImage(memo);
            binding.setClickListener(mClickListener);

            String title = memo.getMemo().getTitle();
            if (title == null || title.length() == 0) {
                title = mContext.getString(R.string.default_fill_title);
            }
            binding.previewTitleTextView.setText(title);

            Image firstImage = memo.getFirstImage();
            if (firstImage == null || firstImage.getPath() == null) {
                binding.thumbnailImageView.setVisibility(View.GONE);
                return;
            }
            binding.thumbnailImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(firstImage.getPath())
                    .error(mContext.getDrawable(R.drawable.ic_fallback)).into(binding.thumbnailImageView);
        }
    }
}
