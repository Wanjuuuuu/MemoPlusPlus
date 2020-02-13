package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.models.Image;

import java.util.ArrayList;
import java.util.List;

public class EditPhotoAdapter extends RecyclerView.Adapter<EditPhotoAdapter.PhotoHolder> {

    private Context mContext;
    private List<Image> mImageList;

    public EditPhotoAdapter(Context context) {
        mContext = context;
        mImageList = new ArrayList<>();
    }

    public void addImages(List<Image> images) {
        mImageList.addAll(images);
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.edit_photo_view,
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
        private ImageButton mClearButton;

        PhotoHolder(View view) {
            super(view);
            mPhotoView = view.findViewById(R.id.detail_image_view);
            mClearButton = view.findViewById(R.id.clear_image_button);
        }

        void bind(Image image) {
            // handle mPhotoView
        }
    }
}
