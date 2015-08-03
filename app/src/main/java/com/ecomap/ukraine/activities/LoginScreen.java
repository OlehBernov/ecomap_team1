package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginScreen extends AppCompatActivity implements LogInListener {

    private Intent intent;

    private AccountManager accountManager;

    private LoginButton loginButton;

    private SharedPreferences sharedPreferences;

    private CallbackManager callbackManager;

    private static final String TAG = "LoginActivity";

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                hideKeyboard(v);
            }
        }
    };

    @InjectView(R.id.input_email_log) EditText emailText;
    @InjectView(R.id.input_password_log) EditText passwordText;
    @InjectView(R.id.btn_log_in) Button logInBut;
    @InjectView(R.id.sign_up) TextView signUpLink;

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

        ButterKnife.inject(this);
        emailText.setOnFocusChangeListener(focusChangeListener);
        passwordText.setOnFocusChangeListener(focusChangeListener);

        logInBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

     /*   signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        }); */

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

        View skip = findViewById(R.id.skip_button);
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

    public void login() {
        Log.d(TAG, "login");

        logInBut.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this,
                R.style.Base_V11_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Log in...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        accountManager.logInUser(password, email);

    /*    new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000); */
    }

    private void openMainActivity() {
     //   onDestroy();
        startActivity(intent);
    }

    private void saveLoginResult(LoginResult loginResult) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", loginResult.getAccessToken().getToken());
        editor.putString("userId", loginResult.getAccessToken().getUserId());
        editor.putString("AccessToken", loginResult.getAccessToken().toString());
        editor.apply();
    }

 /*   public void LogIn(View view) {
        EditText password = (EditText) findViewById(R.id.Password);
        EditText login = (EditText) findViewById(R.id.Login);
        accountManager.logInUser(password.getText().toString(),
                login.getText().toString());
    } */

    @Override
    public void setLogInResult(User user) {
        logInBut.setEnabled(true);
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


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
