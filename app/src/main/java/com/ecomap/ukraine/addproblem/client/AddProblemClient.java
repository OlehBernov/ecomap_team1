package com.ecomap.ukraine.addproblem.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecomap.ukraine.addproblem.manager.AddProblemRequestReceiver;
import com.ecomap.ukraine.updating.serverclient.RequestQueueWrapper;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Andriy on 12.08.2015.
 */
public class AddProblemClient {

    private static final String POST_PROBLEM_URL = "http://ecomap.org/api/problempost/";

    private static final String POST_PHOTO_URL = "http://ecomap.org/api/photo/";

    private Context context;

    private AddProblemRequestReceiver addProblemRequestReceiver;

    /**
     * Constructor
     * @param addProblemRequestReceiver receive request result
     * @param context appication context
     */
    public AddProblemClient(final AddProblemRequestReceiver addProblemRequestReceiver, final Context context) {
        this.addProblemRequestReceiver = addProblemRequestReceiver;
        this.context = context;
    }

    public void addProblemDescription (final String title, final String content, final String proposal,
                                       final String latitude, final String longitude, final String type,
                                       final String userId, final String userName, final String userSurname) {
        HashMap<String, String> params = new HashMap<String, String>();

        File file = null;
        params.put("title", title);
        params.put("content", content);
        params.put("proposal",  proposal);
        params.put("latitude",  latitude);
        params.put("longitude",  longitude);
        params.put("type",  type);
        params.put("userId",  userId);
        params.put("userName",  userName);
        params.put("userSurname", userSurname);



        MultipartRequest multipartRequest = new MultipartRequest(POST_PROBLEM_URL, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                addProblemRequestReceiver.setAddProblemRequestResult(true);
            }

        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Request Error", error.getLocalizedMessage());
                addProblemRequestReceiver.setAddProblemRequestResult(false);
            }

        }, file, params);

        RequestQueueWrapper.getInstance(context).addToRequestQueue(multipartRequest);


    }

    public void addPhotoToProblem (final String userID, final String userName,
                                   final String userSurname , final String description, File file,
                                   String problemID){

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", userID);
        params.put("userName", userName);
        params.put("userSurname",  userSurname);
        params.put("description",  description);
        params.put("null",  "Додати");
        params.put("null",  "Звернути");

        MultipartRequest multipartRequest = new MultipartRequest(POST_PHOTO_URL + problemID + "/",
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                    }

                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Request Error", error.getLocalizedMessage());
            }

        }, file, params);

        RequestQueueWrapper.getInstance(context).addToRequestQueue(multipartRequest);


    }


}
