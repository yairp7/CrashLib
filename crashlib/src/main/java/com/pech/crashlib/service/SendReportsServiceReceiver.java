package com.pech.crashlib.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by yairp on 08/11/2017.
 */

public class SendReportsServiceReceiver extends ResultReceiver
{
    private IResultListener mListener = null;

    public SendReportsServiceReceiver(Handler handler)
    {
        super(handler);
    }

    public void setResultListener(IResultListener listener)
    {
        mListener = listener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData)
    {
        if(mListener != null)
            mListener.onReceiveResult(resultCode, resultData);
    }

    public interface IResultListener
    {
        void onReceiveResult(int resultCode, Bundle resultData);
    }
}
