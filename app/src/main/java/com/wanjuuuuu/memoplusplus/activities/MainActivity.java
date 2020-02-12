package com.wanjuuuuu.memoplusplus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.wanjuuuuu.memoplusplus.R;
import com.wanjuuuuu.memoplusplus.adapters.MemoAdapter;
import com.wanjuuuuu.memoplusplus.models.Memo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MemoAdapter mMemoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
