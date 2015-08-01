package com.ecomap.ukraine.updating.serverclient;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.RequestReceiver;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;

import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.updating.convertion.JSONFields;
import com.ecomap.ukraine.updating.convertion.JSONParser;

import org.json.JSONException;

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

    private static final String PHOTOS_URL = "http://ecomap.org/photos/large/";

    private static final String LOG_IN_URL = "http://ecomap.org/api/login/";

    private static final String LOG_OUT_URL = "http://ecomap.org/api/logout/";

    /**
     * Application context.
     */
    private Context context;

    /**
     * Request receiver.
     */
    private RequestReceiver requestReceiver;

    /**
     * Constructor of LoadingClient.
     *
     * @param requestReceiver  request receiver.
     * @param context application context.
     */
    public LoadingClient(RequestReceiver requestReceiver, Context context) {
        this.requestReceiver = requestReceiver;
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
                            requestReceiver.setAllProblemsRequestResult(
                                    new JSONParser().parseBriefProblems(response));
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in LoadingClient");
                            requestReceiver.setAllProblemsRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestReceiver.setAllProblemsRequestResult(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Sends a request to download detailed information
     * about concrete problem.
     *
     *  @param problemId id of concrete problem
     */
    public void getProblemDetail(final int problemId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_PROBLEMS_URL
                + problemId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Details details = new JSONParser().parseDetailedProblem(response);
                            getPhotos(details);
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in getProblemDetail");
                            requestReceiver.setProblemDetailsRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error response", "onErrorResponse in getProblemDetail");
                requestReceiver.setProblemDetailsRequestResult(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * TODO docs
     *
     * @param password
     * @param login
     */
    public void postLogIn(final String password, final String login) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOG_IN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            User user = new JSONParser().parseUserInformation(response);
                            requestReceiver.setLogInRequestResult(user);
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in LogInUser");
                            requestReceiver.setLogInRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error response", "onErrorResponse");
                requestReceiver.setLogInRequestResult(null);
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
                            requestReceiver.setLogOutRequestResult(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error response", "onErrorResponse log out");
                requestReceiver.setLogOutRequestResult(false);
            }
        });

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Adds Bitmap to corresponding photos in details object.
     * @param details details that contains
     */
    private void getPhotos(final Details details) {
        final Map<Photo, Bitmap> photos = details.photos;
        for (final Photo photo : photos.keySet()) {
            ImageRequest imageRequest = new ImageRequest(PHOTOS_URL + photo.getLink(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        photos.put(photo, bitmap);
                        if (allInitialized(photos)) {
                           requestReceiver.setProblemDetailsRequestResult(details);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        photos.put(photo, BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.photo_error1));
                        if (allInitialized(photos)) {
                            requestReceiver.setProblemDetailsRequestResult(details);
                        }
                    }
                });
            RequestQueueWrapper.getInstance(context).addToRequestQueue(imageRequest);
        }
    }

    private boolean allInitialized(Map<Photo, Bitmap> photos) {
        for (Photo photo : photos.keySet()) {
            if (photos.get(photo) == null) {
                return false;
            }
        }
        return true;
    }

}