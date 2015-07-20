package com.ecomap.ukraine.restclient;


import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ecomap.ukraine.convertion.JSONParser;

import org.json.JSONException;

public class RestClient {

    private Object output;
    private String url = "http://ecomap.org/api/problems/";

    public void getAllProblems(Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            output = new JSONParser().parseBriefProblems(response);
                        } catch (JSONException e) {
                            output = null;
                        }

                        DataManager.getInstance().setRequestResult(output);
                        try {
                            DataManager.getInstance().notifyListeners(RequestTypes.ALL_PROBLEMS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                output = null;
            }
        });

        RequestQueueSingeltion.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getProblemDetail(Context context) {

    }


}
