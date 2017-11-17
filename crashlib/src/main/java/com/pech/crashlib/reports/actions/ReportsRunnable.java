package com.pech.crashlib.reports.actions;

import com.pech.crashlib.reports.datasource.IReportDataSource;
import com.pech.crashlib.reports.Report;

/**
 * Created by yairp on 08/11/2017.
 */

/**
 * Parent class for the runnables that manipulate the report files.
 * @param <T> - Incase of future types of report types.
 */
public abstract class ReportsRunnable<T extends Report> implements Runnable
{
    protected T mReport = null;
    protected IReportDataSource<T> mDataSource;

    public ReportsRunnable(IReportDataSource dataSource)
    {
        mDataSource = dataSource;
    }

    public ReportsRunnable(T report, IReportDataSource dataSource)
    {
        mReport = report;
        mDataSource = dataSource;
    }
}
