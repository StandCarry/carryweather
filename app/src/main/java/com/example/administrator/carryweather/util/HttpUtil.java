package com.example.administrator.carryweather.util;



import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 发送HTTP请求，传入地址，注册回调处理服务器的响应
 */

public class HttpUtil {

    public static void sendOkHttpRequset(String address, okhttp3.Callback callback){

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
