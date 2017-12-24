package com.hohenheim.homepage.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hohenheim.R;
import com.hohenheim.common.manager.ItemClickController;
import com.hohenheim.common.fragments.BaseFragment;
import com.hohenheim.homepage.adapter.RecentAdapter;
import com.hohenheim.homepage.event.AddToRecentEvent;
import com.hohenheim.homepage.event.DBRecentListEvent;
import com.hohenheim.homepage.listener.RecentClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;

public class RecommendFragment extends BaseFragment {

    private RecyclerView mRecentRV;
    private View mParenV;
    private RecentAdapter mRecentAdapter;
    private LinkedList<String> contents = new LinkedList<>();

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParenV = inflater.inflate(R.layout.homepage_fragment_recommend, container, false);
        mRecentRV = (RecyclerView) mParenV.findViewById(R.id.homepage_recent);
        return mParenV;
    }

    @Override
    public void onActivityCreated(Bundle onSaveInstanceState) {
        super.onActivityCreated(onSaveInstanceState);

        mRecentAdapter = new RecentAdapter(contents);
        mRecentRV.setAdapter(mRecentAdapter);
        mRecentRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemClickController controller = ItemClickController.addTo(mRecentRV);
        controller.setOnItemClickListener(new RecentClickListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DBRecentListEvent event) {
        contents = event.getContents();
        mRecentAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AddToRecentEvent event) {
        contents.addFirst(event.getContent());
        mRecentAdapter.notifyDataSetChanged();
    }
}
