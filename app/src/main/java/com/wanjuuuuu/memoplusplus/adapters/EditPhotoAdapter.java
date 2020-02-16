package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

    public void insertImage(Image image) {
        if (image == null) {
            return;
        }
        mImageList.add(image);
        notifyItemInserted(getItemCount() - 1);
    }

    public void insertImages(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        int positionStart = getItemCount();
        mImageList.addAll(images);
        notifyItemRangeInserted(positionStart, images.size());
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.edit_photo_view,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        if (mImageList.isEmpty()) {
            return;
        }
        Image image = mImageList.get(position);
        holder.bind(image);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    private void removeImage(Image image) {
        if (image == null) {
            return;
        }
        int position = mImageList.indexOf(image);
        mImageList.remove(position);
        notifyItemRemoved(position);
    }

    class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPhotoView;
        private Image mImage;

        private PhotoHolder(View view) {
            super(view);
            mPhotoView = view.findViewById(R.id.update_image_view);
            ImageButton clearButton = view.findViewById(R.id.clear_image_button);
            clearButton.setOnClickListener(this);
        }

        private void bind(Image image) {
            if (image == null) {
                return;
            }
            mImage = image;
            Glide.with(mContext).load(mImage.getPath()).error(R.drawable.ic_error).into(mPhotoView);
        }

        @Override
        public void onClick(View v) {
            removeImage(mImage);
        }
    }
}
