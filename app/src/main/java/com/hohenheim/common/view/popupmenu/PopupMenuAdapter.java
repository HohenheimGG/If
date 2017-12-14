package com.hohenheim.common.view.popupmenu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hohenheim.R;

import java.util.List;

/**
 * Created by hohenheim on 2017/12/14.
 */

class PopupMenuAdapter extends RecyclerView.Adapter<PopupMenuAdapter.PopupMenuViewHolder> {

    private List<String> menus;

    PopupMenuAdapter(List<String> menus) {
        this.menus = menus;
    }

    @Override
    public void onBindViewHolder(PopupMenuAdapter.PopupMenuViewHolder holder, int position) {
        holder.mContentTV.setText(menus.get(position));
    }

    @Override
    public PopupMenuAdapter.PopupMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PopupMenuAdapter.PopupMenuViewHolder viewHolder = new PopupMenuAdapter.PopupMenuViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.common_view_menu_item, parent, false));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    static class PopupMenuViewHolder extends RecyclerView.ViewHolder {

        private View mParent;
        private TextView mContentTV;

        private PopupMenuViewHolder(View itemView) {
            super(itemView);
            mParent = itemView.findViewById(R.id.menu_item_container);
            mContentTV = (TextView)itemView.findViewById(R.id.menu_item_content);
        }
    }
}
