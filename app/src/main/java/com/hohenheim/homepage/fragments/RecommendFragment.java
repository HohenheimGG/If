package com.hohenheim.homepage.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hohenheim.R;
import com.hohenheim.common.controller.ItemClickController;
import com.hohenheim.common.fragments.BaseFragment;
import com.hohenheim.common.view.popupmenu.MenuBaseAdapter;
import com.hohenheim.common.view.popupmenu.PopupMenu;
import com.hohenheim.homepage.adapter.RecentAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends BaseFragment {

    private RecyclerView mRecentRV;
    private View mParenTV;
    private SparseArrayCompat<String> contents = new SparseArrayCompat<>();

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParenTV = inflater.inflate(R.layout.homepage_fragment_recommend, container, false);
        mRecentRV = (RecyclerView) mParenTV.findViewById(R.id.homepage_recent);
        return mParenTV;
    }

    @Override
    public void onActivityCreated(Bundle onSaveInstanceState) {
        super.onActivityCreated(onSaveInstanceState);
        contents.append(0, "收费业务");
        RecentAdapter adapter = new RecentAdapter(contents);
        mRecentRV.setAdapter(adapter);
        mRecentRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemClickController controller = ItemClickController.addTo(mRecentRV);
        controller.setOnItemClickListener(new ItemClickController.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, View v) {
                PopupMenu popupMenu = new PopupMenu();

                SparseArrayCompat<String> list = new SparseArrayCompat<>();
                list.append(0, "中信关心");
                list.append(1, "理财管理");

                MenuBaseAdapter.DefaultMenuAdapter menuAdapter = new MenuBaseAdapter.DefaultMenuAdapter(getActivity(), list);
                popupMenu.build(menuAdapter).showWithWindowAlphaChange(v, getActivity().getWindow());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
