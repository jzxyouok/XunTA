package com.zhenai.xunta.utils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
/*        //设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();*/

        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //POST请求，提交单个参数
    public static void sendPostRequestWithOkHttp(String address, String key, String value, okhttp3.Callback callback){
        //OkHttpClient client = new OkHttpClient();

        //设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        RequestBody requestBody  = new FormBody.Builder()
                .add(key,value)
                .build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //POST请求，上传图片文件
    public static void sendFileWithOkHttp(String address,String name, File imageFile, okhttp3.Callback callback){
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(name, imageFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                .build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }


    //POST请求，提交多个参数
    public static void sendPostMultiRequestWithOkHttp(String address, Map<String,String> map, okhttp3.Callback callback){

        //设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

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
