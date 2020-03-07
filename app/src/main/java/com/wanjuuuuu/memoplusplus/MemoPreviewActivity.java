package com.wanjuuuuu.memoplusplus;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
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

import java.util.List;

public class MemoPreviewActivity extends BaseActivity<ActivityMemoPreviewBinding,
        PreviewViewModel> {

    private PreviewMemoAdapter mMemoAdapter;

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
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // request necessary Permissions to use app
        PermissionManager.requestAll(this);

        mMemoAdapter = new PreviewMemoAdapter(this);
        mMemoAdapter.setOnClickListener(new PreviewMemoAdapter.OnClickListener() {
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
        mBinding.previewMemoRecyclerView.setAdapter(mMemoAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mBinding.previewMemoRecyclerView.setLayoutManager(layoutManager);

        mViewModel.getAllMemos(this).observe(this, new Observer<List<MemoWithFirstImage>>() {
            @Override
            public void onChanged(List<MemoWithFirstImage> memoWithFirstImages) {
                int previousItemCount = mMemoAdapter.getItemCount();
                mBinding.setMemos(memoWithFirstImages);
                // memo is added or updated
                int currentItemCount = mMemoAdapter.getItemCount();
                if (currentItemCount > 0 && currentItemCount >= previousItemCount) {
                    mBinding.previewMemoRecyclerView.smoothScrollToPosition(currentItemCount - 1);
                }
            }
        });
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
