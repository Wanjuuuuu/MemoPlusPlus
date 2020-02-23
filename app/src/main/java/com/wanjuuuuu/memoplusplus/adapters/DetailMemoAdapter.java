package com.wanjuuuuu.memoplusplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DetailMemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Seoul");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 " +
            "mm분");
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTO = 1;

    private Context mContext;
    private Memo mMemo;
    private List<Image> mImageList;

    public DetailMemoAdapter(Context context) {
        mContext = context;
        mImageList = new ArrayList<>();
    }

    public void setItems(Memo memo, List<Image> images) {
        if (memo != null) {
            mMemo = memo;
        }

        mImageList.clear();
        if (images != null && !images.isEmpty()) {
            mImageList.addAll(images);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.detail_header_view, parent, false));
        }
        return new PhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.detail_photo_view,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).bind(mMemo);
        } else if (holder instanceof PhotoHolder) {
            Image image = mImageList.get(position - 1);
            ((PhotoHolder) holder).bind(image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == TYPE_HEADER) ? TYPE_HEADER : TYPE_PHOTO;
    }

    @Override
    public int getItemCount() {
        return mImageList.size() + 1;
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

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
                    .error(mContext.getDrawable(R.drawable.ic_fallback)).into(mPhotoView);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView mDateView;
        private TextView mTitleView;
        private TextView mContentView;

        private HeaderHolder(View view) {
            super(view);
            mDateView = view.findViewById(R.id.detail_modifyDatetime_text_view);
            mTitleView = view.findViewById(R.id.detail_title_text_view);
            mContentView = view.findViewById(R.id.detail_content_text_view);
        }

        private void bind(Memo memo) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TIME_ZONE);
            calendar.setTimeInMillis(memo.getModifyTimestamp());

            String modifyDatetime = DATE_FORMAT.format(calendar.getTime());
            mDateView.setText(modifyDatetime);
            mTitleView.setText(memo.getTitle());
            mContentView.setText(memo.getContent());
        }
    }
}
