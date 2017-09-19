package com.felix.hohenheim.banner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.felix.hohenheim.banner.R;

/**
 * Created by hohenheim on 17/9/19.
 */

public class PopWindowAdapter {

    private Context context;
    private View.OnClickListener listener;
    private TextView messageView;

    public PopWindowAdapter(Context context, View.OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void notifyMsg(String content) {
        messageView.setText(content);
    }

    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_capture_pop_window, null);
        messageView = (TextView)view.findViewById(R.id.message);
        Button top = (Button)view.findViewById(R.id.top);
        top.setText("复制内容");
        top.setOnClickListener(listener);
        Button bottom = (Button)view.findViewById(R.id.bottom);
        bottom.setText("跳转");
        bottom.setOnClickListener(listener);
        return view;
    }
}
