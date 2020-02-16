package com.wanjuuuuu.memoplusplus.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.EditPhotoAdapter;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.utils.FileManager;
import com.wanjuuuuu.memoplusplus.utils.Logger;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;
import com.wanjuuuuu.memoplusplus.views.WrapAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MemoUpdateActivity extends AppCompatActivity {

    private static final String TAG = MemoUpdateActivity.class.getSimpleName();

    private static final String[] PERMISSIONS_FOR_GALLERY = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String[] PERMISSIONS_FOR_CAMERA = {
            Manifest.permission.CAMERA
    };

    private static final int REQUEST_CODE_GALLERY = 0;
    private static final int REQUEST_CODE_CAMERA = 1;

    private RecyclerView mRecyclerView;
    private EditPhotoAdapter mPhotoAdapter;
    private String mPhotoPathFromCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_update);
        mRecyclerView = findViewById(R.id.update_photo_recycler_view);

        // just mock
        List<Image> images = new ArrayList<>();

        mPhotoAdapter = new EditPhotoAdapter(this);
        mPhotoAdapter.insertImages(images);
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
                PermissionManager.request(this, PERMISSIONS_FOR_GALLERY,
                        PermissionManager.REQUEST_CODE_GALLERY);
                break;
            case R.id.photo_from_camera_menu:
                PermissionManager.request(this, PERMISSIONS_FOR_CAMERA,
                        PermissionManager.REQUEST_CODE_CAMERA);
                break;
            case R.id.photo_from_link_menu:
                final WrapAlertDialog alertDialog = new WrapAlertDialog(this);
                alertDialog.show();
                alertDialog.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mPhotoPathFromlinkUrl = alertDialog.getLinkUrl();
                        if (mPhotoPathFromlinkUrl == null || mPhotoPathFromlinkUrl.length() == 0) {
                            showToast(getResources().getString(R.string.toast_dialog_warning));
                            return;
                        }
                        Image image = new Image(mPhotoPathFromlinkUrl);
                        mPhotoAdapter.insertImage(image);
                        alertDialog.dismiss();
                    }
                });
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
            showToast(getResources().getString(R.string.toast_permission_warning));
            return;
        }

        if (requestCode == PermissionManager.REQUEST_CODE_GALLERY) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            if (intent.resolveActivity(getPackageManager()) == null) {
                return;
            }
            startActivityForResult(Intent.createChooser(intent, "안녕"), REQUEST_CODE_GALLERY);
        } else if (requestCode == PermissionManager.REQUEST_CODE_CAMERA) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) == null) {
                return;
            }

            File imageFile = FileManager.createImageFile(this);
            if (imageFile != null) {
                mPhotoPathFromCamera = imageFile.getAbsolutePath();

                Uri photoUri = FileProvider.getUriForFile(this,
                        FileManager.getFileProviderName(this), imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (resultCode == RESULT_OK) {
                if (data == null || data.getData() == null) {
                    return;
                }
                String photoPathForGallery = FileManager.getFilePathFromUri(this, data.getData());
                if (photoPathForGallery == null) {
                    Logger.debug(TAG, "PhotoPath is null");
                    return;
                }
                Image image = new Image(photoPathForGallery);
                mPhotoAdapter.insertImage(image);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                Image image = new Image(mPhotoPathFromCamera);
                mPhotoAdapter.insertImage(image);
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
