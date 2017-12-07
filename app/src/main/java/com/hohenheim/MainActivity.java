package com.hohenheim;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


import com.hohenheim.common.adapter.BannerAdapter;
import com.hohenheim.common.transformer.ScalePageTransformer;
import com.hohenheim.common.utils.StringUtils;
import com.hohenheim.common.view.Banner;


public class MainActivity extends AppCompatActivity {

    private Button next;
    private Banner banner;

    private void init() {
        String[] pics = StringUtils.getImages();

        banner.setPageTransformer(new ScalePageTransformer());
        banner.setAdapter(new BannerAdapter(pics, this));
        banner.startAutoPlay();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = (Button)findViewById(R.id.next);
        banner = (Banner)findViewById(R.id.banner);
        Drawable drawable = getResources().getDrawable(R.drawable.arrow);
        drawable.setBounds(10, 0, 35, 25);
        next.setCompoundDrawables(null, null, drawable, null);
        init();
    }

}

