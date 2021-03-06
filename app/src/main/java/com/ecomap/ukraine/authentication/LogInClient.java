package com.ecomap.ukraine.authentication;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.authentication.manager.LogInResponseReceiver;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.update.RequestQueueWrapper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Performs identification on server.
 */

public class LogInClient {

    /**
     * Address of the server on which LoginClient post
     * information for identification on server.
     */
    private static final String LOG_IN_URL = "http://ecomap.org/api/login/";

    /**
     * Address of the server on which LoginClient post
     * information for registration on server.
     */
    private static final String REGISTRATION_URL = "http://ecomap.org/api/register/";

    /**
     * TAG with name of class
     */
    protected final String TAG = getClass().getSimpleName();

    /**
     * Application context
     */
    private Context context;

    /**
     * Request receiver.
     */
    private LogInResponseReceiver logInResponseReceiver;

    /**
     * Constructor
     *
     * @param logInResponseReceiver receive request result
     * @param context            application context
     */
    public LogInClient(final LogInResponseReceiver logInResponseReceiver, final Context context) {
        this.logInResponseReceiver = logInResponseReceiver;
        this.context = context;
    }

    /**
     * Sends request to login user
     *
     * @param password account password
     * @param login    user login
     */
    public void postLogIn(final String password, final String login) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOG_IN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            User user = JSONParser.parseUserInformation(response);
                            logInResponseReceiver.getLogInResponseResult(user);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException in LogInUser");
                            logInResponseReceiver.getLogInResponseResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                logInResponseReceiver.getLogInResponseResult(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(JSONFields.EMAIL, login);
                params.put(JSONFields.PASSWORD, password);
                return params;
            }
        };

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * @param name     user name
     * @param surname  user surname
     * @param email    user email
     * @param password account password
     */
    public void postRegistration(final String name, final String surname,
                                 final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            User user = JSONParser.parseRegistrationInformation(response, email);
                            logInResponseReceiver.getLogInResponseResult(user);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException in Registration");
                            logInResponseReceiver.getLogInResponseResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                logInResponseReceiver.getLogInResponseResult(null);
                Log.e(TAG, "onErrorResponse in postRegistration");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(JSONFields.FIRST_NAME, name);
                params.put(JSONFields.LAST_NAME, surname);
                params.put(JSONFields.EMAIL, email);
                params.put(JSONFields.PASSWORD, password);
                return params;
            }
        };

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

}
