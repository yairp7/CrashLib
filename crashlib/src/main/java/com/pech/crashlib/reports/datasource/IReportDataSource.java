package com.pech.crashlib.reports.datasource;

import com.pech.crashlib.reports.Report;

import java.util.List;

/**
 * Created by yairp on 08/11/2017.
 */

/**
 * Generic interface for the data source for the local reports.
 * (incase saving needs to be in a local database/file etc...)
 */
public interface IReportDataSource<T extends Report>
{
    boolean save(T report);
    boolean delete(T report);
    List<T> list();
}
