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

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.activities.MemoDetailActivity;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoHolder> {

    private Context mContext;
    private List<Memo> mMemoList;

    public MemoAdapter(Context context) {
        mContext = context;
        mMemoList = new ArrayList<>();
    }

    public void insertMemos(List<Memo> memos) {
        if (memos == null || memos.isEmpty()) {
            return;
        }
//        int positionStart = getItemCount();
        mMemoList.addAll(memos);
//        notifyItemRangeInserted(positionStart, memos.size());
    }

    @NonNull
    @Override
    public MemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoHolder(LayoutInflater.from(mContext).inflate(R.layout.preview_memo_view,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemoHolder holder, int position) {
        Memo memo = mMemoList.get(position);
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
        private ConstraintLayout mMemoPreview;
        private Memo mMemo;

        private MemoHolder(View view) {
            super(view);
            mTitleView = view.findViewById(R.id.preview_title_text_view);
            mContentView = view.findViewById(R.id.preview_content_text_view);
            mThumbnailView = view.findViewById(R.id.thumbnail_image_view);
            mMemoPreview = view.findViewById(R.id.memo_preview);
            mMemoPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMemo == null) {
                        return;
                    }
                    Intent intent = new Intent(mContext, MemoDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        private void bind(Memo memo) {
            if (memo == null) {
                return;
            }
            mMemo = memo;
            mTitleView.setText(memo.getTitle());
            mContentView.setText(memo.getContent());
            // handle mThumbnailView
        }
    }
}
