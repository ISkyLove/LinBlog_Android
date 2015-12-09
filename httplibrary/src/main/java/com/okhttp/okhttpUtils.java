package com.okhttp;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;


public class okhttpUtils {
    private static OkHttpClient client=new OkHttpClient();

    public static void get(String url,Callback callback){
        Request mreRequest;
        mreRequest = new Request.Builder().url(url).build();
        client.newCall(mreRequest).enqueue(callback);
    }
}
