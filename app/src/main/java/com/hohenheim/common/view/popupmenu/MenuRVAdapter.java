package com.hohenheim.common.view.popupmenu;

import android.support.v4.util.SparseArrayCompat;
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

class MenuRVAdapter extends RecyclerView.Adapter<MenuRVAdapter.MenuRVViewHolder> {

    private SparseArrayCompat<String> menus;

    MenuRVAdapter(SparseArrayCompat<String> menus) {
        this.menus = menus;
    }

    @Override
    public void onBindViewHolder(MenuRVAdapter.MenuRVViewHolder holder, int position) {
        holder.mContentTV.setText(menus.get(position));
    }

    @Override
    public MenuRVAdapter.MenuRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuRVAdapter.MenuRVViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.common_view_menu_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    static class MenuRVViewHolder extends RecyclerView.ViewHolder {

        private View mParent;
        private TextView mContentTV;

        private MenuRVViewHolder(View itemView) {
            super(itemView);
            mParent = itemView.findViewById(R.id.menu_item_container);
            mContentTV = (TextView)itemView.findViewById(R.id.menu_item_content);
        }
    }
}
