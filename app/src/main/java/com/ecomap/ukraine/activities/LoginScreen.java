package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

public class LoginScreen extends Activity implements LogInListener {

    private Intent intent;

    private AccountManager accountManager;

    private LoginButton loginButton;

    private SharedPreferences sharedPreferences;

    private CallbackManager callbackManager;

    /**
     * Initialize activity
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        sharedPreferences = this.getSharedPreferences("security", MODE_PRIVATE);
        setContentView(R.layout.login_activity);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.facebook_button);
        final LoginManager loginManager = LoginManager.getInstance();
        String s = sharedPreferences.getString("token", "fuckedUp");
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookLogIn(loginResult);
            }

            @Override
            public void onCancel() {
                Log.e("login", "facebook login canceled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("login", "facebook login error", e);
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user"));

        accountManager = accountManager.getInstance(getApplicationContext());
        accountManager.registerLogInListener(this);
        intent = new Intent(this, MainActivity.class);

        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openMainActivity();
                                    }
                                }
        );

        final Intent signUpIntent = new Intent(this, SignupActivity.class);
        View signUp = findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(signUpIntent);

                    }
                }
        );
    }

    private void openMainActivity() {
        onDestroy();
        startActivity(intent);
    }
    private void saveLoginResult(LoginResult loginResult) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", loginResult.getAccessToken().getToken());
        editor.putString("userId", loginResult.getAccessToken().getUserId());
        editor.putString("AccessToken", loginResult.getAccessToken().toString());
        editor.apply();
    }

    public void LogIn(View view) {
        EditText password = (EditText) findViewById(R.id.Password);
        EditText login = (EditText) findViewById(R.id.Login);
        accountManager.logInUser(password.getText().toString(),
                login.getText().toString());
    }

    @Override
    public void setLogInResult(User user) {
        if (user != null) {
            intent.putExtra("User", user);
            openMainActivity();
        } else {
            Log.e("log in", "null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogIn(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        try {
                            jsonObject.getString("id");
                        } catch (JSONException e){
                            Log.e("parsing", "invalid response from server", e);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private long generatePassword(String id) {
        long input = Long.getLong(id);
        return new Random(input + 1).nextLong();
    }

}
