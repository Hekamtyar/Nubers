package com.example.hekmatyar.numbers;

import android.app.Activity;
import android.app.Application;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hekmatyar on 2018/5/29.
 */

public class SysApplication extends Application {
    private List<Activity> mList = new ArrayList<>();
    private static SysApplication instance;
    private SysApplication(){}
    public static synchronized SysApplication getInstance(){
        if (null == instance){
            instance = new SysApplication();
        }
        return instance;
    }
    public void addActivity(Activity activity){
        mList.add(activity);
    }
    public void query() {
        for (Activity activity : mList) {
            if (activity != Welcome.welcome) {
                activity.finish();
            }
        }
    }

    public void exit(){
        for(Activity activity:mList){
            if (activity != null){
                activity.finish();
            }
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}