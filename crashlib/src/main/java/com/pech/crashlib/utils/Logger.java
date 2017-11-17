package com.pech.crashlib.utils;

import android.util.Log;

/**
 * Created by yairp on 08/11/2017.
 */

public class Logger
{
    public static final String TAG = Constants.NAME;

    public static void log(String log)
    {
        Log.d(TAG, log);
    }
}
