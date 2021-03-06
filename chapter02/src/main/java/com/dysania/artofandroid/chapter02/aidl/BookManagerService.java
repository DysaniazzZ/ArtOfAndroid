package com.dysania.artofandroid.chapter02.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by DysaniazzZ on 31/05/2017.
 */

public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    //private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!mListenerList.contains(listener)) {
//                mListenerList.add(listener);
//                Log.i(TAG, "register listener succeeded.");
//            } else {
//                Log.i(TAG, "already exists.");
//            }
//            Log.i(TAG, "registerListener, size: " + mListenerList.size());

            mListenerList.register(listener);
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.i(TAG, "registerListener, current size: " + N);

        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (mListenerList.contains(listener)) {
//                mListenerList.remove(listener);
//                Log.i(TAG, "unregister listener succeeded.");
//            } else {
//                Log.i(TAG, "not found, can not unregister.");
//            }
//            Log.i(TAG, "unregisterListener, current size: " + mListenerList.size());

            boolean unregister = mListenerList.unregister(listener);
            if (unregister) {
                Log.i(TAG, "unregister listener succeeded.");
            } else {
                Log.i(TAG, "not found, can not unregister.");
            }
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.i(TAG, "registerListener, current size: " + N);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission("com.dysania.artofandroid.chapter02.permission.ACCESS_BOOK_SERVICE");
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String packName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packName = packages[0];
            }
            if (!packName.startsWith("com.dysania")) {
                return false;
            }

            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "iOS"));

        new Thread(new ServiceWorker()).start();
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 确保该方法运行在非UI线程
     */
    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
//        Log.i(TAG, "onNewBookArrived, notify listeners: " + mListenerList.size());
//
//        for (int i = 0; i < mListenerList.size(); i++) {
//            IOnNewBookArrivedListener bookArrivedListener = mListenerList.get(i);
//            Log.i(TAG, "onNewBookArrived, notify listener: " + bookArrivedListener);
//            bookArrivedListener.onNewBookArrivedListener(book);
//        }

        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrivedListener(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //方法一：在onBind方法中验证权限
        int check = checkCallingOrSelfPermission("com.dysania.artofandroid.chapter02.permission.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }
}
