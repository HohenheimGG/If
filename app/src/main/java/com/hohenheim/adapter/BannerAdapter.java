package com.hohenheim.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hohenheim.R;


//import com.felix.com.hohenheim.banner.loader.ImageLoader;

public class BannerAdapter extends BasePagerAdapter{

    private String[] array;
    private Context context;

    public BannerAdapter(String[] array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int curPosition = position % array.length;
        CardView cardView = new CardView(context);
        cardView.setCardElevation(1);
        cardView.setCardBackgroundColor(Color.parseColor("#393F44B3"));
        cardView.setRadius(10);
        ImageView view = new ImageView(context);
        view.setBackgroundResource(R.drawable.view_banner);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
//        ImageLoader.load(array[curPosition], view);
        cardView.addView(view);
        container.addView(cardView);
        return cardView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        int count = array.length;
        if(count == 1)
            return 1;
        return MAX_NUM;
    }

    @Override
    public int getInitialPosition() {
        return MAX_NUM / 2 - MAX_NUM / 2 % array.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
