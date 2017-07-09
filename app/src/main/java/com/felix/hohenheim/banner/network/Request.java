package com.felix.hohenheim.banner.network;

import java.util.Map;

public abstract class Request<E> {

    private Response.Listener<E> listener;

    public Request(Response.Listener<E> listener) {
        this.listener = listener;
    }

    public Response.Listener<E> getListener() {
        return listener;
    }

    public abstract String getUrl();
    public abstract Map<String, String> getHeaders();
    public abstract int getTimeoutMs();

    protected abstract Response<E> parseNetworkResponse(NetworkResponse networkResponse);
}
