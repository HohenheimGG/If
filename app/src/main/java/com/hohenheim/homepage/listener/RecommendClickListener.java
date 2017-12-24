package com.hohenheim.homepage.listener;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.hohenheim.R;
import com.hohenheim.common.manager.ItemClickController;
import com.hohenheim.common.utils.ResUtils;
import com.hohenheim.homepage.bean.RecommendModal;

/**
 * Created by hohenheim on 2017/12/10.
 */

public class RecommendClickListener
        implements ItemClickController.OnItemClickListener,
        ItemClickController.OnItemLongClickListener {

    private SparseArray<RecommendModal> mRecommendArray;

    public RecommendClickListener(SparseArray<RecommendModal> mRecommendArray) {
        this.mRecommendArray = mRecommendArray;
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, View v) {
        Context context = recyclerView.getContext();
        int position = recyclerView.getChildAdapterPosition(v);
        String resId = "action_" + mRecommendArray.get(position).getResId();
        Intent intent = new Intent(context.getString(ResUtils.getResId(resId, R.string.class)));
        ComponentName name = intent.resolveActivity(context.getPackageManager());
//        if(name != null)
            context.startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, View v) {
        return false;
    }
}
