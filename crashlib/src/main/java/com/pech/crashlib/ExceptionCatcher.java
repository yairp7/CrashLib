package com.pech.crashlib;

/**
 * Created by yairp on 08/11/2017.
 */

import com.pech.crashlib.utils.Utils;

/**
 * The class responsible for catching the exceptions thrown
 * and passing them to the ReportsManager.
 */
public class ExceptionCatcher implements Thread.UncaughtExceptionHandler
{
    // The application context needed for the actions like file writing.
    private Thread.UncaughtExceptionHandler mDefaultUEH = null;
    private IExceptionCatcherListener mExceptionCaughtListener = null;
    private boolean mUseDefaultExceptionHandler = false;

    public ExceptionCatcher()
    {
        // Keep the current default exception handler aside so can be called on exception
        mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // Set the thread`s default exception handler to be this class
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setExceptionCaughtListener(IExceptionCatcherListener exceptionCaughtListener)
    {
        mExceptionCaughtListener = exceptionCaughtListener;
    }

    /**
     * Called when an exception is thrown in the application
     * @param thread
     * @param exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception)
    {
        // Notify
        if(mExceptionCaughtListener != null && !mUseDefaultExceptionHandler)
            mExceptionCaughtListener.onExceptionCaught(Utils.generateExceptionID(), exception);

        // Call the default exception handler to not disturb the program`s workflow.
        mDefaultUEH.uncaughtException(thread, exception);
    }

    public void setUseDefaultExceptionHandler(boolean useDefaultExceptionHandler)
    {
        mUseDefaultExceptionHandler = useDefaultExceptionHandler;
    }

    interface IExceptionCatcherListener
    {
        void onExceptionCaught(long when, Throwable exception);
    }
}
