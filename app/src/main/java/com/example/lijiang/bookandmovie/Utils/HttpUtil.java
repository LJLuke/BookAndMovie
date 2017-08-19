package com.example.lijiang.bookandmovie.Utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by cnrobin on 17-8-15.
 * Just Enjoy It!!!
 */

public class HttpUtil {
    public static void sendOkhttpRequest(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
