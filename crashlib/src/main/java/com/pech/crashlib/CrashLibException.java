package com.pech.crashlib;

/**
 * Created by yairp on 08/11/2017.
 */

public class CrashLibException extends RuntimeException
{
    public CrashLibException(String message)
    {
        super(message);
    }

    public void logException()
    {
        CrashLib.getInstance().logException(this);
    }
}
