package com.zhenai.xunta.utils;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * HTTP请求类
 * Created by wenjing.tang on 2017/7/26.
 */

public class HttpUtil {

    //Get请求
    public static void sendGetRequestWithOkHttp(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //POST请求，提交单个参数
    public static void sendPostRequestWithOkHttp(String address, String key, String value, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody  = new FormBody.Builder()
                .add(key,value)
                .build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //POST请求，提交多个参数
    public static void sendPostMultiRequestWithOkHttp(String address, Map<String,String> map, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder  builder = new FormBody.Builder();
        //遍历Map，添加提交参数
        for(Map.Entry<String, String> m :map.entrySet()){
            builder.add(m.getKey(),m.getValue());
        }
        RequestBody requestBody  = builder.build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

}