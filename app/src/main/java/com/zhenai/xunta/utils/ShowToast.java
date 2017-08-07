package com.zhenai.xunta.utils;

import android.widget.Toast;

/**
 * 打印Toast
 * Created by wenjing.tang on 2017/7/26.
 */

public class ShowToast {

    public static void showToast(String toastString){
        Toast.makeText(MyApplication.getContext(),toastString, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String toastString){
        Toast.makeText(MyApplication.getContext(),toastString, Toast.LENGTH_LONG).show();
    }
}
