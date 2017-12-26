package com.hohenheim.scancode.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hohenheim.R;
import com.hohenheim.scancode.adapter.HistoryAdapter;
import com.hohenheim.scancode.db.HistoryDBManager;
import com.hohenheim.scancode.event.HistoryEvent;
import com.hohenheim.scancode.modal.ScanResultModal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class HistoryActivity extends ModuleBaseActivity {

    private HistoryDBManager controller;
    private HistoryAdapter adapter;
    private RecyclerView historyList;
    private TextView contentView;

    @Override
    public void setContentView() {
        setContentView(R.layout.scancode_activity_history);
    }

    @Override
    public void findViews() {
        historyList = findViewById(R.id.history_list);
        contentView = findViewById(R.id.content);
    }

    @Override
    public void getData() {
        controller = new HistoryDBManager();
    }

    @Override
    public void showContent() {
        controller.loadHistory();
    }

    @Override
    public int getTitleRes() {
        return R.string.scan_code_history;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HistoryEvent event) {
        if(event != null)
            loadData(event.getList());
    }

    private void loadData(List<ScanResultModal> list) {
        if(list == null || list.isEmpty()) {
            contentView.setVisibility(View.VISIBLE);
            return;
        }
        historyList.setVisibility(View.VISIBLE);
        adapter = new HistoryAdapter(this, list);
        historyList.setAdapter(adapter);
        historyList.setLayoutManager(new LinearLayoutManager(this));
    }
}
