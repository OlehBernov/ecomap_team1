package com.ecomap.ukraine.updating.serverclient;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.data.manager.ListenersNotifier;
import com.ecomap.ukraine.updating.convertion.JSONParser;
import com.ecomap.ukraine.data.manager.DataListener;

import org.json.JSONException;

import java.util.Set;

/**
 * Performs loading data from server.
 */
public class LoadingClient {

    /**
     * Address of the server from which LoadingClient gets brief
     * information about all problems.
     */
    private static final String ALL_PROBLEMS_URL = "http://ecomap.org/api/problems/";

    /**
     * Server response converted to the entity.
     */
    private Object requestResult;

    /**
     * Object, which able to notify listeners
     * about result.
     */
    private ListenersNotifier listenersNotifier;

    /**
     * Constructor of LoadingClient.
     *
     * @param listenersNotifier able to notify listeners about result.
     */
    public LoadingClient(ListenersNotifier listenersNotifier) {
        this.listenersNotifier = listenersNotifier;
    }

    /**
     * Sends a request to download brief information
     * about all problems.
     *
     * @param context application context.
     * @param listeners objects, which get response from
     *                  server converted to the objects of entities.
     */
    public void getAllProblems(final Context context, final Set<DataListener> listeners) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_PROBLEMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            requestResult = new JSONParser().parseBriefProblems(response);
                        } catch (JSONException e) {
                            requestResult = null;
                        } finally {
                            listenersNotifier.notifyListeners(RequestTypes.ALL_PROBLEMS,
                                    requestResult, listeners);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestResult = null;
                listenersNotifier.notifyListeners(RequestTypes.ALL_PROBLEMS,
                        requestResult, listeners);
            }
        });

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
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

}