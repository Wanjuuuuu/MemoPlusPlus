package com.wanjuuuuu.memoplusplus.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.EditPhotoAdapter;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.ImageDao;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.models.MemoDao;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;
import com.wanjuuuuu.memoplusplus.utils.Constant;
import com.wanjuuuuu.memoplusplus.utils.FileManager;
import com.wanjuuuuu.memoplusplus.utils.Logger;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;
import com.wanjuuuuu.memoplusplus.views.ClearEditText;
import com.wanjuuuuu.memoplusplus.views.LinkInputDialog;

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

    private static final int REQUEST_CODE_GALLERY = 103;
    private static final int REQUEST_CODE_CAMERA = 104;

    private RecyclerView mRecyclerView;
    private EditPhotoAdapter mPhotoAdapter;
    private ClearEditText mTitleEditText;
    private ClearEditText mContentEditText;

    private MemoDao mMemoDao;
    private ImageDao mImageDao;
    private Memo mMemo;
    private List<Image> mImagesInserted;
    private List<Image> mImagesDeleted;
    private String mPhotoPathFromCamera;
    private long mMemoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_update);
        mRecyclerView = findViewById(R.id.update_photo_recycler_view);
        mTitleEditText = findViewById(R.id.update_title_edit_text);
        mContentEditText = findViewById(R.id.update_content_edit_text);

        mPhotoAdapter = new EditPhotoAdapter(this);
        mPhotoAdapter.setOnRemoveListener(new EditPhotoAdapter.OnRemoveListener() {
            @Override
            public void onRemove(Image image) {
                removeImage(image);
            }
        });
        mRecyclerView.setAdapter(mPhotoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        MemoPlusDatabase database = MemoPlusDatabase.getInstance(this);
        mMemoDao = database.memoDao();
        mImageDao = database.imageDao();
        mImagesInserted = new ArrayList<>();
        mImagesDeleted = new ArrayList<>();

        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        // to update memo
        Bundle bundle = intent.getExtras();
        mMemo = bundle.getParcelable("memo");
        if (mMemo == null) {
            showToast(getResources().getString(R.string.toast_update_memo_error));
            setResult(Constant.ResultCodes.FAILED);
            finish();
            return;
        }
        mTitleEditText.setText(mMemo.getTitle());
        mContentEditText.setText(mMemo.getContent());

        List<Image> images = bundle.getParcelableArrayList("images");
        mPhotoAdapter.initImages(images);
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
                final LinkInputDialog inputDialog = new LinkInputDialog(this);
                inputDialog.show();
                inputDialog.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mPhotoPathFromlinkUrl = inputDialog.getLinkUrl();
                        if (mPhotoPathFromlinkUrl == null || mPhotoPathFromlinkUrl.length() == 0) {
                            showToast(getResources().getString(R.string.toast_dialog_warning));
                            return;
                        }
                        Image image = new Image(0, 0, mPhotoPathFromlinkUrl);
                        addImage(image);
                        inputDialog.dismiss();
                    }
                });
                break;
            case R.id.update_complete_menu:
                if (!isContentFilled()) {
                    showToast(getResources().getString(R.string.toast_text_warning));
                    return false;
                }
                boolean isAddition = false;
                if (mMemo == null) {
                    isAddition = true;
                }
                saveDataOnDatabase();

                Intent intent = new Intent();
                intent.putExtra("memoid", mMemoId);
                if (isAddition) {
                    setResult(Constant.ResultCodes.ADDED, intent);
                } else {
                    setResult(Constant.ResultCodes.UPDATED, intent);
                }
                finish();
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
                Image image = new Image(0, 0, photoPathForGallery);
                addImage(image);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                Image image = new Image(0, 0, mPhotoPathFromCamera);
                addImage(image);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = createDialog();
        dialog.show();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Constant.ResultCodes.CANCELLED);
                    finish();
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.dialog_back_description));
        dialogBuilder.setPositiveButton(getResources().getString(R.string.dialog_back_button),
                null);
        dialogBuilder.setNegativeButton(getResources().getString(R.string.dialog_negative_button)
                , null);
        return dialogBuilder.create();
    }

    private void addImage(Image image) {
        mImagesInserted.add(image);
        mPhotoAdapter.addImage(image);
    }

    private void removeImage(Image image) {
        if (image.getImageId() == 0) {
            mImagesInserted.remove(image);
        } else {
            mImagesDeleted.add(image);
        }
        mPhotoAdapter.removeImage(image);
    }

    private boolean isContentFilled() {
        if (mTitleEditText.getText() == null || mContentEditText.getText() == null) {
            return false;
        }
        String title = mTitleEditText.getText().toString();
        String content= mContentEditText.getText().toString();
        return (title.length() > 0 || content.length() > 0);
    }

    private void saveDataOnDatabase() {
        String title = mTitleEditText.getText().toString();
        String content = mContentEditText.getText().toString();
        long timestamp = System.currentTimeMillis();

        if (mMemo == null) {
            mMemo = new Memo(0, title, content, timestamp);
            mMemoId = mMemoDao.insertMemo(mMemo);
        } else {
            mMemoId = mMemo.getId();
            mMemo = new Memo(mMemoId, title, content, timestamp);
            mMemoDao.updateMemo(mMemo);
        }

        for (Image image : mImagesInserted) {
            if (image != null) {
                image.setMemoId(mMemoId);
            }
        }
        mImageDao.insertImages(mImagesInserted);
        mImageDao.deleteImages(mImagesDeleted);
    }
}
