package com.hohenheim.homepage.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hohenheim.R;
import com.hohenheim.common.fragments.BaseFragment;

public class RecommendFragment extends BaseFragment {

    private RecyclerView mRecentRV;
    private View mParenTV;
    private SparseArray<String> contents = new SparseArray<>();

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

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
