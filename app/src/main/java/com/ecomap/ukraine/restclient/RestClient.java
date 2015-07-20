package com.ecomap.ukraine.restclient;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.convertion.JSONParser;

import org.json.JSONException;

public class RestClient {

    private static final String URL = "http://ecomap.org/api/problems/";
    private Object output;
    private Context context;

    public void getAllProblems(Context context) {
        this.context = context;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            output = new JSONParser().parseBriefProblems(response);
                        } catch (JSONException e) {
                            output = null;
                        } finally {
                            DataManager.getInstance().notifyListeners(RequestTypes.ALL_PROBLEMS,
                                    output);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                output = null;
                DataManager.getInstance().notifyListeners(RequestTypes.ALL_PROBLEMS,
                        output);
            }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getProblemDetail(int problemId, Context context) {
        this.context = context;
    }

}