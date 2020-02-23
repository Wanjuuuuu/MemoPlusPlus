package com.wanjuuuuu.memoplusplus.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.DetailMemoAdapter;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.ImageDao;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.models.MemoDao;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;
import com.wanjuuuuu.memoplusplus.utils.Constant;
import com.wanjuuuuu.memoplusplus.utils.Logger;

import java.util.ArrayList;

public class MemoDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_UPDATE = 102;

    private RecyclerView mRecyclerView;
    private DetailMemoAdapter mMemoAdapter;

    private MemoDao mMemoDao;
    private ImageDao mImageDao;
    private Memo mMemo;
    private ArrayList<Image> mImages;
    private boolean mIsUpdated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        mRecyclerView = findViewById(R.id.detail_photo_recycler_view);

        mMemoAdapter = new DetailMemoAdapter(this);
        mRecyclerView.setAdapter(mMemoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent == null) {
            showToast(getString(R.string.toast_load_memo_error));
            finish();
            return;
        }
        mMemo = intent.getParcelableExtra("memo");
        if (mMemo == null) {
            showToast(getString(R.string.toast_load_memo_error));
            finish();
            return;
        }
        Logger.debug("MemoDetailActitivy", " " + mMemo.getModifyTimestamp());

        // get images from database
        MemoPlusDatabase database = MemoPlusDatabase.getInstance(this);
        mMemoDao = database.memoDao();
        mImageDao = database.imageDao();
        mImages = new ArrayList<>(mImageDao.getImages(mMemo.getId()));

        updateMemoDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_modify_menu:
                Bundle bundle = new Bundle();
                bundle.putParcelable("memo", mMemo);
                bundle.putParcelableArrayList("images", mImages);

                Intent intent = new Intent(this, MemoUpdateActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_UPDATE);
                break;
            case R.id.detail_delete_menu:
                final AlertDialog dialog = createDialog();
                dialog.show();

                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (button != null) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            mImageDao.deleteImages(mImages);
                            mMemoDao.deleteMemo(mMemo);

                            Intent intent = new Intent();
                            intent.putExtra("memoid", mMemo.getId());
                            setResult(Constant.ResultCodes.DELETED, intent);
                            finish();
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode == Constant.ResultCodes.UPDATED) {
                if (data == null) {
                    return;
                }
                long memoId = data.getLongExtra("memoid", 0);
                if (memoId == 0) {
                    showToast(getString(R.string.toast_load_memo_error));
                    setResult(Constant.ResultCodes.FAILED);
                    finish();
                    return;
                }

                mMemo = mMemoDao.getMemo(memoId);
                mImages = new ArrayList<>(mImageDao.getImages(memoId));

                updateMemoDetail();
                mIsUpdated = true;
            } else if (resultCode == Constant.ResultCodes.FAILED) {
                setResult(Constant.ResultCodes.FAILED);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("memoid", mMemo.getId());
        if (mIsUpdated) {
            setResult(Constant.ResultCodes.UPDATED, intent);
        } else {
            setResult(Constant.ResultCodes.CANCELLED);
        }
        finish();
    }

    private void updateMemoDetail() {
        mMemoAdapter.setItems(mMemo, mImages);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.dialog_delete_description));
        dialogBuilder.setPositiveButton(getString(R.string.dialog_delete_button),
                null);
        dialogBuilder.setNegativeButton(getString(R.string.dialog_cancel_button),
                null);
        return dialogBuilder.create();
    }
}
