package com.pech.crashlib.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pech.crashlib.reports.Report;
import com.pech.crashlib.net.BaseRequest;

import java.util.List;

/**
 * Created by yairp on 08/11/2017.
 */

public class SendReportsRequest extends BaseRequest
{
    private List<Report> mReports = null;

    public SendReportsRequest(List<Report> reports, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(listener, errorListener);
        mReports = reports;
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            String json = objectMapper.writeValueAsString(mReports);
            return json.getBytes();
        }
        catch(JsonProcessingException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
