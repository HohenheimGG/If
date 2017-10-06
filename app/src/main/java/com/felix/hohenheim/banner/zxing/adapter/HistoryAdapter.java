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
import java.util.List;

/**
 * Created by hohenheim on 2017/10/1.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private static final int HISTORY_TITLE = 0;
    private static final int HISTORY_ITEM = 1;

    private List<ScanResultModal> list;
    private Context context;

    public HistoryAdapter(Context context, List<ScanResultModal> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case HISTORY_ITEM:
                return HistoryItemViewHolder.newInstance(context, parent);
            case HISTORY_TITLE:
                return HistoryTitleViewHolder.newInstance(context, parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        int titlePosition = getTitleCount(position);
        int itemPosition = position - getTitleCount(position);

        ScanResultModal result = list.get(titlePosition - 1);
        holder.onBindViewHolder(result, itemPosition);
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

    private static class HistoryItemViewHolder extends HistoryViewHolder {

        LinearLayout container;
        TextView hour_min;
        TextView content;

        private HistoryItemViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            hour_min = (TextView) itemView.findViewById(R.id.hour_min);
            content = (TextView)itemView.findViewById(R.id.content);
        }

        private static HistoryItemViewHolder newInstance(Context context, ViewGroup parent) {
            return new HistoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.view_history_item, parent, false));
        }


        @Override
        public void onBindViewHolder(ScanResultModal result, int itemPosition) {
            hour_min.setText(result.getHourToSeconds().get(itemPosition));
            content.setText(result.getContents().get(itemPosition));
        }
    }

    private static class HistoryTitleViewHolder extends HistoryViewHolder {

        TextView title;

        private HistoryTitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
        }

        private static HistoryTitleViewHolder newInstance(Context context, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_history_title, parent, false);
            return new HistoryTitleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ScanResultModal result, int itemPosition) {
            title.setText(result.getYearToDate());
        }
    }

    abstract static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private HistoryViewHolder(View view) {
            super(view);
        }

        protected abstract void onBindViewHolder(ScanResultModal result, int itemPosition);
    }
}
