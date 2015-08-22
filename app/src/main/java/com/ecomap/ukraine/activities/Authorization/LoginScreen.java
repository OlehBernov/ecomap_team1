package com.ecomap.ukraine.activities.Authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.activities.addProblem.AddProblemDescriptionFragment;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.activities.Keyboard;
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

    private static final String LOGIN_MESSAGE = "Please wait...";
    private static final String LOGIN_TITLE = "Log in";
    private static final String FAILE_TITLE = "Log in failed.";
    private final static String SIGN_UP = "Sign up";
    private final static String CANCEL = "Cancel";

    private Intent mainIntent;
    private AccountManager accountManager;
    private CallbackManager callbackManager;
    private MaterialDialog logInProgress;

    protected final String TAG = getClass().getSimpleName();

    @InjectView(R.id.input_email_log) EditText emailText;
    @InjectView(R.id.input_password_log) EditText passwordText;
    @InjectView(R.id.btn_log_in) Button logInButton;

    public void login() {
        boolean isLogInValid;
        isLogInValid = new Validator().logInValid(emailText, passwordText);
        if (!isLogInValid) {
            return;
        }
        logInButton.setEnabled(false);

        logInProgress = new MaterialDialog.Builder(this)
                .title(LOGIN_TITLE)
                .content(LOGIN_MESSAGE)
                .progress(true, 0)
                .cancelable(false)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        accountManager.registerLogInListener(this);
        accountManager.logInUser(password, email);
    }

    @Override
    public void setLogInResult(final User user) {
        logInButton.setEnabled(true);
        if (user != null) {
            openMainActivity();
        } else {
            Log.e(TAG, "null");
            showFailureDialog();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Initialize activity
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        SharedPreferences sharedPreferences =
                getSharedPreferences(AccountManager.USER_INFO, MODE_PRIVATE);

        setContentView(R.layout.login_activity);
        mainIntent = new Intent(this, MainActivity.class);

        ButterKnife.inject(this);
        emailText.setText(sharedPreferences.getString(AccountManager.LOGIN, ""));
        passwordText.setText(sharedPreferences.getString(AccountManager.PASSWORD, ""));

        Keyboard keyboard = new Keyboard(this);
        keyboard.setOnFocusChangeListener(emailText);
        keyboard.setOnFocusChangeListener(passwordText);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        LoginButton logInFacebookButton = (LoginButton) findViewById(R.id.facebook_button);
        logInFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookLogIn(loginResult);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "facebook login canceled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "facebook login error", e);
            }
        });
        accountManager = AccountManager.getInstance(getApplicationContext());

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

    public void onDestroy() {
        super.onDestroy();
        accountManager.removeLogInListener(this);
    }

    private void showFailureDialog() {
        new MaterialDialog.Builder(this)
                .title(FAILE_TITLE)
                .content(R.string.FAILURE_OF_LOG_IN)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .negativeColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .cancelable(false)
                .positiveText(SIGN_UP)
                .negativeText(CANCEL).callback(
                new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.cancel();
                        logInProgress.cancel();
                        Intent signUpIntent = new Intent(LoginScreen.this, SignupActivity.class);
                        startActivity(signUpIntent);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        logInProgress.cancel();
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void openMainActivity() {
        startActivity(mainIntent);
        finish();
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

}
