package com.wanjuuuuu.memoplusplus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.PreviewMemoAdapter;
import com.wanjuuuuu.memoplusplus.databinding.ActivityMemoPreviewBinding;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;
import com.wanjuuuuu.memoplusplus.viewmodels.PreviewViewModel;

import java.util.List;

public class MemoPreviewActivity extends AppCompatActivity {

    private PreviewMemoAdapter mMemoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMemoPreviewBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_memo_preview);

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
        binding.previewMemoRecyclerView.setAdapter(mMemoAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.previewMemoRecyclerView.setLayoutManager(layoutManager);

        PreviewViewModel viewModel = new ViewModelProvider(this).get(PreviewViewModel.class);
        viewModel.getAllMemos(this).observe(this, new Observer<List<MemoWithFirstImage>>() {
            @Override
            public void onChanged(List<MemoWithFirstImage> memoWithFirstImages) {
                int previousItemCount = mMemoAdapter.getItemCount();
                binding.setMemos(memoWithFirstImages);
                // memo is added or updated
                int currentItemCount = mMemoAdapter.getItemCount();
                if (currentItemCount > 0 && currentItemCount >= previousItemCount) {
                    binding.previewMemoRecyclerView.smoothScrollToPosition(currentItemCount - 1);
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
