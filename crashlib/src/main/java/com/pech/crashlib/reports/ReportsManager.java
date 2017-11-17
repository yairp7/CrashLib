package com.pech.crashlib.reports;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.pech.crashlib.reports.actions.LoadUnsentReportsRunnable;
import com.pech.crashlib.reports.actions.SaveReportRunnable;
import com.pech.crashlib.reports.datasource.IReportDataSource;
import com.pech.crashlib.reports.datasource.ReportsFileDataSource;
import com.pech.crashlib.service.SendReportsService;
import com.pech.crashlib.service.SendReportsServiceReceiver;
import com.pech.crashlib.utils.Constants;
import com.pech.crashlib.utils.FileManager;
import com.pech.crashlib.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yairp on 08/11/2017.
 */

/**
 * The class responsible for loading/saving/sending the reports.
 */
public class ReportsManager implements LoadUnsentReportsRunnable.ILoadReportsListener, SaveReportRunnable.ISaveReportListener
{
    private Application mApplication = null;

    private ArrayList<Report> mReports = null;

    private ExecutorService mThreadPool = null;

    private ReportScheduler mReportScheduler = null;

    private IReportDataSource mDataSource = null;

    private boolean mIsSending = false;

    public ReportsManager(Application application)
    {
        mApplication = application;

        mReports = new ArrayList<>();

        // Create a thread pool for managing the thread used(save/send/load reports)
        mThreadPool = Executors.newSingleThreadExecutor();

        // Create a datasource that works with local files
        mDataSource = new ReportsFileDataSource();
    }

    /**
     * Called on init of the CrashLib
     */
    public void init()
    {
        // Create the neccesery folders to save the reports
        FileManager.createFolders();

        mReportScheduler = new ReportScheduler(new ReportScheduler.ISchedulerListener()
        {
            @Override
            public void onSchedulerEvent()
            {
                sendReports();
            }
        });

        // Load unsent reports from the last time the application ran
        loadUnsentReports();
    }

    /**
     * Load unsent reports saved as files in the reports folder.
     */
    private void loadUnsentReports()
    {
        LoadUnsentReportsRunnable loadUnsentReportsRunnable = new LoadUnsentReportsRunnable(mDataSource, this);

        mThreadPool.submit(loadUnsentReportsRunnable);
    }

    /**
     * Saves the provided report as a file.
     * @param report - The provided report.
     */
    public void saveReport(Report report)
    {
        mReports.add(report);

        SaveReportRunnable saveReportThread = new SaveReportRunnable(report, mDataSource, this);

        mThreadPool.submit(saveReportThread);
    }

    /**
     * If there`s unsent reports, send them to the server.
     */
    private void sendReports()
    {
        if(mIsSending)
            return;

        mIsSending = true;

        if(mReports.size() > 0)
        {
            Logger.log("Trying to send reports...");

            startSendReportsService();
        }
        else
        {
            Logger.log("No events to be sent.");
            mIsSending = false;
        }
    }

    /**
     * Start the sending intentservice with the reports needed to be sent.
     */
    private void startSendReportsService()
    {
        mReportScheduler.stop();

        // Create a copy of the reports to be sent
        final ArrayList<Report> sentReports = new ArrayList<>(mReports);

        mReports.clear();

        SendReportsServiceReceiver receiver = new SendReportsServiceReceiver(new Handler(Looper.getMainLooper()));
        receiver.setResultListener(new SendReportsServiceReceiver.IResultListener()
        {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData)
            {
                mIsSending = false;

                if(resultCode == Constants.Results.RESULT_FAIL)
                {
                    // Restore the reports after failure of sending them
                    mReports.addAll(sentReports);
                }

                mReportScheduler.restart();
            }
        });

        Logger.log("Starting service...");

        Intent intent = new Intent(mApplication, SendReportsService.class);
        intent.putExtra(SendReportsService.PARAM_RECEIVER, receiver);
        intent.putParcelableArrayListExtra(SendReportsService.PARAM_REPORTS, sentReports);
        mApplication.startService(intent);
    }

    public void close()
    {
        mReportScheduler.close();
        mThreadPool.shutdown();
    }

    /**
     * Called after loading the unsent reports from previous run of the app.
     * @param reports - The list of unsent reports.
     */
    @Override
    public void onReportsLoaded(List<Report> reports)
    {
        int numOfUnsentReports = reports.size();

        for(Report report : reports)
        {
            mReports.add(report);
        }

        Logger.log(numOfUnsentReports + " unsent reports loaded!");

        if(numOfUnsentReports > 0)
            sendReports();
        else
            mReportScheduler.restart();
    }

    @Override
    public void onFailed()
    {
        mReportScheduler.restart();
    }

    @Override
    public void onReportSaved(Report report)
    {
        Logger.log("Report[" + report.getReportID() + "] saved!");
    }

    @Override
    public void onReportSavingFailed(Report report)
    {
        Logger.log("Report[" + report.getReportID() + "] saving failed!");
    }

    public IReportDataSource getDataSource()
    {
        return mDataSource;
    }
}
