package com.hohenheim.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hohenheim.R;
import com.hohenheim.view.IconView;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private View parentView;

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
        parentView = inflater.inflate(R.layout.fragment_recommend, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ToolListAdapter(new ArrayList<String>()));
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

        public ToolListAdapter(List<String> strings) {
            this.strings = strings;
        }

        @Override
        public ToolListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ToolListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_common_tool_item, null));
        }

        @Override
        public void onBindViewHolder(ToolListViewHolder holder, int position) {
            holder.tvContent.setText(strings.get(position));
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
}
