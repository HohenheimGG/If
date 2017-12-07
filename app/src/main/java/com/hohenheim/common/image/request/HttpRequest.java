package com.hohenheim.common.image.request;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hohenheim.common.image.Decoder;
import com.hohenheim.common.image.RequestManager;
import com.hohenheim.common.image.encoder.Encoder;

/**
 * Created by com.hohenheim on 17/7/14.
 */

public class HttpRequest implements Request{

    private String url;
    private int error;
    private Decoder decoder;
    private Encoder encoder;
    private int placeholder;
    private ImageView view;
    private RequestManager manager;

    public HttpRequest(String url, RequestManager manager) {
        this.url = url;
        this.manager = manager;
    }

    @Override
    public Request error(int resourceId) {
        error = resourceId;
        return this;
    }

    @Override
    public Request decoder(Decoder decoder) {
        this.decoder = decoder;
        return this;
    }

    @Override
    public Request encoder(Encoder encoder) {
        this.encoder = encoder;
        return this;
    }

    @Override
    public Request placeholder(int resourceId) {
        this.placeholder = resourceId;
        return this;
    }

    @Override
    public Request into(ImageView view) {
        this.view = view;
        this.view.setTag(url);
        return this;
    }

    @Override
    public void recycler() {
        encoder = null;
        decoder = null;
        view = null;
    }

    @Override
    public void begin() {
        manager.load(decoder, encoder, url, this);
    }

    @Override
    public void onResourceReady(Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @Override
    public void onException(Exception e) {
        view.setImageResource(error);
    }
}
