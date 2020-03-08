package com.wanjuuuuu.memoplusplus.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.util.List;

abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    abstract void setMemo(Memo memo);

    abstract void setImages(List<Image> images);
}
