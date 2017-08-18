package com.example.lijiang.bookandmovie;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lijiang on 2017/8/17.
 */

public class HttpUtil {
    public static void sendOkHttpRequst(String adress,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(adress)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
