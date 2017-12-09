package com.hohenheim.homepage.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.hohenheim.R;
import com.hohenheim.common.controller.ItemClickController;

/**
 * Created by hohenheim on 2017/12/10.
 */

public class RecommendClickListener
        implements ItemClickController.OnItemClickListener,
        ItemClickController.OnItemLongClickListener {

    public RecommendClickListener() {
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        Context context = recyclerView.getContext();

    }

    @Override
    public boolean onItemLongClicked(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, View v) {
        return false;
    }
}
