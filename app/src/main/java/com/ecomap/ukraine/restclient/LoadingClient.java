package com.ecomap.ukraine.restclient;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.convertion.JSONParser;

import org.json.JSONException;

import java.util.Set;

/**
 * Performs loading data from server.
 */
public class LoadingClient {

    /**
     * Address of the server from which LoadingClient gets information.
     */
    private static final String URL = "http://ecomap.org/api/problems/";

    /**
     * Server response converted to the entity.
     */
    private Object requestResult;

    /**
     * Sends a request to download brief information
     * about all problems.
     *
     * @param context application context.
     * @param listeners objects, which get response from
     *                  server converted to the objects of entities.
     */
    public void getAllProblems(final Context context, final Set<DataListener> listeners) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            requestResult = new JSONParser().parseBriefProblems(response);
                        } catch (JSONException e) {
                            requestResult = null;
                        } finally {
                            notifyListeners(RequestTypes.ALL_PROBLEMS, requestResult, listeners);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestResult = null;
                notifyListeners(RequestTypes.ALL_PROBLEMS, requestResult, listeners);
            }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Sends a request to download detailed information
     * about concrete problem.
     *
     * @param problemId id of concrete problem
     * @param context context of application.
     * @param listeners objects, which get response from
     *                  server converted to the objects of entities.
     */
    public void getProblemDetail(final int problemId, final Context context,
                                 final Set<DataListener> listeners) {

    }

    /**
     * Notify all listeners about server response
     * and send them received information.
     *
     * @param requestType type of request.
     * @param requestResult server response converted to the objects of entities.
     * @param listeners objects, which get response from
     *                  server converted to the objects of entities.
     */
    private void notifyListeners(final int requestType, Object requestResult,
                                 final Set<DataListener> listeners) {
        for (DataListener listener : listeners) {
            listener.update(requestType, requestResult);
        }
    }

}