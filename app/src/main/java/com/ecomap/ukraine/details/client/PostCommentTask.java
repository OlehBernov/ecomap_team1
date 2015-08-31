package com.ecomap.ukraine.details.client;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.details.manager.DetailsRequestReceiver;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Andriy on 31.08.2015.
 */
public class PostCommentTask extends AsyncTask<JSONObject, Void, Void> {

    private static final String POST_COMMENT_URL = "http://ecomap.org/api/comment/";

    private  int problemID;
    private Boolean onResponse = false;
    private Context context;
    private DetailsRequestReceiver detailsRequestReceiver;

    public PostCommentTask (int problemID, Context context,
                            DetailsRequestReceiver detailsRequestReceiver) {
        this.problemID = problemID;
        this.context = context;
        this.detailsRequestReceiver = detailsRequestReceiver;

    }

    @Override
    protected Void doInBackground(JSONObject ... request) {
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(POST_COMMENT_URL + problemID);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.connect();

            connection.getOutputStream().write(request[0].toString().getBytes("UTF-8"));

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                onResponse = true;
                Toast.makeText(context,
                        R.string.success_post_comment, Toast.LENGTH_LONG).show();
            } else {
                Log.e("error response", "onErrorResponse in postComment");
                Toast.makeText(context,
                        R.string.error_of_connection, Toast.LENGTH_LONG).show();
            }
            connection.disconnect();
            return null;
        } catch (Exception e) {
           Log.e("exception", "Exception in postComment");
       }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(onResponse) {
            detailsRequestReceiver.onCommentAdded();
        }
    }
}
