package com.ecomap.ukraine.activities.Authorization;

import android.content.ComponentName;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.activities.Keyboard;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.validation.Validator;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity implements LogInListener {

    private static final String MAIN_ACTIVITY = "com.ecomap.ukraine.activities.main.MainActivity";
    private static final String CREATING_ACCOUNT = "Creating Account...";
    private static final String SIGN_UP_FAILED = "Sign Up failed";
    private static final String SIGN_UP_MESSAGE = "Please wait...";
    private static final String SIGN_UP_TITLE = "Log in failed.";
    private final static String RETRY = "Retry";
    private final static String CANCEL = "Cancel";
    private final String TAG = getClass().getSimpleName();
    @InjectView(R.id.input_name)
    EditText nameText;
    @InjectView(R.id.input_surname)
    EditText surnameText;
    @InjectView(R.id.input_email)
    EditText emailText;
    @InjectView(R.id.input_password)
    EditText passwordText;
    @InjectView(R.id.input_password_confirmation)
    EditText passwordConfirmText;
    @InjectView(R.id.btn_signup)
    Button signUpButton;
    @InjectView(R.id.link_login_p2)
    TextView loginLink;
    private ScrollView scrollView;
    private ScrollView background;
    private Intent mainIntent;
    private AccountManager accountManager;
    private MaterialDialog signUpProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        Keyboard keyboard = new Keyboard(this);
        keyboard.setOnFocusChangeListener(nameText);
        keyboard.setOnFocusChangeListener(surnameText);
        keyboard.setOnFocusChangeListener(emailText);
        keyboard.setOnFocusChangeListener(passwordText);
        keyboard.setOnFocusChangeListener(passwordConfirmText);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        background = (ScrollView) findViewById(R.id.background_layout);
        scrollView.setVerticalScrollBarEnabled(false);
        background.setVerticalScrollBarEnabled(false);

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        background.setScrollY((int) (scrollView.getScrollY() * 0.5));
                    }
                });

        mainIntent = new Intent(this, MainActivity.class);

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
                Intent intent = new Intent(SignupActivity.this, LoginScreen.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void signUp() {
        boolean isRegistrationValid;
        isRegistrationValid = new Validator().registrationValidation(nameText, surnameText, emailText,
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

    public void onSignUpSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), SIGN_UP_FAILED, Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void setLogInResult(final User user) {
        accountManager.removeLogInListener(this);
        signUpButton.setEnabled(true);
        if (user != null) {
            if (isCameFromMainActivity()) {
                finish();
            } else {
                openMainActivity();
            }
        } else {
            Log.e(TAG, "Registration null");
            showFailureDialog();
        }
    }

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

    private void openMainActivity() {
        startActivity(mainIntent);
        finish();
    }

    private boolean isCameFromMainActivity() {
        ComponentName componentName = getCallingActivity();
        return (componentName != null) && componentName.getClassName().equals(MAIN_ACTIVITY);
    }

}