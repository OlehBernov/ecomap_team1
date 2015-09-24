package com.ecomap.ukraine.update;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.update.manager.DataResponseReceiver;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask which post comment to server
 */
public class PostCommentTask extends AsyncTask<JSONObject, Void, Boolean> {

    /**
     * URL of posting comment
     */
    private static final String POST_COMMENT_URL = "http://ecomap.org/api/comment/";

    protected final String TAG = getClass().getSimpleName();

    private int problemID;
    private Context context;
    private DataResponseReceiver dataResponseReceiver;

    /**
     * Constructor
     * @param problemID problem id
     * @param context application context
     * @param dataResponseReceiver receiver of request
     */
    public PostCommentTask(int problemID, Context context,
                           DataResponseReceiver dataResponseReceiver) {
        this.problemID = problemID;
        this.context = context;
        this.dataResponseReceiver = dataResponseReceiver;
    }

    /**
     * Posting comment in background
     * @param request body of request
     * @return result of request
     */
    @Override
    protected Boolean doInBackground(JSONObject ... request) {
        URL url;
        HttpURLConnection connection;
        boolean result = false;
        try {
            url = new URL(POST_COMMENT_URL + problemID);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.connect();

            connection.getOutputStream().write(request[0].toString().getBytes("UTF-8"));

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = true;
                Toast.makeText(context,
                        R.string.success_post_comment, Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "onErrorResponse in postComment");
                Toast.makeText(context,
                        R.string.error_of_connection, Toast.LENGTH_LONG).show();
            }
            connection.disconnect();
            return result;
        } catch (Exception e) {
           Log.e(TAG, "Exception in postComment");
       }
        return result;
    }

    /**
     * Action after posting
     * @param result result of request
     */
    @Override
    protected void onPostExecute(Boolean result) {
        if(result) {
            dataResponseReceiver.onCommentAdded();
        }
    }
}
