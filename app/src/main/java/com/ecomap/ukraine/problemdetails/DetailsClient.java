package com.ecomap.ukraine.problemdetails;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.problemdetails.manager.DetailsRequestReceiver;
import com.ecomap.ukraine.problemupdate.RequestQueueWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DetailsClient {

    private static final String POST_VOTE_URL = "http://ecomap.org/api/vote";

    protected final String TAG = getClass().getSimpleName();

    private Context context;

    private DetailsRequestReceiver detailsRequestReceiver;

    /**
     * Constructor
     *
     * @param detailsRequestReceiver receive request result
     * @param context application context
     */
    public DetailsClient(final DetailsRequestReceiver detailsRequestReceiver, final Context context) {
        this.detailsRequestReceiver = detailsRequestReceiver;
        this.context = context;
    }

    /**
     * Post vote of problem on server
     * @param problemID problem id
     * @param userID user id
     * @param userName user name
     * @param userSurname user surname
     */
    public void postVote(final String problemID, final String userID,
                         final String userName, final String userSurname) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_VOTE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        detailsRequestReceiver.onVoteAdded();
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
                if(!accountManager.isAnonymousUser()) {
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
     * @param problemID problem id
     * @param userID user id
     * @param userName user name
     * @param userSurname user surname
     * @param content content of content
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
                detailsRequestReceiver);
        postComment.execute(dataJSON);
    }
}