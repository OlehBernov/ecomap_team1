package com.ecomap.ukraine.problemposting.RESTclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecomap.ukraine.problemposting.parser.JSONParser;
import com.ecomap.ukraine.problemposting.manager.AddProblemRequestReceiver;
import com.ecomap.ukraine.problemposting.parser.JSONFields;
import com.ecomap.ukraine.model.newproblem.NewProblemData;
import com.ecomap.ukraine.updateproblem.RESTclient.RequestQueueWrapper;

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

    protected final String TAG = getClass().getSimpleName();

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
    public void addProblemDescription(final NewProblemData problemData) {
        HashMap<String, String> params = new HashMap<>();
        final String userId = String.valueOf(problemData.getUser().getId());
        final String userName = problemData.getUser().getName();
        final String userSurname = problemData.getUser().getSurname();

        params.put(JSONFields.TITLE, problemData.getTitle());
        params.put(JSONFields.CONTENT, problemData.getContent());
        params.put(JSONFields.PROPOSAL, problemData.getProposal());
        params.put(JSONFields.LATITUDE, String.valueOf(problemData.getPosition().latitude));
        params.put(JSONFields.LONGITUDE, String.valueOf(problemData.getPosition().longitude));
        params.put(JSONFields.PROBLEM_TYPE, problemData.getType());
        params.put(JSONFields.USER_ID, userId);
        params.put(JSONFields.USER_NAME, userName);
        params.put(JSONFields.USER_SURNAME, userSurname);

        MultipartRequest multipartRequest = new MultipartRequest(POST_PROBLEM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (problemData.getPhotos() != null) {
                    try {
                        String addedProblemID = JSONParser.parseAddedProblemInformation(response);
                        addPhotosToProblem(userId, userName, userSurname,
                                problemData.getPhotoDescriptions(), addedProblemID,
                                problemData.getPhotos());
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException in parsing json from new problem");
                    }
                } else {
                    addProblemRequestReceiver.onSuccesProblemPosting();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
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
                        if (count == size) {
                            addProblemRequestReceiver.onSuccesPhotoPosting();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
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
