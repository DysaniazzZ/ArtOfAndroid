package com.dysania.artofandroid.chapter13;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DysaniazzZ on 19/07/2017.
 */

public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/CrashTest/log/";
    private static final String FILE_NAME_PREFIX = "crash_";
    private static final String FILE_NAME_SUFFIX = "_.trace";

    private Context mContext;
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private static final CrashHandler sInstance = new CrashHandler();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当程序中有未捕获的异常，系统将会自动调用#uncaughtException
     *
     * @param thread 出现未捕获异常的线程
     * @param ex 未捕获的异常
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到SD卡
            dumpExceptionToSDCard(ex);
            //上传异常信息到服务器
            uploadExceptionToServer(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ex.printStackTrace();

        //如果系统提供了默认的异常处理器，就交给系统去处理，否则就自己手动结束进程
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        //如果SD卡不存在，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.w(TAG, "sdcard unmounted, dump exception failed.");
            return;
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(PATH + FILE_NAME_PREFIX + currentTime + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(currentTime);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + ", dump exception failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);

        //应用版本号
        pw.print("App Version: " + pi.versionName + "_" + pi.versionCode);
        pw.println();

        //系统版本号
        pw.print("OS Version: " + VERSION.RELEASE + "_" + VERSION.SDK_INT);
        pw.println();

        //手机制造商
        pw.print("Vendor: " + Build.MANUFACTURER);
        pw.println();

        //手机品牌
        pw.print("Brand: " + Build.BRAND);
        pw.println();

        //手机型号
        pw.print("Model: " + Build.MODEL);
        pw.println();

        //CPU架构
        pw.print("CPU ABI: " + Build.CPU_ABI);
        pw.println();
    }

    private void uploadExceptionToServer(Throwable ex) {
        //TODO upload exception info to web server
    }
}
