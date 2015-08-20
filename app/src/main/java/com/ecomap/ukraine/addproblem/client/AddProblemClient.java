package com.ecomap.ukraine.addproblem.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecomap.ukraine.addproblem.convertion.JSONParser;
import com.ecomap.ukraine.addproblem.manager.AddProblemRequestReceiver;
import com.ecomap.ukraine.addproblem.convertion.JSONFields;
import com.ecomap.ukraine.updating.serverclient.RequestQueueWrapper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
/**
 * Performs posting problems on server.
 */
public class AddProblemClient {

    /**
     * Address of the server on which LoadingClient post
     * information about new problems.
     */
    private static final String POST_PROBLEM_URL = "http://ecomap.org/api/problempost/";

    /**
     * Address of the server on which LoadingClient post
     * photos of new problems.
     */
    private static final String POST_PHOTO_URL = "http://ecomap.org/api/photo/";

    /**
     * Application context.
     */
    private Context context;

    /**
     * Request receiver.
     */
    private AddProblemRequestReceiver addProblemRequestReceiver;

    /**
     * Constructor
     *
     * @param addProblemRequestReceiver receive request result
     * @param context                   appication context
     */
    public AddProblemClient(final AddProblemRequestReceiver addProblemRequestReceiver,
                            final Context context) {
        this.addProblemRequestReceiver = addProblemRequestReceiver;
        this.context = context;
    }

    /**
     * Sends a request to post information about new problems.
     */
    public void addProblemDescription(final String title, final String content, final String proposal,
                                      final String latitude, final String longitude, final String type, final String userId,
                                      final String userName, final String userSurname, final List<Bitmap> bitmaps,
                                      final List<String> photoDescriptions) {
        HashMap<String, String> params = new HashMap<>();

        params.put(JSONFields.TITLE, title);
        params.put(JSONFields.CONTENT, content);
        params.put(JSONFields.PROPOSAL, proposal);
        params.put(JSONFields.LATITUDE, latitude);
        params.put(JSONFields.LONGITUDE, longitude);
        params.put(JSONFields.PROBLEM_TYPE, type);
        params.put(JSONFields.USER_ID, userId);
        params.put(JSONFields.USER_NAME, userName);
        params.put(JSONFields.USER_SURNAME, userSurname);

        MultipartRequest multipartRequest = new MultipartRequest(POST_PROBLEM_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                if (bitmaps != null) {
                    try {
                        JSONParser jsonParser = new JSONParser();
                        String addedProblemID = jsonParser.parseAddedProblemInformation(response);
                        addPhotosToProblem(userId, userName, userSurname,
                                photoDescriptions, addedProblemID, bitmaps);
                    } catch (JSONException e) {
                        Log.e("JSONException", "JSONException in parsing json from new problem");
                    }
                } else {
                    addProblemRequestReceiver.onSuccesProblemPosting();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Request Error", error.getLocalizedMessage());
                addProblemRequestReceiver.onFailedProblemPosting();
            }

        }, null, params, 0);

        RequestQueueWrapper.getInstance(context).addToRequestQueue(multipartRequest);

    }

    /**
     * Sends a request to post photo of new problems.
     */
    public void addPhotoToProblem(final String userID, final String userName,
                                  final String userSurname, final String description,
                                  final String problemID, Bitmap photo, final int count, final int size) {


        HashMap<String, String> params = new HashMap<>();
        params.put(JSONFields.USER_ID, userID);
        params.put(JSONFields.USER_NAME, userName);
        params.put(JSONFields.USER_SURNAME, userSurname);
        params.put(JSONFields.DESCRIPTION, description);
        MultipartRequest multipartRequest = new MultipartRequest(POST_PHOTO_URL + problemID + "/",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        if (count == size) {
                            addProblemRequestReceiver.onSuccesPhotoPosting();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Request Error", error.getLocalizedMessage());
                if (count == size) {
                    addProblemRequestReceiver.onFailedPhotoPosting();
                }
            }

        }, photo, params, count);

        RequestQueueWrapper.getInstance(context).addToRequestQueue(multipartRequest);
    }

    /**
     * Sends all  photos of new problems.
     */
    public void addPhotosToProblem(final String userID, final String userName,
                                   final String userSurname, final List<String> descriptions,
                                   final String problemID, final List<Bitmap> photos) {
        final int size = photos.size();
        for (int i = 1; i <= descriptions.size(); i++) {
            addPhotoToProblem(userID, userName, userSurname, descriptions.get(i - 1),
                    problemID, photos.get(i - 1), i, size);
        }
    }

}
