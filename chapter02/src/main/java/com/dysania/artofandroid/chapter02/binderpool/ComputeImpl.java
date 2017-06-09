package com.dysania.artofandroid.chapter02.binderpool;

import android.os.RemoteException;

/**
 * Created by DysaniazzZ on 08/06/2017.
 */

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
