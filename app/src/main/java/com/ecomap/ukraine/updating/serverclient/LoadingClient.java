package com.ecomap.ukraine.updating.serverclient;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.ListenersNotifier;
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
     * Server response converted to the entity.
     */
    private Object requestResult;

    /**
     * Object, which able to notify listeners
     * about result.
     */
    private ListenersNotifier listenersNotifier;

    /**
     * Constructor of LoadingClient.
     *
     * @param listenersNotifier able to notify listeners about result.
     */
    public LoadingClient(ListenersNotifier listenersNotifier) {
        this.listenersNotifier = listenersNotifier;
    }

    /**
     * Sends a request to download brief information
     * about all problems.
     *  @param context application context.
     *
     */
    public void getAllProblems(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_PROBLEMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            requestResult = new JSONParser().parseBriefProblems(response);
                        } catch (JSONException e) {
                            requestResult = null;
                        } finally {
                            listenersNotifier.notifyListeners(RequestTypes.ALL_PROBLEMS,
                                    requestResult);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestResult = null;
                listenersNotifier.notifyListeners(RequestTypes.ALL_PROBLEMS,
                        requestResult);
            }
        });

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Sends a request to download detailed information
     * about concrete problem.
     *  @param problemId id of concrete problem
     * @param context context of application.
     */
    public void getProblemDetail(final int problemId, final Context context) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_PROBLEMS_URL
                + problemId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Details details = new JSONParser().parseDetailedProblem(response);
                            getPhotos(context, details);
                        } catch (JSONException e) {
                            requestResult = null;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestResult = null;
            }
        });
        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Adds Bitmap values to corresponding photos in details object.
     * @param context context of application.
     * @param details details that contains
     */
    private void getPhotos(final Context context, final Details details) {
        final Map<Photo, Bitmap> photos = details.photos;
        for (final Photo photo : photos.keySet()) {
            ImageRequest imageRequest = new ImageRequest(PHOTOS_URL + photo.getLink(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        photos.put(photo, bitmap);
                        if (allInitialized(photos)) {
                            listenersNotifier.notifyListeners(RequestTypes.PROBLEM_DETAIL,
                                    details);
                        }
                    }

                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        photos.put(photo, BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.photo_error1));
                        if (allInitialized(photos)) {
                            listenersNotifier.notifyListeners(RequestTypes.PROBLEM_DETAIL,
                                    details);
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