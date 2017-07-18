package com.dysania.artofandroid.chapter12.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by DysaniazzZ on 18/07/2017.
 */

public class IOUtil {

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
