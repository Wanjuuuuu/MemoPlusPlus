package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.activities.MemoDetailActivity;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;
import com.wanjuuuuu.memoplusplus.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoHolder> {

    public interface OnClickListener {
        void onClick(MemoWithFirstImage memo);
    }

    private Context mContext;
    private List<MemoWithFirstImage> mMemoList;
    private OnClickListener mClickListener;

    public MemoAdapter(Context context) {
        mContext = context;
        mMemoList = new ArrayList<>();
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void initMemos(List<MemoWithFirstImage> memos) {
        if (memos == null || memos.isEmpty()) {
            return;
        }
        mMemoList.addAll(memos);
        Logger.debug("MemoAdapter", "length = " + getItemCount());
    }

    public void addMemo(MemoWithFirstImage memo) {
        if (memo == null) {
            return;
        }
        mMemoList.add(memo);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeMemo(MemoWithFirstImage memo) {
        if (memo == null) {
            return;
        }
        int position = mMemoList.indexOf(memo);
        mMemoList.remove(memo);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoHolder(LayoutInflater.from(mContext).inflate(R.layout.preview_memo_view,
                parent, false));
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

        private TextView mTitleView;
        private TextView mContentView;
        private ImageView mThumbnailView;
        private MemoWithFirstImage mMemo;

        private MemoHolder(View view) {
            super(view);
            mTitleView = view.findViewById(R.id.preview_title_text_view);
            mContentView = view.findViewById(R.id.preview_content_text_view);
            mThumbnailView = view.findViewById(R.id.thumbnail_image_view);
            ConstraintLayout memoPreview = view.findViewById(R.id.memo_preview);
            memoPreview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onClick(mMemo);
                    }
                }
            });
        }

        private void bind(MemoWithFirstImage memo) {
            Logger.debug("MemoAdapter", memo.getMemo().getId()+" ");
            if (memo == null || memo.getMemo() == null) {
                return;
            }
            mMemo = memo;
            mTitleView.setText(memo.getMemo().getTitle());
            mContentView.setText(memo.getMemo().getContent());

            Image firstImage = memo.getFirstImage();
            if (firstImage == null || firstImage.getPath() == null) {
                mThumbnailView.setVisibility(View.GONE);
                return;
            }
            Glide.with(mContext).load(firstImage.getPath())
                    .error(mContext.getResources().getDrawable(R.drawable.ic_error)).into(mThumbnailView);
        }
    }
}
