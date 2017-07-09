package com.felix.hohenheim.banner.network;

import android.graphics.Bitmap;

import com.felix.hohenheim.banner.utils.ImageResize;

import java.util.HashMap;
import java.util.Map;

public class ImageRequest extends Request<Bitmap> {

    private String url;
    private int width;
    private int height;

    public ImageRequest(Response.Listener<Bitmap> listener, String url, int width, int height) {
        super(listener);
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<>();
    }

    @Override
    public int getTimeoutMs() {
        return 3000;
    }

    @Override
    protected Response<Bitmap> parseNetworkResponse(NetworkResponse networkResponse) {
        Response<Bitmap> response = new Response<>();
        response.setContent(ImageResize.decodeRoundBitmapFromByte(networkResponse.getContent(), width, height));
        return response;
    }
}
