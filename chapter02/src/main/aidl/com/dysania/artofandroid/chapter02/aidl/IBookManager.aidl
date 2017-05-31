// IBookManager.aidl
package com.dysania.artofandroid.chapter02.aidl;

// Declare any non-default types here with import statements
import com.dysania.artofandroid.chapter02.aidl.Book;
import com.dysania.artofandroid.chapter02.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
