package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.models.Image;

import java.util.ArrayList;
import java.util.List;

public class DetailPhotoAdapter extends RecyclerView.Adapter<DetailPhotoAdapter.PhotoHolder> {

    private Context mContext;
    private List<Image> mImageList;

    public DetailPhotoAdapter(Context context) {
        mContext = context;
        mImageList = new ArrayList<>();
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
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.detail_photo_view,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Image image = mImageList.get(position);
        holder.bind(image);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoView;

        private PhotoHolder(View view) {
            super(view);
            mPhotoView = view.findViewById(R.id.detail_image_view);
        }

        private void bind(Image image) {
            String path = "";
            if (image != null && image.getPath() != null) {
                path = image.getPath();
            }
            Glide.with(mContext).load(path)
                    .error(mContext.getResources().getDrawable(R.drawable.ic_error)).into(mPhotoView);
        }
    }
}
