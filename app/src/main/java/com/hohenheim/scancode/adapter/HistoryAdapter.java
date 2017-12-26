package com.hohenheim.scancode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hohenheim.R;
import com.hohenheim.scancode.modal.ScanResultModal;
import com.hohenheim.scancode.utils.HistoryConstant;

import java.util.List;

/**
 * Created by com.hohenheim on 2017/10/1.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private List<ScanResultModal> list;
    private Context context;

    public HistoryAdapter(Context context, List<ScanResultModal> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case HistoryConstant.HISTORY_ITEM:
                return HistoryItemViewHolder.newInstance(context, parent);
            case HistoryConstant.HISTORY_TITLE:
                return HistoryTitleViewHolder.newInstance(context, parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        ScanResultModal result = list.get(position);
        holder.onBindViewHolder(result, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        ScanResultModal result = list.get(position);
        return result.getType();
    }

    private static class HistoryItemViewHolder extends HistoryViewHolder {

        LinearLayout container;
        TextView hour_min;
        TextView content;

        private HistoryItemViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            hour_min = itemView.findViewById(R.id.hour_min);
            content = itemView.findViewById(R.id.content);
        }

        private static HistoryItemViewHolder newInstance(Context context, ViewGroup parent) {
            return new HistoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.scancode_view_history_item, parent, false));
        }


        @Override
        public void onBindViewHolder(ScanResultModal result, int itemPosition) {
            hour_min.setText(result.getHourToSecond());
            content.setText(result.getContent());
        }
    }

    private static class HistoryTitleViewHolder extends HistoryViewHolder {

        TextView title;

        private HistoryTitleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        private static HistoryTitleViewHolder newInstance(Context context, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.scancode_view_history_title, parent, false);
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
