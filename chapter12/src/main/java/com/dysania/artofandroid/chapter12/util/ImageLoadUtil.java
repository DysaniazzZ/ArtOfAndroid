package com.dysania.artofandroid.chapter12.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import com.dysania.artofandroid.chapter12.DiskLruCache;
import com.dysania.artofandroid.chapter12.R;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by DysaniazzZ on 17/07/2017.
 */

public class ImageLoadUtil {

    private static final String TAG = "ImageLoadUtil";

    private Context mContext;
    private LruCache<String, Bitmap> mMemoryLruCache;
    private DiskLruCache mDiskLruCache;

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;               //磁盘缓存取50MB
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private boolean mIsDiskLruCacheCreated = false;

    private static final int TAG_KEY_URL = R.id.imageloader_url;

    private static final int MSG_LOAD_RESULT = 0x01;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 5;   //线程闲置超时时间

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_RESULT:
                    LoaderResult loaderResult = (LoaderResult) msg.obj;
                    ImageView imageView = loaderResult.mImageView;
                    String url = (String) imageView.getTag(TAG_KEY_URL);
                    if (!TextUtils.isEmpty(url) && url.equals(loaderResult.mUrl)) {
                        imageView.setImageBitmap(loaderResult.mBitmap);
                    } else {
                        Log.w(TAG, "set image bitmap, but url has changed.");
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), sThreadFactory);

    private ImageLoadUtil(Context context) {
        mContext = context.getApplicationContext();

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);        //KB
        int cacheSize = maxMemory / 8;  //内存缓存取当前内存的1/8
        mMemoryLruCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }

        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ImageLoadUtil build(Context context) {
        return new ImageLoadUtil(context);
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private long getUsableSpace(File path) {
        return path.getUsableSpace();
    }

    private void addBitmapToMemoryCache(String key, Bitmap value) {
        if (getBitmapFromMemory(key) == null) {
            mMemoryLruCache.put(key, value);
        }
    }

    private Bitmap getBitmapFromMemory(String key) {
        return mMemoryLruCache.get(key);
    }

    public void bindBitmap(String url, ImageView imageView) {
        bindBitmap(url, imageView, 0, 0);
    }

    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URL, url);

        Bitmap bitmap = loadBitmapFromMemoryCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable runnableTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult loaderResult = new LoaderResult(imageView, url, bitmap);
                    mMainHandler.obtainMessage(MSG_LOAD_RESULT, loaderResult).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(runnableTask);
    }

    public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemoryCache(url);
        if (bitmap != null) {
            Log.d(TAG, "load bitmap from memory cache.");
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "load bitmap from disk cache.");
                return bitmap;
            }

            bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "load bitmap from url");
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "something wrong happens when load bitmap, so download bitmap from url directly.");
            bitmap = downloadBitmapFromUrl(url);
        }

        return bitmap;
    }

    private Bitmap loadBitmapFromMemoryCache(String url) {
        String key = hashKeyFormUrl(url);
        return getBitmapFromMemory(key);
    }

    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI thread.");
        }

        if (mDiskLruCache == null) {
            return null;
        }

        String key = hashKeyFormUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }

        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI thread, it's not recommended!");
        }

        if (mDiskLruCache == null) {
            return null;
        }

        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = ImageResizeUtil.decodeBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }

        return bitmap;
    }

    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "download bitmap failed.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            IOUtil.close(out);
            IOUtil.close(in);
        }
        return false;
    }

    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            IOUtil.close(in);
        }

        return bitmap;
    }

    private static class LoaderResult {

        public ImageView mImageView;
        public String mUrl;
        public Bitmap mBitmap;

        public LoaderResult(ImageView imageView, String url, Bitmap bitmap) {
            mImageView = imageView;
            mUrl = url;
            mBitmap = bitmap;
        }
    }
}
