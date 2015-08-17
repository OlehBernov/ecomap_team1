package com.ecomap.ukraine.updating.serverclient;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.ProblemRequestReceiver;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;

import com.ecomap.ukraine.updating.convertion.JSONParser;

import org.json.JSONException;

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
     * @param problemRequestReceiver  request receiver.
     * @param context application context.
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
                    public void onResponse(String response) {
                        try {
                            problemRequestReceiver.setAllProblemsRequestResult(
                                    new JSONParser().parseBriefProblems(response));
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in LoadingClient");
                            problemRequestReceiver.setAllProblemsRequestResult(null);
                        }
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
                            if (isPhotosExist(details)) {
                                getPhotos(details);
                            } else {
                                problemRequestReceiver.setProblemDetailsRequestResult(details);
                            }
                        } catch (JSONException e) {
                            Log.e("exception", "JSONException in getProblemDetail");
                            problemRequestReceiver.setProblemDetailsRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error response", "onErrorResponse in getProblemDetail");
                problemRequestReceiver.setProblemDetailsRequestResult(null);
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    private boolean isPhotosExist(final Details details) {
        return details.getPhotos().size() > 0;
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
                           problemRequestReceiver.setProblemDetailsRequestResult(details);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        photos.put(photo, BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.photo_error1));
                        if (allInitialized(photos)) {
                            problemRequestReceiver.setProblemDetailsRequestResult(details);
                        }
                    }
                });
            RequestQueueWrapper.getInstance(context).addToRequestQueue(imageRequest);
        }
    }

    private boolean allInitialized(final Map<Photo, Bitmap> photos) {
        for (Photo photo : photos.keySet()) {
            if (photos.get(photo) == null) {
                return false;
            }
        }
        return true;
    }

}