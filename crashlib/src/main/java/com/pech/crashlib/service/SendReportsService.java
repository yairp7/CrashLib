package com.pech.crashlib.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.android.volley.toolbox.RequestFuture;
import com.pech.crashlib.CrashLib;
import com.pech.crashlib.net.BaseRequest;
import com.pech.crashlib.net.SendReportsRequest;
import com.pech.crashlib.reports.Report;
import com.pech.crashlib.reports.actions.DeleteReportsRunnable;
import com.pech.crashlib.reports.datasource.IReportDataSource;
import com.pech.crashlib.utils.Constants;
import com.pech.crashlib.utils.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if(intent.getExtras() != null && intent.getExtras().containsKey(PARAM_RECEIVER))
        {
            mReceiver = intent.getParcelableExtra(PARAM_RECEIVER);
        }

        if(intent.getExtras() != null && intent.getExtras().containsKey(PARAM_REPORTS))
        {
            mResult = Constants.Results.RESULT_FAIL;

            final List<Report> reports = intent.getParcelableArrayListExtra(PARAM_REPORTS);

            RequestFuture<String> future = RequestFuture.newFuture();

            SendReportsRequest sendReportsRequest = new SendReportsRequest(reports, future, future);

            CrashLib.getInstance().getNetManager().sendRequest(sendReportsRequest);

            try
            {
                String response = future.get();

                if(BaseRequest.isResponseValid(response))
                {
                    mResult = Integer.parseInt(response);

                    deleteSentFiles(reports);
                }

                if(mReceiver != null)
                    mReceiver.send(mResult, null);
            }
            catch(InterruptedException | ExecutionException e)
            {
                Logger.log(e.toString());

                if(mReceiver != null)
                    mReceiver.send(mResult, null);
            }
        }
    }

    private void deleteSentFiles(List<Report> reports)
    {
        IReportDataSource dataSource = CrashLib.getInstance().getReportsManager().getDataSource();
        DeleteReportsRunnable deleteReportsRunnable = new DeleteReportsRunnable(reports, dataSource);
        new Handler(Looper.myLooper()).post(deleteReportsRunnable);
    }
}
