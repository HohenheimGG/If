package com.felix.hohenheim.banner.zxing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felix.hohenheim.banner.R;
import com.felix.hohenheim.banner.zxing.modal.ScanResultModal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hohenheim on 2017/10/1.
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int HISTORY_TITLE = 0;
    private static final int HISTORY_ITEM = 1;

    private List<ScanResultModal> list;
    private Context context;

    public HistoryAdapter(Context context, List<ScanResultModal> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case HISTORY_ITEM:
                return new HistoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.view_history_item, parent, false));
            case HISTORY_TITLE:
                return new HistoryTitleViewHolder(LayoutInflater.from(context).inflate(R.layout.view_history_title, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int titlePosition = getTitleCount(position);
        int itemPosition = position - getTitleCount(position);

        ScanResultModal result = list.get(titlePosition - 1);

        if(holder instanceof HistoryTitleViewHolder) {
            HistoryTitleViewHolder titleHolder = (HistoryTitleViewHolder)holder;
            titleHolder.title.setText(result.getYearToDate());
            return;
        }
        HistoryItemViewHolder itemHolder = (HistoryItemViewHolder)holder;
        itemHolder.hour_min.setText(result.getHourToSeconds().get(itemPosition));
        itemHolder.content.setText(result.getContents().get(itemPosition));
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for(ScanResultModal result: list)
            count += result.getContents().size();
        count += list.size();
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        for(ScanResultModal result: list) {
            position -=1;
            if(position > -1)
                position -= result.getContents().size();
            else if(position == -1)
                return HISTORY_TITLE;
            else
                return HISTORY_ITEM;
        }
        return HISTORY_ITEM;
    }

    private int getTitleCount(int position) {
        int count = 0;
        for(ScanResultModal result: list) {
            position -= 1;
            if(position >= -1) {
                count ++;
                position -= result.getContents().size();
            } else
                break;
        }
        return count;
    }

    private static class HistoryItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        TextView hour_min;
        TextView content;

        private HistoryItemViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            hour_min = (TextView) itemView.findViewById(R.id.hour_min);
            content = (TextView)itemView.findViewById(R.id.content);
        }
    }

    private static class HistoryTitleViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        private HistoryTitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
