package com.hohenheim.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hohenheim.R;
import com.hohenheim.homepage.bean.RecommendModal;

/**
 * Created by hohenheim on 2017/12/9.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder>{

    private SparseArray<RecommendModal> recommendArray;

    public RecommendAdapter(SparseArray<RecommendModal> recommendArray) {
        this.recommendArray = recommendArray;
    }

    @Override
    public RecommendAdapter.RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendAdapter.RecommendViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.homepage_view_tool, null));
    }

    @Override
    public void onBindViewHolder(RecommendAdapter.RecommendViewHolder holder, int position) {
        holder.tvContent.setText(recommendArray.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return recommendArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class RecommendViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        private RecommendViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView)itemView.findViewById(R.id.tv_tool_content);
        }
    }
}
