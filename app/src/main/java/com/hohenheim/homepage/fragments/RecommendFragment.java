package com.hohenheim.homepage.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hohenheim.R;
import com.hohenheim.common.fragments.BaseFragment;
import com.hohenheim.common.view.IconView;
import com.hohenheim.scancode.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private View parentView;
    private List<String> strings = new ArrayList<>();

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
        strings.add("二维码扫码");
        parentView = inflater.inflate(R.layout.fragment_recommend, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ToolListAdapter(strings, new ItemOnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CaptureActivity.class));
            }
        }));
        return parentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private static class ToolListAdapter extends RecyclerView.Adapter<ToolListViewHolder> {

        private List<String> strings;
        private ItemOnClickListener listener;

        public ToolListAdapter(List<String> strings, ItemOnClickListener listener) {
            this.strings = strings;
            this.listener = listener;
        }

        @Override
        public ToolListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ToolListViewHolder holder = new ToolListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_common_tool_item, null));
            holder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ToolListViewHolder holder, int position) {
            holder.tvContent.setText(strings.get(position));
            holder.ivCollect.setFraction(1);
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }
    }

    private static class ToolListViewHolder extends RecyclerView.ViewHolder {

        TextView tvContent;
        IconView ivCollect;

        ToolListViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView)itemView.findViewById(R.id.tv_tool_content);
            ivCollect = (IconView)itemView.findViewById(R.id.iv_collect);
        }
    }

    private interface ItemOnClickListener {
        void onClick(View view);
    }
}
