package com.zhenai.xunta.utils;

import android.app.Application;
import android.content.Context;

/**
 * 获取全局Context
 * Created by wenjing.tang on 2017/7/26.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
