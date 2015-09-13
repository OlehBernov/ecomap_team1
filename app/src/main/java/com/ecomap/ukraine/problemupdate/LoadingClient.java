package com.ecomap.ukraine.problemupdate;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.problemupdate.manager.ProblemRequestReceiver;

import org.json.JSONException;

import java.util.List;

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
     * Address of the server from which LoadingClient gets
     * information about top 10 problem
     */
    private static final String TOP_10_PROBLEMS_URL = "http://ecomap.org/api/getStats4/";

    protected final String TAG = getClass().getSimpleName();

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
                    public void onResponse(final String response) {
                        new AsyncTask<Void, Void, List<Problem>>() {
                            @Override
                            protected List<Problem> doInBackground(Void... params) {
                                List<Problem> temp = null;
                                try {
                                     temp = JSONParser.parseBriefProblems(response);
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSONException in LoadingClient");
                                }
                                return temp;
                            }
                            @Override
                            protected void onPostExecute(List<Problem> result) {
                                problemRequestReceiver.setAllProblemsRequestResult(result);
                            }
                        }.execute();
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
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                ALL_PROBLEMS_URL + problemId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Details details = JSONParser.parseDetailedProblem(response);
                            problemRequestReceiver.setProblemDetailsRequestResult(details);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException in getProblemDetail");
                            problemRequestReceiver.setProblemDetailsRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse in getProblemDetail");
                problemRequestReceiver.setProblemDetailsRequestResult(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getTop10 () {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, TOP_10_PROBLEMS_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AllTop10Items allTop10Items =
                                    JSONParser.parseAllTop10Items(response);
                            problemRequestReceiver.setTop10RequestResult(allTop10Items);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException in getTop10");
                            problemRequestReceiver.setTop10RequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse in getTop10");
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

}