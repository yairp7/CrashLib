package com.pech.crashlib;

import android.app.Application;
import android.os.Build;
import android.support.annotation.NonNull;

import com.pech.crashlib.net.NetManager;
import com.pech.crashlib.reports.Report;
import com.pech.crashlib.reports.ReportsManager;
import com.pech.crashlib.utils.Logger;
import com.pech.crashlib.utils.Utils;

/**
 * Created by yairp on 08/11/2017.
 */

/**
 * Main singleton manager for the library.
 */
public class CrashLib
{
    private static CrashLib instance = null;

    public static CrashLib getInstance()
    {
        if(instance == null)
            instance = new CrashLib();

        return instance;
    }

    private ExceptionCatcher mExceptionCatcher = null;
    private ReportsManager mReportsManager = null;
    private NetManager mNetManager = null;

    private boolean mIsInit = false;

    /**
     * Initializes the library.
     * Must be called from the
     * protected void attachBaseContext(Context base)
     * function in the extended Application class.
     */
    public static void init(@NonNull Application application)
    {
        instance = new CrashLib();

        instance.mNetManager = new NetManager(application);
        instance.mExceptionCatcher = new ExceptionCatcher();
        instance.mReportsManager = new ReportsManager(application);

        instance.mExceptionCatcher.setExceptionCaughtListener(new ExceptionCatcher.IExceptionCatcherListener()
        {
            @Override
            public void onExceptionCaught(long id, Throwable exception)
            {
                instance.logException(id, exception);
            }
        });

        instance.mReportsManager.init();

        instance.mIsInit = true;
    }

    public void logException(Throwable exception)
    {
        logException(Utils.generateExceptionID(), exception);
    }

    /**
     * Log an exception.
     * Used for both uncaught exception arriving from the ExceptionCatcher and
     * for caught exception that needs to be logged.
     * @param id - The id of the report.
     * @param exception - The exception.
     */
    public void logException(long id, Throwable exception)
    {
        checkIfInit();

        Logger.log("Exception[" + id + "] caught: " + exception.getMessage());

        // Build a report object with the data from the exception
        Report report = new Report.Builder()
                .reportID(id)
                .when(Utils.getDate())
                .exception(exception.toString())
                .stackTrace(Utils.buildStackTraceString(exception))
                .logcat(Utils.getLogcat())
                .androidVersion(Build.VERSION.RELEASE)
                .device(android.os.Build.MODEL)
                .build();

        mReportsManager.saveReport(report);
    }

    /**
     * Disbale the exception handler of the library, and use only the default one.
     * @param useDefaultExceptionHandler
     */
    public void setUseDefaultExceptionHandler(boolean useDefaultExceptionHandler)
    {
        mExceptionCatcher.setUseDefaultExceptionHandler(useDefaultExceptionHandler);
    }

    public NetManager getNetManager()
    {
        checkIfInit();

        return mNetManager;
    }

    public ReportsManager getReportsManager()
    {
        checkIfInit();

        return mReportsManager;
    }

    private void checkIfInit()
    {
        if(!mIsInit)
            throw new CrashLibException("Must call init() first!");
    }
}
