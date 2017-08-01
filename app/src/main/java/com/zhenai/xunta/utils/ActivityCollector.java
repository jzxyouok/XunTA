package com.zhenai.xunta.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity活动管理器
 * Created by wenjing.tang on 2017/8/1.
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishActivity(Activity activity){
        activity.finish();

    }
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
