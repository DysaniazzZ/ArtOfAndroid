package com.dysania.artofandroid.chapter02.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import java.util.List;

/**
 * Created by DysaniazzZ on 22/05/2017.
 */

public class MyUtil {

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        if(runningAppProcessInfos == null) {
            return null;
        }
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos) {
            if(runningAppProcessInfo.pid == pid) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }
}
