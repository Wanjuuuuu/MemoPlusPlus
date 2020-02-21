package com.wanjuuuuu.memoplusplus.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.MemoAdapter;
import com.wanjuuuuu.memoplusplus.models.MemoDao;
import com.wanjuuuuu.memoplusplus.models.MemoPlusDatabase;
import com.wanjuuuuu.memoplusplus.models.MemoWithFirstImage;
import com.wanjuuuuu.memoplusplus.utils.Constant;
import com.wanjuuuuu.memoplusplus.utils.Logger;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_DETAIL = 100;
    private static final int REQUEST_CODE_ADD = 101;

    private RecyclerView mRecyclerView;
    private MemoAdapter mMemoAdapter;

    private MemoDao mMemoDao;
    private MemoWithFirstImage mMemoClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.preview_memo_recycler_view);

        // request necessary Permissions to use app
        PermissionManager.requestAll(this);

        // get memos from database
        mMemoDao = MemoPlusDatabase.getInstance(this).memoDao();
        List<MemoWithFirstImage> memos = mMemoDao.getAllMemoWithFirstImage();

        mMemoAdapter = new MemoAdapter(this);
        mMemoAdapter.setOnClickListener(new MemoAdapter.OnClickListener() {
            @Override
            public void onClick(MemoWithFirstImage memo) {
                if (memo == null || memo.getMemo() == null) {
                    return;
                }
                mMemoClicked = memo;
                Intent intent = new Intent(MainActivity.this, MemoDetailActivity.class);
                intent.putExtra("memo", memo.getMemo());
                startActivityForResult(intent, REQUEST_CODE_DETAIL);
            }
        });
        mMemoAdapter.initMemos(memos);
        mRecyclerView.setAdapter(mMemoAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
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
            startActivityForResult(intent, REQUEST_CODE_ADD);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MemoWithFirstImage newMemo = null;
        if (data != null) {
            long memoId = data.getLongExtra("memoid", 0);
            newMemo = mMemoDao.getMemoWithFristImage(memoId);
        }

        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == Constant.ResultCodes.ADDED) {
                if (newMemo != null) {
                    mMemoAdapter.addMemo(newMemo);
                }
            }
        } else if (requestCode == REQUEST_CODE_DETAIL) {
            if (mMemoClicked == null) {
                return;
            }
            if (resultCode == Constant.ResultCodes.DELETED) {
                mMemoAdapter.removeMemo(mMemoClicked);
            } else if (resultCode == Constant.ResultCodes.UPDATED) {
                mMemoAdapter.removeMemo(mMemoClicked);
                if (newMemo != null) {
                    mMemoAdapter.addMemo(newMemo);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (PermissionManager.isDenied(grantResults)) {
            Toast.makeText(this, getResources().getString(R.string.toast_all_permission_warning),
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
