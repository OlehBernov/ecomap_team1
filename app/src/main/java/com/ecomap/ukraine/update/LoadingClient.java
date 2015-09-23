package com.ecomap.ukraine.update;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
import com.ecomap.ukraine.update.manager.ProblemRequestReceiver;

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
                                problemRequestReceiver.setTop10RequestResult(result);
                            }
                        }.execute();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse in getTop10");
                problemRequestReceiver.setTop10RequestResult(null);
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
                        problemRequestReceiver.onVoteAdded();
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
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
                problemRequestReceiver);
        postComment.execute(dataJSON);
    }

}