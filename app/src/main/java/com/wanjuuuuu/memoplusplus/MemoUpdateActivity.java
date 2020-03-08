package com.wanjuuuuu.memoplusplus;

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
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wanjuuuuu.memoplusplus.adapters.UpdateMemoAdapter;
import com.wanjuuuuu.memoplusplus.databinding.ActivityMemoUpdateBinding;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.utils.FileManager;
import com.wanjuuuuu.memoplusplus.utils.OnCompleteListener;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;
import com.wanjuuuuu.memoplusplus.viewmodels.UpdateViewModel;
import com.wanjuuuuu.memoplusplus.views.LinkInputDialog;

import java.io.File;
import java.util.List;

public class MemoUpdateActivity extends BaseActivity<ActivityMemoUpdateBinding, UpdateViewModel> {

    private static final String[] PERMISSIONS_FOR_GALLERY = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String[] PERMISSIONS_FOR_CAMERA = {
            Manifest.permission.CAMERA
    };

    private static final int REQUEST_CODE_GALLERY = 101;
    private static final int REQUEST_CODE_CAMERA = 102;

    private UpdateMemoAdapter mMemoAdapter;
    private Memo mMemo;
    private String mPhotoPathFromCamera;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_memo_update;
    }

    @Override
    protected Class<UpdateViewModel> getViewModel() {
        return UpdateViewModel.class;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMemoAdapter = new UpdateMemoAdapter();
        mMemoAdapter.setOnRemoveListener(new UpdateMemoAdapter.OnRemoveListener() {
            @Override
            public void onRemove(Image image) {
                removeImage(image);
            }
        });
        mBinding.updateMemoRecyclerView.setAdapter(mMemoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.updateMemoRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        // to update memo
        Bundle bundle = intent.getExtras();
        mMemo = bundle.getParcelable("memo");
        if (mMemo == null) {
            showToast(getString(R.string.toast_update_memo_error));
            finish();
            return;
        }
        mBinding.setMemo(mMemo);

        List<Image> images = bundle.getParcelableArrayList("images");
        mBinding.setImages(images);
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
                        String mPhotoPathFromLinkUrl = inputDialog.getLinkUrl();
                        if (mPhotoPathFromLinkUrl == null || mPhotoPathFromLinkUrl.length() == 0) {
                            showToast(getString(R.string.toast_dialog_warning));
                            return;
                        }
                        if (!PermissionManager.isInternetConnected(MemoUpdateActivity.this)) {
                            showToast(getString(R.string.toast_dialog_internet_warning));
                            return;
                        }
                        if (!URLUtil.isNetworkUrl(mPhotoPathFromLinkUrl)) {
                            showToast(getString(R.string.toast_dialog_scheme_warning));
                            return;
                        }
                        addImage(mPhotoPathFromLinkUrl);
                        inputDialog.dismiss();
                    }
                });
                break;
            case R.id.update_complete_menu:
                if (!isContentFilled()) {
                    showToast(getString(R.string.toast_text_warning));
                    return false;
                }
                saveMemoAndFinishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (PermissionManager.isDenied(grantResults)) {
            showToast(getString(R.string.toast_permission_warning));
            return;
        }

        if (requestCode == PermissionManager.REQUEST_CODE_GALLERY) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            if (intent.resolveActivity(getPackageManager()) == null) {
                return;
            }
            startActivityForResult(Intent.createChooser(intent,
                    getString(R.string.photo_from_gallery)), REQUEST_CODE_GALLERY);
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
                    showToast(getString(R.string.toast_load_photo_error));
                    return;
                }
                addImage(photoPathForGallery);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                addImage(mPhotoPathFromCamera);
            }
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog dialog = createDialog();
        dialog.show();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.dialog_back_description));
        dialogBuilder.setPositiveButton(getString(R.string.dialog_back_button), null);
        dialogBuilder.setNegativeButton(getString(R.string.dialog_negative_button), null);
        return dialogBuilder.create();
    }

    private void addImage(String imagePath) {
        Image image = new Image(0, 0, imagePath);
        mViewModel.addImage(image);
        mMemoAdapter.addImage(image);
        mBinding.updateMemoRecyclerView.smoothScrollToPosition(mMemoAdapter.getItemCount() - 1);
    }

    private void removeImage(Image image) {
        mViewModel.deleteImage(image);
        mMemoAdapter.removeImage(image);
    }

    private boolean isContentFilled() {
        return mMemoAdapter.isContentFilled();
    }

    private void saveMemoAndFinishActivity() {
        final boolean isUpdated = (mMemo != null);
        createMemo();

        mViewModel.updateMemoAndImages(mMemo, new OnCompleteListener() {
            @Override
            public void onComplete() {
                if (isUpdated) {
                    Intent intent = new Intent();
                    intent.putExtra("memo", mMemo);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    private void createMemo() {
        String title = mMemoAdapter.getTitle();
        String content = mMemoAdapter.getContent();
        long timestamp = System.currentTimeMillis();

        long memoId;
        if (mMemo == null) {
            mMemo = new Memo(0, title, content, timestamp);
        } else {
            memoId = mMemo.getId();
            mMemo = new Memo(memoId, title, content, timestamp);
        }
    }
}
