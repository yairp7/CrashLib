package com.pech.crashlib.reports.actions;

import com.pech.crashlib.reports.Report;
import com.pech.crashlib.reports.datasource.IReportDataSource;
import com.pech.crashlib.utils.Logger;

import java.util.List;

/**
 * Created by yairp on 08/11/2017.
 */

/**
 * Runnable responsible for deleting the sent report files.
 */
public class DeleteReportsRunnable extends ReportsRunnable<Report>
{
    private List<Report> mReports = null;

    public DeleteReportsRunnable(List<Report> reports, IReportDataSource<Report> dataSource)
    {
        super(dataSource);

        mReports = reports;
    }

    @Override
    public void run()
    {
        int numFilesDeleted = 0;

        for(Report report : mReports)
        {
            if(mDataSource.delete(report))
                numFilesDeleted++;
        }

        Logger.log(numFilesDeleted + " Files deleted.");
    }
}
