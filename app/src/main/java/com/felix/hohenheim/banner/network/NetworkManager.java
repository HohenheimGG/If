package com.felix.hohenheim.banner.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {

    private static volatile NetworkManager manager;
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public static NetworkManager getInstance() {
        if(manager == null)
            synchronized(NetworkManager.class) {
                if(manager == null)
                    manager = new NetworkManager();
            }
            return manager;
    }

    public void executor(final Request<?> request) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                performRequest(request);
            }
        });
    }

    private static Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            Response response = (Response) msg.obj;
            if(response.isSuccess()) {
                response.getListener().onResponse(response.getContent());
            } else {
                response.getListener().onError(response.getError());
            }
        }
    };

    private void performRequest(Request<?> request) {
        String url = request.getUrl();
        Map<String, String> map = new HashMap<>();
        map.putAll(request.getHeaders());
        Response response;
        Message message = new Message();
        try{
            URL parseUrl = new URL(url);
            HttpURLConnection connection = openConnection(parseUrl, request);
            NetworkResponse networkResponse = new NetworkResponse();
            for (String header : map.keySet()) {
                connection.addRequestProperty(header, map.get(header));
            }
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();
            if (responseCode == -1) {
                networkResponse.setSuccess(false);
            }
            response = performResponse(connection, request);
        } catch(Exception e) {
            response = new Response();
            response.setError(e);
        }
        response.setListener(request.getListener());

        message.obj = response;
        handler.sendMessage(message);
    }

    private Response<?> performResponse(HttpURLConnection connection, Request<?> request) throws IOException{
        NetworkResponse networkResponse = new NetworkResponse();
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
            networkResponse.setSuccess(false);
        }
        networkResponse.setContentLength(connection.getContentLength());
        networkResponse.setContentEncoding(connection.getContentEncoding());
        networkResponse.setContentType(connection.getContentType());
        byte[] bytes = entityToBytes(inputStream);
        networkResponse.setContent(bytes);
        Response<?> response = request.parseNetworkResponse(networkResponse);
        return response;
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        return connection;
    }

    private byte[] entityToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bytes =
                new ByteArrayOutputStream();
        byte[] buffer;
        try {
            buffer = new byte[8 * 1024];
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
            return bytes.toByteArray();
        } finally {
            bytes.close();
        }
    }
}
