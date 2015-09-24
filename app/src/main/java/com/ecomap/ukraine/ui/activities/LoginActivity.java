package com.ecomap.ukraine.ui.activities;

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
import com.ecomap.ukraine.util.ExtraFieldNames;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.authentication.manager.LogInListener;
import com.ecomap.ukraine.util.Keyboard;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.authentication.validator.Validator;
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

/**
 * Activity which represent identification on server
 */
public class LoginActivity extends AppCompatActivity implements LogInListener {

    private static final String LOGIN_MESSAGE = "Please wait...";
    private static final String LOGIN_TITLE = "Log in";
    private static final String FAILE_TITLE = "Log in failed.";
    private static final String SIGN_UP = "Sign up";
    private static final String CANCEL = "Cancel";

    protected final String TAG = getClass().getSimpleName();

    @InjectView(R.id.input_email_log) EditText emailText;
    @InjectView(R.id.input_password_log) EditText passwordText;
    @InjectView(R.id.btn_log_in) Button logInButton;

    private AccountManager accountManager;
    private CallbackManager callbackManager;
    private MaterialDialog logInProgress;

    /**
     * Identify on server
     */
    public void login() {
        if (!Validator.logInValid(emailText, passwordText)) {
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

    /**
     * Receive from server identified user.
     *
     * @param user identified user.
     */
    @Override
    public void onLogInResult(final User user) {
        logInButton.setEnabled(true);
        if (user != null) {
            openMainActivity();
        } else {
            Log.e(TAG, "null");
            showFailureDialog();
        }
    }

    /**
     * Called when an activity you launched exits.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller.
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                getSharedPreferences(ExtraFieldNames.USER_INFO, MODE_PRIVATE);

        setContentView(R.layout.login_activity);

        ButterKnife.inject(this);
        emailText.setText(sharedPreferences.getString(ExtraFieldNames.LOGIN, ""));
        passwordText.setText(sharedPreferences.getString(ExtraFieldNames.PASSWORD, ""));

        Keyboard.setOnFocusChangeListener(emailText);
        Keyboard.setOnFocusChangeListener(passwordText);

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
        skip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMainActivity();
                    }
                }
        );

        View signUp = findViewById(R.id.sign_up);
        signUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent signUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
                        startActivityForResult(signUpIntent, 1);
                    }
                }
        );
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    public void onDestroy() {
        super.onDestroy();
        accountManager.removeLogInListener(this);
    }

    /**
     * Show dialog of failed registration.
     */
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
                        Intent signUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
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

    /**
     * Opens Main activity
     */
    private void openMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

    /**
     * Registration via facebook
     * @param loginResult
     */
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

    /**
     * Generates random password
     *
     * @param id user id
     * @return generated password
     */
    private long generatePassword(final String id) {
        long input = Long.getLong(id);
        return new Random(input + 1).nextLong();
    }

}
