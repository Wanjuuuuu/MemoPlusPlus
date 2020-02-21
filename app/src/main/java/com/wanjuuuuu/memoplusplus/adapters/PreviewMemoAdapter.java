package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
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

    public void initMemos(List<MemoWithFirstImage> memos) {
        if (memos == null || memos.isEmpty()) {
            return;
        }
        mMemoList.addAll(memos);
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

            ConstraintLayout memoPreview = view.findViewById(R.id.memo_preview);
            memoPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onClick(mMemo);
                    }
                }
            });
            mTitleView = view.findViewById(R.id.preview_title_text_view);
            mContentView = view.findViewById(R.id.preview_content_text_view);
            mThumbnailView = view.findViewById(R.id.thumbnail_image_view);
        }

        private void bind(MemoWithFirstImage memo) {
            if (memo == null || memo.getMemo() == null) {
                return;
            }
            mMemo = memo;
            String title = mMemo.getMemo().getTitle();

            if (title == null || title.length() == 0) {
                title = mContext.getString(R.string.default_fill_title);
            }
            mTitleView.setText(title);
            String content = mMemo.getMemo().getContent();
            mContentView.setText(content);

            Image firstImage = memo.getFirstImage();
            if (firstImage == null || firstImage.getPath() == null) {
                mThumbnailView.setVisibility(View.GONE);
                return;
            }
            mThumbnailView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(firstImage.getPath())
                    .error(mContext.getDrawable(R.drawable.ic_fallback)).into(mThumbnailView);
        }
    }
}
