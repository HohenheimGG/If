package com.felix.hohenheim.banner.network;


public class Response<T> {

    public interface Listener<T> {
        public void onResponse(T t);
        public void onError(Exception e);
    }

    private T content;
    private Listener<T> listener;
    private Exception error;

    public void setError(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Listener<T> getListener() {
        return listener;
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }
}
