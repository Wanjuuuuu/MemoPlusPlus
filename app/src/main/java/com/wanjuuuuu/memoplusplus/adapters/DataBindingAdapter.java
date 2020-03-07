package com.wanjuuuuu.memoplusplus.adapters;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;

import java.util.List;

public class DataBindingAdapter {

    @BindingAdapter({"bindItem"})
    public static void bindAllMemos(RecyclerView recyclerView, List<MemoWithFirstImage> memos) {
        PreviewMemoAdapter adapter = (PreviewMemoAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setMemos(memos);
        }
    }

    @BindingAdapter({"bindMemo", "bindImages"})
    public static void bindMemoAndImages(RecyclerView recyclerView, Memo memo, List<Image> images) {
        CustomAdapter adapter = (CustomAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setMemo(memo);
            adapter.setImages(images);
        }
    }
}
