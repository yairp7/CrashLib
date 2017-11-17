package com.pech.crashlib.utils;

import android.text.format.DateFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by yairp on 08/11/2017.
 */

public class Utils
{
    public static String getLogcat()
    {
        String processId = Integer.toString(android.os.Process.myPid());

        StringBuilder builder = new StringBuilder();

        try
        {
            String[] command = new String[] { "logcat", "-d", "-v", "threadtime" };

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                if(line.contains(processId))
                    builder.append(line + "\n");
            }
        }
        catch (IOException ex)
        {
            Logger.log("getLog failed");
        }

        return builder.toString();
    }

    public static long generateExceptionID()
    {
        return System.currentTimeMillis();
    }

    public static String getDate()
    {
        Date d = new Date();
        return DateFormat.format("dd/MM/yyyy - hh:mm:ss", d.getTime()).toString();
    }

    public static String buildStackTraceString(Throwable exception)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(StackTraceElement stackTraceElement : exception.getStackTrace())
        {
            stringBuilder.append(stackTraceElement.toString() + "\n");
        }

        return stringBuilder.toString();
    }
}
