package com.dysania.artofandroid.chapter02.utils;

import android.os.Environment;

/**
 * Created by DysaniazzZ on 24/05/2017.
 */

public class MyConstants {

    public static final String CHAPTER_2_PATH = Environment.getExternalStorageDirectory().getPath() + "/artofandroid/chapter02/";
    public static final String CACHE_FILE_PATH = CHAPTER_2_PATH + "usercache";

    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVICE = 1;
}
