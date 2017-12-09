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
import com.hohenheim.common.fragments.BaseFragment;
import com.hohenheim.homepage.listener.RecommendClickListener;
import com.hohenheim.homepage.adapter.RecommendAdapter;
import com.hohenheim.common.controller.ItemClickController;

public class RecommendFragment extends BaseFragment {

    private View parentView;
    private SparseArray<String> strings = new SparseArray<>();

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] temp = getResources().getStringArray(R.array.tool_list);
        for(int i = 0; i < temp.length; i ++)
            strings.append(i, temp[i]);
        parentView = inflater.inflate(R.layout.fragment_recommend, container, false);
        initView();
        return parentView;
    }

    private void initView() {
        RecyclerView recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new RecommendAdapter(strings));
        ItemClickController controller = ItemClickController.addTo(recyclerView);
        RecommendClickListener listener = new RecommendClickListener();
        controller.setOnItemClickListener(listener);
        controller.setOnItemLongClickListener(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
