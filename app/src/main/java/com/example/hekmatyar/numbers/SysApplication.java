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
    //将启动的进程添加进入list中
    public void addActivity(Activity activity){
        mList.add(activity);
    }
    //将list中的activity全部销毁
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
    //杀进程
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}