package com.example.downloadfile;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

//关闭程序工具类，使用方法：ApplicationUtil.getInstance().exit();
//*在每次新建一个activity时，需要使用ApplicationUtil.getInstance().addActivity(activity)；
public class ApplicationUtil extends Application {
    private List<Activity> activityList;
    private static ApplicationUtil instance;//创建一个实例
    public ApplicationUtil() {
        super();
        activityList=new LinkedList<Activity>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    //返回ApplicationUtil类的实例
    public static ApplicationUtil getInstance(){
        if(instance==null){
            instance=new ApplicationUtil();
        }
        return instance;
    }

    //添加activity到list中
    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    //将栈中的activity全部清除
    public void exit(){
        for(Activity activity:activityList){
            activity.finish();
        }
        activityList.clear();
    }
}
