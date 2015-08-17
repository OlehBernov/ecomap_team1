package com.ecomap.ukraine.activities.Authorization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.activities.addProblem.AddProblemDescriptionFragment;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.validation.Validator;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginScreen extends AppCompatActivity implements LogInListener {

    private Intent mainIntent;

    private AccountManager accountManager;

    private LoginButton logInFacebookButton;

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
    @InjectView(R.id.btn_log_in) Button logInButton;

    /**
     * Initialize activity
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        sharedPreferences = this.getSharedPreferences(AccountManager.USER_INFO, MODE_PRIVATE);

        setContentView(R.layout.login_activity);
        mainIntent = new Intent(this, MainActivity.class);

        ButterKnife.inject(this);
        emailText.setText(sharedPreferences.getString(AccountManager.LOGIN, ""));
        passwordText.setText(sharedPreferences.getString(AccountManager.PASSWORD, ""));
        emailText.setOnFocusChangeListener(focusChangeListener);
        passwordText.setOnFocusChangeListener(focusChangeListener);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        logInFacebookButton = (LoginButton)findViewById(R.id.facebook_button);
        logInFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
        accountManager = accountManager.getInstance(getApplicationContext());

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

        boolean isLogInValid;
        isLogInValid = new Validator().logInValid(emailText, passwordText);
        if (!isLogInValid) {
            return;
        }

        logInButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this,
                android.R.style.Theme_Holo_Light_Panel);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Log in...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        accountManager.registerLogInListener(AddProblemDescriptionFragment.getInstance(null));
        accountManager.registerLogInListener(this);
        accountManager.logInUser(password, email);

    }


    @Override
    public void setLogInResult(final User user) {
        logInButton.setEnabled(true);
        if (user != null) {
            mainIntent.putExtra("User", user);
            openMainActivity();
        } else {
            Log.e("log in", "null");
        }
    }

    private void openMainActivity() {
        startActivity(mainIntent);

        finish();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogIn(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        try {
                            jsonObject.getString("id");
                        } catch (JSONException e) {
                            Log.e("parsing", "invalid response from server", e);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private long generatePassword(final String id) {
        long input = Long.getLong(id);
        return new Random(input + 1).nextLong();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onDestroy() {
        super.onDestroy();
        accountManager.removeLogInListener(this);
    }

}
