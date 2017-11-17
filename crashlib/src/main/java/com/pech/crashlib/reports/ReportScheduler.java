package com.pech.crashlib.reports;

import android.os.Handler;
import android.os.HandlerThread;

import com.pech.crashlib.utils.Logger;

/**
 * Created by yairp on 08/11/2017.
 */

public class ReportScheduler
{
    private static final int TIME_INTERVAL = 60000;

    private HandlerThread mSchedulerThread = null;
    private Handler mSchedulerHandler = null;

    private ISchedulerListener mListener = null;

    public ReportScheduler(ISchedulerListener listener)
    {
        mListener = listener;
        mSchedulerThread = new HandlerThread("SchedulerThread");
        mSchedulerThread.start();
        mSchedulerHandler = new Handler(mSchedulerThread.getLooper());
    }

    public void start()
    {
        schedule();
        Logger.log("Scheduler Started");
    }

    public void stop()
    {
        mSchedulerHandler.removeCallbacks(mRunnable);
        Logger.log("Scheduler Stopped");
    }

    public void restart()
    {
        stop();
        start();
    }

    private void schedule()
    {
        mSchedulerHandler.postDelayed(mRunnable, TIME_INTERVAL);
    }

    private Runnable mRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if(mListener == null)
                return;

            try
            {
                mListener.onSchedulerEvent();
                Logger.log("Scheduler Event");
                schedule();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    public void close()
    {
        stop();
        mSchedulerThread.interrupt();
    }

    public interface ISchedulerListener
    {
        void onSchedulerEvent();
    }
}
