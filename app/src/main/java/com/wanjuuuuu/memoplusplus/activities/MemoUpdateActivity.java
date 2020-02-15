package com.wanjuuuuu.memoplusplus.activities;

import android.Manifest;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.EditPhotoAdapter;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoUpdateActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditPhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_update);
        mRecyclerView = findViewById(R.id.update_photo_recycler_view);

        // just mock
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            images.add(new Image());
        }

        mPhotoAdapter = new EditPhotoAdapter(this);
        mPhotoAdapter.addImages(images);
        mRecyclerView.setAdapter(mPhotoAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_from_gallery_menu:
                String[] permissionForGallery = {Manifest.permission.READ_EXTERNAL_STORAGE};
                break;
            case R.id.photo_from_camera_menu:
                String[] permissionForCamera = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA};
                break;
            case R.id.photo_from_link_menu:
                break;
            case R.id.update_complete_menu:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (PermissionManager.isDenied(grantResults)) {
            Toast.makeText(this, getResources().getString(R.string.toast_permission_warning),
                    Toast.LENGTH_LONG).show();
            return;
        }

    }
}
