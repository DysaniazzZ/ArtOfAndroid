package com.dysania.artofandroid.chapter12;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * Created by DysaniazzZ on 17/07/2017.
 */

public class BitmapUtil {

    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calcInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calcInSampleSize(Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        int width = options.outWidth;
        int height = options.outHeight;

        if (width > reqWidth && height > reqHeight) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            while ((halfWidth / inSampleSize) > reqWidth && (halfHeight / inSampleSize) > reqHeight) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
