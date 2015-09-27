package com.ecomap.ukraine.update;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Statistics;
import com.ecomap.ukraine.update.manager.DataResponseReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Performs loading data from server.
 */
public class LoadingClient {

    /**
     * Address of the server from which LoadingClient gets brief
     * information about all problems.
     */
    private static final String ALL_PROBLEMS_URL = "http://ecomap.org/api/problems/";

    private static final String POST_VOTE_URL = "http://ecomap.org/api/vote";

    /**
     * Address of the server from which LoadingClient gets
     * information about top 10 problem
     */
    private static final String TOP_10_PROBLEMS_URL = "http://ecomap.org/api/getStats4/";

    private static final String STATISTICS_URL = "http://ecomap.org/api/getStats2/";

    private static final int STATISTIC_ITEMS_COUNT = 5;

    protected final String TAG = getClass().getSimpleName();

    /**
     * Application context.
     */
    private Context context;

    /**
     * Request receiver.
     */
    private DataResponseReceiver dataResponseReceiver;

    /**
     * Constructor of LoadingClient.
     *
     * @param dataResponseReceiver request receiver.
     * @param context              application context.
     */
    public LoadingClient(final DataResponseReceiver dataResponseReceiver,
                         final Context context) {
        this.dataResponseReceiver = dataResponseReceiver;
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
                                dataResponseReceiver.setAllProblemsResponse(result);
                            }
                        }.execute();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataResponseReceiver.setAllProblemsResponse(null);
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
                            dataResponseReceiver.setProblemDetailsResponse(details);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException in getProblemDetail");
                            dataResponseReceiver.setProblemDetailsResponse(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse in getProblemDetail");
                dataResponseReceiver.setProblemDetailsResponse(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getTop10() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, TOP_10_PROBLEMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        new AsyncTask<Void, Void, AllTop10Items>() {
                            @Override
                            protected AllTop10Items doInBackground(Void... params) {
                                AllTop10Items allTop10Items = null;
                                try {
                                    allTop10Items =
                                            JSONParser.parseAllTop10Items(response);
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSONException in getTop10");
                                }
                                return allTop10Items;
                            }

                            @Override
                            protected void onPostExecute(AllTop10Items result) {
                                dataResponseReceiver.setTop10Response(result);
                            }
                        }.execute();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse in getTop10");
                dataResponseReceiver.setTop10Response(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Post vote of problem on server
     *
     * @param problemID   problem id
     * @param userID      user id
     * @param userName    user name
     * @param userSurname user surname
     */
    public void postVote(final String problemID, final String userID,
                         final String userName, final String userSurname) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_VOTE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataResponseReceiver.onVoteAdded();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse in postVote");
                        Toast.makeText(context,
                                R.string.error_of_connection, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(JSONFields.PROBLEM_ID, problemID);
                AccountManager accountManager = AccountManager.getInstance(context);
                if (!accountManager.isAnonymousUser()) {
                    params.put(JSONFields.USER_ID, userID);
                    params.put(JSONFields.USER_NAME, userName);
                    params.put(JSONFields.USER_SURNAME, userSurname);
                }
                return params;
            }
        };

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Post comment of problem on server using AsyncTask
     *
     * @param problemID   problem id
     * @param userID      user id
     * @param userName    user name
     * @param userSurname user surname
     * @param content     content of content
     */
    public void postComment(final int problemID, final String userID,
                            final String userName, final String userSurname,
                            final String content) {
        JSONObject dataJSON;
        try {
            dataJSON = JSONParser.generateCommentObj(
                    userID, userName, userSurname, content);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException in postComment");
            return;
        }

        PostCommentTask postComment = new PostCommentTask(problemID, context,
                dataResponseReceiver);
        postComment.execute(dataJSON);
    }

    /**
     * Starts downloading information about statistics of problem posting for all
     * avalible periods.
     */
    public void getStatistics() {
        Map<String, SparseIntArray> statisticsItems = new HashMap<>();
        getStatisticsItem(Statistics.DAILY, statisticsItems);
        getStatisticsItem(Statistics.WEEKLY, statisticsItems);
        getStatisticsItem(Statistics.MONTH, statisticsItems);
        getStatisticsItem(Statistics.ANNUAL, statisticsItems);
        getStatisticsItem(Statistics.FOR_ALL_TIME, statisticsItems);
    }

    /**
     * Sends a request to download statistics information
     * about problem posting for concrete period.
     *
     * @param period          period for statistics and part of api address for downloading.
     * @param statisticsItems map for response saving.
     */
    private void getStatisticsItem(final String period, final Map<String, SparseIntArray> statisticsItems) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, STATISTICS_URL + period,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        new AsyncTask<Void, Void, SparseIntArray>() {
                            @Override
                            protected SparseIntArray doInBackground(Void... params) {
                                SparseIntArray statisticsItem = null;
                                try {
                                    statisticsItem = JSONParser.parseStatisticsItem(response);
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSONException in getStatisticsItem");
                                }
                                return statisticsItem;
                            }

                            @Override
                            protected void onPostExecute(SparseIntArray result) {
                                addStatisticsItem(statisticsItems, period, result);
                                if (isStatisticsObjectReady(statisticsItems)) {
                                    Statistics statistics = new Statistics(statisticsItems);
                                    dataResponseReceiver.setStatisticsResponse(statistics);
                                }
                            }
                        }.execute();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse in getStatisticsItem");
                addStatisticsItem(statisticsItems, period, null);
                if (isStatisticsObjectReady(statisticsItems)) {
                    Statistics statistics = new Statistics(statisticsItems);
                    dataResponseReceiver.setStatisticsResponse(statistics);
                }
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Adds response with statistics information about problem posting for certain period to
     * resulting map.
     *
     * @param statisticsItems resulting map.
     * @param period          certain period.
     * @param statisticsItem  server responce.
     */
    private synchronized void addStatisticsItem(final Map<String, SparseIntArray> statisticsItems,
                                                final String period,
                                                final SparseIntArray statisticsItem) {
        statisticsItems.put(period, statisticsItem);
    }

    /**
     * Checks if all information about statistics is ready.
     *
     * @param statisticsItems all downloaded period for statistics.
     * @return is information ready.
     */
    private synchronized boolean isStatisticsObjectReady(final Map<String, SparseIntArray> statisticsItems) {
        return statisticsItems.size() == STATISTIC_ITEMS_COUNT;
    }

}