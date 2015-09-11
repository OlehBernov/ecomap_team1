package com.ecomap.ukraine.problemupdate;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Wraps request queue and ensures
 * the existence of only one instance of it.
 */
public class RequestQueueWrapper {

    /**
     * Request queue wrapper instance.
     */
    private static RequestQueueWrapper instance;

    /**
     * Application context.
     */
    private static Context context;

    /**
     * Request queue.
     */
    private RequestQueue requestQueue;

    /**
     * Wrapper constructor.
     *
     * @param context application context.
     */
    private RequestQueueWrapper(final Context context) {
        RequestQueueWrapper.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Returns request queue wrapper instance.
     *
     * @param context application context.
     * @return request queue wrapper instance.
     */
    public static synchronized RequestQueueWrapper getInstance(final Context context) {
        if (instance == null) {
            instance = new RequestQueueWrapper(context);
        }
        return instance;
    }

    /**
     * Returns request queue.
     *
     * @return request queue.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds request to request queue.
     *
     * @param request request.
     * @param <T>     request type.
     */
    public <T> void addToRequestQueue(final Request<T> request) {
        getRequestQueue().add(request);
    }

}
