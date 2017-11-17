package com.pech.crashlib.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.pech.crashlib.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yairp on 08/11/2017.
 */

public abstract class BaseRequest extends StringRequest
{
    public BaseRequest(Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, Constants.URL, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();

        if(headers == null)
            headers = new HashMap<>();

        // headers.put("Content-Type", "application/json");

        return headers;
    }

    @Override
    public Object getTag()
    {
        return Constants.NAME;
    }

    public static boolean isResponseValid(String response)
    {
        try
        {
            Integer.parseInt(response);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
