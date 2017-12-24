package com.hohenheim.homepage.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hohenheim.R;
import com.hohenheim.common.manager.ItemClickController;
import com.hohenheim.common.fragments.BaseFragment;
import com.hohenheim.common.utils.ResUtils;
import com.hohenheim.homepage.adapter.RecommendAdapter;
import com.hohenheim.homepage.bean.RecommendModal;
import com.hohenheim.homepage.listener.RecommendClickListener;

public class ToolsFragment extends BaseFragment {

    private View parentView;

    private SparseArray<RecommendModal> mRecommendArray = new SparseArray<>();

    public ToolsFragment() {

    }

    public static ToolsFragment newInstance() {
        return new ToolsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] mResIds = getResources().getStringArray(R.array.tool_list);
        for(int i = 0; i < mResIds.length; i ++) {
            String s = mResIds[i];
            RecommendModal modal = new RecommendModal();
            modal.setResId(s);
            modal.setContent(getString(ResUtils.getResId(s, R.string.class)));
            mRecommendArray.append(i, modal);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.homepage_fragment_tools, container, false);
        initView();
        return parentView;
    }

    private void initView() {
        RecyclerView recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new RecommendAdapter(mRecommendArray));
        ItemClickController controller = ItemClickController.addTo(recyclerView);
        RecommendClickListener listener = new RecommendClickListener(mRecommendArray);
        controller.setOnItemClickListener(listener);
        controller.setOnItemLongClickListener(listener);
    }

}
