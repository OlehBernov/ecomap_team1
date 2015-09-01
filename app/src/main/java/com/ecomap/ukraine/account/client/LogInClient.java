package com.ecomap.ukraine.account.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecomap.ukraine.account.convertion.JSONFields;
import com.ecomap.ukraine.account.convertion.JSONParser;
import com.ecomap.ukraine.account.manager.LogRequestReceiver;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.updating.serverclient.RequestQueueWrapper;

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
    private LogRequestReceiver logRequestReceiver;

    /**
     * Constructor
     *
     * @param logRequestReceiver receive request result
     * @param context            appication context
     */
    public LogInClient(final LogRequestReceiver logRequestReceiver, final Context context) {
        this.logRequestReceiver = logRequestReceiver;
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
                            logRequestReceiver.setLogInRequestResult(user);
                            logRequestReceiver.putUserToPreferences(user, password);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException in LogInUser");
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
                            logRequestReceiver.setLogInRequestResult(user);
                            logRequestReceiver.putUserToPreferences(user, password);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException in Registration");
                            logRequestReceiver.setLogInRequestResult(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                logRequestReceiver.setLogInRequestResult(null);
                Log.e(TAG, "onErrorResponse in postRegistration");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(JSONFields.FIRST_NAME, name);
                params.put(JSONFields.LAST_NAME, surname);
                params.put(JSONFields.EMAIL, email);
                params.put(JSONFields.PASSWORD, password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<>();
            }
        };

        RequestQueueWrapper.getInstance(context).addToRequestQueue(stringRequest);
    }

}
