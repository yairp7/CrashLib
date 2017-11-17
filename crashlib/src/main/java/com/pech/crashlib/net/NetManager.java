package com.pech.crashlib.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pech.crashlib.utils.Constants;

/**
 * Created by yairp on 08/11/2017.
 */

public class NetManager
{
    private RequestQueue mRequestQueue = null;

    public NetManager(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void sendRequest(BaseRequest request)
    {
        mRequestQueue.add(request);
    }

    public void clear()
    {
        mRequestQueue.cancelAll(Constants.NAME);
    }
}
