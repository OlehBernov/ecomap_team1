package com.ecomap.ukraine.updating.serverclient;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.data.manager.ProblemRequestReceiver;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.updating.convertion.JSONParser;

import org.json.JSONException;

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
     * Application context.
     */
    private Context context;

    /**
     * Request receiver.
     */
    private ProblemRequestReceiver problemRequestReceiver;

    /**
     * Constructor of LoadingClient.
     *
     * @param problemRequestReceiver request receiver.
     * @param context                application context.
     */
    public LoadingClient(final ProblemRequestReceiver problemRequestReceiver,
                         final Context context) {
        this.problemRequestReceiver = problemRequestReceiver;
        this.context = context;
    }

    /**
     * Sends a request to download brief information
     * about all problems.
     */
    public void getAllProblems() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_PROBLEMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            problemRequestReceiver.setAllProblemsRequestResult(
                                    new JSONParser().parseBriefProblems(response));
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in LoadingClient");
                            problemRequestReceiver.setAllProblemsRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                problemRequestReceiver.setAllProblemsRequestResult(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Sends a request to download detailed information
     * about concrete problem.
     *
     * @param problemId id of concrete problem
     */
    public void getProblemDetail(final int problemId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_PROBLEMS_URL
                + problemId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Details details = new JSONParser().parseDetailedProblem(response);
                            problemRequestReceiver.setProblemDetailsRequestResult(details);
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in getProblemDetail");
                            problemRequestReceiver.setProblemDetailsRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error response", "onErrorResponse in getProblemDetail");
                problemRequestReceiver.setProblemDetailsRequestResult(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

}