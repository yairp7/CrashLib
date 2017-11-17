package com.pech.crashlib.reports.datasource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pech.crashlib.reports.Report;
import com.pech.crashlib.utils.Constants;
import com.pech.crashlib.utils.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yairp on 08/11/2017.
 */

/**
 * Local reports file saving.
 */
public class ReportsFileDataSource implements IReportDataSource<Report>
{
    @Override
    public boolean save(Report report)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            String jsonData = objectMapper.writeValueAsString(report);

            String file = FileManager.getReportsPath() + report.getReportID() + ".r";

            return FileManager.append(file, jsonData);
        }
        catch(JsonProcessingException e)
        {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean delete(Report report)
    {
        return FileManager.delete(FileManager.getReportsPath() + report.getReportID() + Constants.REPORT_FILE_ENDING);
    }

    @Override
    public List<Report> list()
    {
        List<Report> reports = new ArrayList<>();
        List<String> filesData = FileManager.readFiles(FileManager.getReportsPath());
        for(String fileData : filesData)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                Report report = objectMapper.readValue(fileData, Report.class);
                reports.add(report);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return reports;
    }
}
