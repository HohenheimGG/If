package com.felix.hohenheim.banner.zxing.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felix.hohenheim.banner.R;
import com.felix.hohenheim.banner.zxing.adapter.HistoryAdapter;
import com.felix.hohenheim.banner.zxing.db.DBController;
import com.felix.hohenheim.banner.zxing.modal.ScanResultModal;
import com.felix.hohenheim.banner.zxing.utils.HistoryConstant;

import java.lang.ref.WeakReference;
import java.util.List;

public class HistoryActivity extends Activity {

    private DBController controller;
    private HistoryAdapter adapter;
    private RecyclerView historyList;
    private TextView contentView;

    private Handler mHandler = new Handler(new Handler.Callback() {
        WeakReference<HistoryActivity> reference = new WeakReference<>(HistoryActivity.this);

        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what) {
                case HistoryConstant.HANDLER_LOAD_HISTORY:
                    if(reference.get() != null)
                        reference.get().loadData((List<ScanResultModal>)msg.obj);
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        getData();
    }

    private void initView() {
        RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        TextView title = (TextView)titleLayout.findViewById(R.id.tv_title_text);
        title.setVisibility(View.VISIBLE);
        title.setText("历史记录");


        historyList = (RecyclerView) findViewById(R.id.history_list);
        contentView = (TextView) findViewById(R.id.content);
    }

    private void getData() {
        controller = new DBController(mHandler);
        controller.loadHistory();
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

    public void backClickEevent(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
