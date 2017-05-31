// IOnNewBookArrivedListener.aidl
package com.dysania.artofandroid.chapter02.aidl;

// Declare any non-default types here with import statements
import com.dysania.artofandroid.chapter02.aidl.Book;

interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      void onNewBookArrivedListener(in Book newBook);
}
