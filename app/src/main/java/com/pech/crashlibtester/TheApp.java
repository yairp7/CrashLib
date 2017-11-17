package com.pech.crashlibtester;

import android.app.Application;
import android.content.Context;

import com.pech.crashlib.CrashLib;

/**
 * Created by yairp on 08/11/2017.
 */

public class TheApp extends Application
{
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);

        CrashLib.init(this);
    }
}
