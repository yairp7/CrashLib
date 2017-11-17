package com.pech.crashlib.reports.actions;

import com.pech.crashlib.reports.Report;
import com.pech.crashlib.reports.datasource.IReportDataSource;

import java.util.List;

/**
 * Created by yairp on 08/11/2017.
 */

public class LoadUnsentReportsRunnable extends ReportsRunnable<Report>
{
    private ILoadReportsListener mListener = null;

    public LoadUnsentReportsRunnable(IReportDataSource<Report> dataSource, ILoadReportsListener listener)
    {
        super(dataSource);
        mListener = listener;
    }

    @Override
    public void run()
    {
        List<Report> reports = mDataSource.list();

        if(mListener != null)
            mListener.onReportsLoaded(reports);
    }

    public interface ILoadReportsListener
    {
        void onReportsLoaded(List<Report> reports);
        void onFailed();
    }
}
