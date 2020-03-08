package com.wanjuuuuu.memoplusplus.adapters;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class DataBindingAdapter {

    @BindingAdapter({"adapter"})
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }
}
