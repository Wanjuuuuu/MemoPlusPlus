package com.wanjuuuuu.memoplusplus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.MemoAdapter;
import com.wanjuuuuu.memoplusplus.models.Memo;
import com.wanjuuuuu.memoplusplus.utils.PermissionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MemoAdapter mMemoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // request necessary Permissions to use app
        PermissionManager.requestAll(this);

        mRecyclerView = findViewById(R.id.preview_memo_recycler_view);

        // just mock
        String mockText = getResources().getString(R.string.mock_text);
        List<Memo> memos = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            memos.add(new Memo(mockText, mockText, null));
        }

        mMemoAdapter = new MemoAdapter(this);
        mMemoAdapter.addMemos(memos);
        mRecyclerView.setAdapter(mMemoAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
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

        }
        return super.onOptionsItemSelected(item);
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
