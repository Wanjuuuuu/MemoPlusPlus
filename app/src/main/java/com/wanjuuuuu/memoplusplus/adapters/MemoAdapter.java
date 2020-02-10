package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
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

    public void addMemos(List<Memo> memos) {
        mMemoList.addAll(memos);
    }

    @NonNull
    @Override
    public MemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoHolder(LayoutInflater.from(mContext).inflate(R.layout.memo_view, parent,
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

        MemoHolder(View view) {
            super(view);
            mTitleView = view.findViewById(R.id.title_preview_text_view);
            mContentView = view.findViewById(R.id.content_preview_text_view);
            mThumbnailView = view.findViewById(R.id.thumbnail_image_view);
        }

        void bind(Memo memo) {
            mTitleView.setText(memo.getTitle());
            mContentView.setText(memo.getContent());
            // handle mThumbnailView
        }
    }
}
