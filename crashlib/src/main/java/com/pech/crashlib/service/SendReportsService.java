package com.pech.crashlib.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pech.crashlib.CrashLib;
import com.pech.crashlib.net.BaseRequest;
import com.pech.crashlib.net.SendReportsRequest;
import com.pech.crashlib.reports.Report;
import com.pech.crashlib.reports.actions.DeleteReportsRunnable;
import com.pech.crashlib.reports.datasource.IReportDataSource;
import com.pech.crashlib.utils.Constants;
import com.pech.crashlib.utils.Logger;

import java.util.List;

/**
 * Created by yairp on 08/11/2017.
 */

public class SendReportsService extends IntentService
{
    public static final String PARAM_RECEIVER = "receiver";
    public static final String PARAM_REPORTS = "reports";

    private ResultReceiver mReceiver;
    private int mResult;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendReportsService()
    {
        super("SendReportsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if(intent.getExtras().containsKey(PARAM_RECEIVER))
        {
            mReceiver = intent.getParcelableExtra(PARAM_RECEIVER);
        }

        if(intent.getExtras().containsKey(PARAM_REPORTS))
        {
            mResult = Constants.Results.RESULT_FAIL;

            final List<Report> reports = intent.getParcelableArrayListExtra(PARAM_REPORTS);

            CrashLib.getInstance().getNetManager().sendRequest(new SendReportsRequest(reports, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    if(BaseRequest.isResponseValid(response))
                    {
                        mResult = Integer.parseInt(response);

                        if(mReceiver != null)
                            mReceiver.send(mResult, null);

                        deleteSentFiles(reports);
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Logger.log(error.toString());

                    if(mReceiver != null)
                        mReceiver.send(mResult, null);
                }
            }));
        }
    }

    private void deleteSentFiles(List<Report> reports)
    {
        IReportDataSource dataSource = CrashLib.getInstance().getReportsManager().getDataSource();
        DeleteReportsRunnable deleteReportsRunnable = new DeleteReportsRunnable(reports, dataSource);
        new Handler(Looper.myLooper()).post(deleteReportsRunnable);
    }
}
