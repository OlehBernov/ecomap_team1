package com.ecomap.ukraine.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.authentication.manager.LogInListener;
import com.ecomap.ukraine.util.Keyboard;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.authentication.validator.Validator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Activity which represent registration on server
 */
public class SignupActivity extends AppCompatActivity implements LogInListener {

    private static final String CREATING_ACCOUNT = "Creating Account...";
    private static final String SIGN_UP_MESSAGE = "Please wait...";
    private static final String SIGN_UP_TITLE = "Log in failed.";
    private static final String RETRY = "Retry";
    private static final String CANCEL = "Cancel";

    protected final String TAG = getClass().getSimpleName();

    @InjectView(R.id.input_name) EditText nameText;
    @InjectView(R.id.input_surname) EditText surnameText;
    @InjectView(R.id.input_email) EditText emailText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.input_password_confirmation) EditText passwordConfirmText;
    @InjectView(R.id.btn_signup) Button signUpButton;
    @InjectView(R.id.link_login_p2) TextView loginLink;

    private ScrollView scrollView;
    private ScrollView background;
    private AccountManager accountManager;
    private MaterialDialog signUpProgress;

    /**
     * Register user on server
     */
    public void signUp() {
        boolean isRegistrationValid;
        isRegistrationValid = Validator.registrationValidation(nameText, surnameText, emailText,
                passwordText, passwordConfirmText);
        if (!isRegistrationValid) {
            return;
        }

        signUpButton.setEnabled(false);
        signUpProgress = new MaterialDialog.Builder(this)
                .title(CREATING_ACCOUNT)
                .content(SIGN_UP_MESSAGE)
                .progress(true, 0)
                .cancelable(false)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .show();

        String name = nameText.getText().toString();
        String surname = surnameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        accountManager.registerLogInListener(this);
        accountManager.registerUser(name, surname, email, password);
    }

    /**
     * Initialize activity
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        Keyboard.setOnFocusChangeListener(nameText);
        Keyboard.setOnFocusChangeListener(surnameText);
        Keyboard.setOnFocusChangeListener(emailText);
        Keyboard.setOnFocusChangeListener(passwordText);
        Keyboard.setOnFocusChangeListener(passwordConfirmText);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        background = (ScrollView) findViewById(R.id.background_layout);
        scrollView.setVerticalScrollBarEnabled(false);
        background.setVerticalScrollBarEnabled(false);

        accountManager = AccountManager.getInstance(getApplicationContext());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);
            }
        });
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
     * Receive from server identified user.
     * @param user identified user.
     */
    @Override
    public void onLogInResult(final User user) {
        accountManager.removeLogInListener(this);
        signUpButton.setEnabled(true);
        if (user != null) {
            openMainActivity();

        } else {
            Log.e(TAG, "Registration null");
            showFailureDialog();
        }
    }

    /**
     * Show dialog of failed registration.
     */
    private void showFailureDialog() {
        new MaterialDialog.Builder(this)
                .title(SIGN_UP_TITLE)
                .content(R.string.FAILURE_OF_SIGN_UP)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .negativeColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .cancelable(false)
                .positiveText(RETRY)
                .negativeText(CANCEL).callback(
                new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.cancel();
                        signUp();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        signUpProgress.cancel();
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

}