package com.wanjuuuuu.memoplusplus.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.DetailMemoAdapter;
import com.wanjuuuuu.memoplusplus.models.DatabaseManager;
import com.wanjuuuuu.memoplusplus.models.Image;
import com.wanjuuuuu.memoplusplus.models.ImageDao;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.models.MemoDao;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;

import java.util.ArrayList;
import java.util.List;

public class MemoDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_UPDATE = 100;

    private RecyclerView mRecyclerView;
    private DetailMemoAdapter mMemoAdapter;
    private MemoDao mMemoDao;
    private ImageDao mImageDao;
    private Memo mMemo;
    private ArrayList<Image> mImages;

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
        mMemoAdapter.setMemo(mMemo);

        // get images from database
        MemoPlusDatabase database = MemoPlusDatabase.getInstance(this);
        mMemoDao = database.memoDao();
        mImageDao = database.imageDao();
        mImageDao.getImages(mMemo.getId()).observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                mImages = new ArrayList<>(images);
                mMemoAdapter.setImages(mImages);
            }
        });
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
                            removeMemoAndFinishActivity();
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
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                mMemo = data.getParcelableExtra("memo");
                if (mMemo == null) {
                    showToast(getString(R.string.toast_load_memo_error));
                    finish();
                    return;
                }
                mMemoAdapter.setMemo(mMemo);
                mRecyclerView.smoothScrollToPosition(0);
            }
        }
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

    private void removeMemoAndFinishActivity() {
        DatabaseManager.executeTransaction(new Runnable() {
            @Override
            public void run() {
                mImageDao.deleteImages(mImages);
                mMemoDao.deleteMemo(mMemo);
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(getString(R.string.toast_delete_memo));
                        finish();
                    }
                });
            }
        });
    }
}
