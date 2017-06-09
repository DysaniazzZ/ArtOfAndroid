// IBinderPool.aidl
package com.dysania.artofandroid.chapter02.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {
    /**
     * @param binderCode, the unique token of specific Binder
     * @return specific Binder who's token is binderCode
     */
    IBinder queryBinder(int binderCode);
}
