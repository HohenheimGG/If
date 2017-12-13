package com.hohenheim.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hohenheim.R;

import java.util.List;

/**
 * Created by hohenheim on 2017/12/13.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentViewHolder> {

    private SparseArray<String> contents;

    public RecentAdapter(SparseArray<String> contents) {
        this.contents = contents;
    }

    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.homepage_view_recent, null));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecentViewHolder holder, int position) {
        holder.content.setText(contents.get(position));
    }

    @Override
    public int getItemCount() {
        return this.contents.size();
    }

    static class RecentViewHolder extends RecyclerView.ViewHolder {

        private TextView content;

        public RecentViewHolder(View itemView) {
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.tv_tool_content);
        }
    }
}
