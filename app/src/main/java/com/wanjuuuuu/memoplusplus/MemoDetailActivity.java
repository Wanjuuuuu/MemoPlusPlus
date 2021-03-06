package com.wanjuuuuu.memoplusplus;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wanjuuuuu.memoplusplus.databinding.ActivityMemoDetailBinding;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.viewmodels.DetailViewModel;

public class MemoDetailActivity extends BaseActivity<ActivityMemoDetailBinding, DetailViewModel> {

    private static final int REQUEST_CODE_UPDATE = 100;

    private long mMemoId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_memo_detail;
    }

    @Override
    protected Class<DetailViewModel> getViewModel() {
        return com.wanjuuuuu.memoplusplus.viewmodels.DetailViewModel.class;
    }

    @Override
    protected int getBindingVariable() {
        return BR.detailViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.detailMemoRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent == null) {
            if (savedInstanceState != null) {
                return;
            }
            showToast(getString(R.string.toast_load_memo_error));
            finish();
            return;
        }
        Memo memo = intent.getParcelableExtra("memo");
        if (memo == null) {
            showToast(getString(R.string.toast_load_memo_error));
            finish();
            return;
        }
        mMemoId = memo.getId();
        mViewModel.setMemo(memo);
        mViewModel.loadImages(this, memo.getId());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mMemoId = savedInstanceState.getLong("memoId");

        mViewModel.loadMemo(mMemoId);
        mViewModel.loadImages(this, mMemoId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("memoId", mMemoId);

        super.onSaveInstanceState(outState);
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
                bundle.putParcelable("memo", mViewModel.getMemo());
                bundle.putParcelableArrayList("images", mViewModel.getImages());

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
                            mViewModel.deleteMemoAndImages(new DetailViewModel.OnCompleteListener() {
                                @Override
                                public void onRemoved() {
                                    showToast(getString(R.string.toast_delete_memo));
                                    finish();
                                }
                            });
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
                Memo memo = data.getParcelableExtra("memo");
                if (memo == null) {
                    showToast(getString(R.string.toast_load_memo_error));
                    finish();
                    return;
                }
                mViewModel.setMemo(memo);
                mBinding.detailMemoRecyclerView.smoothScrollToPosition(0);
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
