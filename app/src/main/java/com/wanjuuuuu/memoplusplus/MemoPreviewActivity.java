package com.wanjuuuuu.memoplusplus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wanjuuuuu.memoplusplus.adapters.PreviewMemoAdapter;
import com.wanjuuuuu.memoplusplus.databinding.ActivityMemoPreviewBinding;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;
import com.wanjuuuuu.memoplusplus.viewmodels.PreviewViewModel;

public class MemoPreviewActivity extends BaseActivity<ActivityMemoPreviewBinding,
        PreviewViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_memo_preview;
    }

    @Override
    protected Class<PreviewViewModel> getViewModel() {
        return PreviewViewModel.class;
    }

    @Override
    protected int getBindingVariable() {
        return BR.previewViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // request necessary Permissions to use app
        PermissionManager.requestAll(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mBinding.previewMemoRecyclerView.setLayoutManager(layoutManager);

        mViewModel.getAdapter().setOnItemClickListener(new PreviewMemoAdapter.OnItemClickListener() {
            @Override
            public void onClick(MemoWithFirstImage memo) {
                if (memo == null || memo.getMemo() == null) {
                    return;
                }
                Intent intent = new Intent(MemoPreviewActivity.this, MemoDetailActivity.class);
                intent.putExtra("memo", memo.getMemo());
                startActivity(intent);
            }
        });
        mViewModel.setUpdateListener(new PreviewViewModel.OnUpdateListener() {
            @Override
            public void onUpdated(int itemCount) {
                mBinding.previewMemoRecyclerView.smoothScrollToPosition(itemCount - 1);
            }
        });
        mViewModel.loadAllMemos(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_memo_menu) {
            Intent intent = new Intent(this, MemoUpdateActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (PermissionManager.isDenied(grantResults)) {
            Toast.makeText(this, getString(R.string.toast_all_permission_warning),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
