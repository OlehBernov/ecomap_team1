package com.ecomap.ukraine.account.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.account.manager.LogRequestReceiver;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.updating.convertion.JSONFields;
import com.ecomap.ukraine.updating.convertion.JSONParser;
import com.ecomap.ukraine.updating.serverclient.RequestQueueWrapper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander on 02.08.2015.
 */
public class LogInClient {

    private static final String LOG_IN_URL = "http://ecomap.org/api/login/";

    private static final String LOG_OUT_URL = "http://ecomap.org/api/logout/";

    private Context context;

    private LogRequestReceiver logRequestReceiver;

    public LogInClient(LogRequestReceiver logRequestReceiver, Context context) {
        this.logRequestReceiver = logRequestReceiver;
        this.context = context;
    }

    public void postLogIn(final String password, final String login) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOG_IN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            User user = new JSONParser().parseUserInformation(response);
                            logRequestReceiver.setLogInRequestResult(user);
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in LogInUser");
                            logRequestReceiver.setLogInRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                logRequestReceiver.setLogInRequestResult(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put(JSONFields.EMAIL, login);
                params.put(JSONFields.PASSWORD, password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * TODO
     */
    public void getLogOut() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOG_OUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        logRequestReceiver.setLogOutRequestResult(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                logRequestReceiver.setLogOutRequestResult(false);
            }
        });

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

}