package com.ecomap.ukraine.restclient;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingeltion {

    private static RequestQueueSingeltion mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestQueueSingeltion(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized
    RequestQueueSingeltion getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueSingeltion(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void
    addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}

