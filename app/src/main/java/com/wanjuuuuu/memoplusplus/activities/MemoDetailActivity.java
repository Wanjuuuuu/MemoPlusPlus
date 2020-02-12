package com.wanjuuuuu.memoplusplus.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.DetailPhotoAdapter;
import com.wanjuuuuu.memoplusplus.models.Image;

import java.util.ArrayList;
import java.util.List;

public class MemoDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DetailPhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        mRecyclerView = findViewById(R.id.detail_photo_recycler_view);

        // just mock
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            images.add(new Image());
        }

        mPhotoAdapter = new DetailPhotoAdapter(this);
        mPhotoAdapter.addImages(images);
        mRecyclerView.setAdapter(mPhotoAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
