package com.pech.crashlib.reports.actions;

import com.pech.crashlib.reports.datasource.IReportDataSource;
import com.pech.crashlib.reports.Report;

/**
 * Created by yairp on 08/11/2017.
 */

/**
 * Runnable class responsible for saving the report file.
 */
public class SaveReportRunnable extends ReportsRunnable<Report>
{
    private ISaveReportListener mListener = null;

    public SaveReportRunnable(Report report, IReportDataSource<Report> dataSource, ISaveReportListener listener)
    {
        super(report, dataSource);
        mListener = listener;
    }

    @Override
    public void run()
    {
        boolean success = mDataSource.save(mReport);

        if(success)
        {
            if(mListener != null)
                mListener.onReportSaved(mReport);
        }
        else
        {
            if(mListener != null)
                mListener.onReportSavingFailed(mReport);
        }
    }

    public interface ISaveReportListener
    {
        void onReportSaved(Report report);
        void onReportSavingFailed(Report report);
    }
}
