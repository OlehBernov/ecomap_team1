package com.ecomap.ukraine.activities.Authorization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.validation.Validator;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity implements LogInListener {

    private static final String CREATING_ACCOUNT = "Creating Account...";
    private static final String SIGN_UP_FAILED = "Sign Up failed";
    private static final String SIGNUP_MESSAGE = "Please wait...";
    private final String TAG = getClass().getSimpleName();
    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    };
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        nameText.setOnFocusChangeListener(focusChangeListener);
        surnameText.setOnFocusChangeListener(focusChangeListener);
        emailText.setOnFocusChangeListener(focusChangeListener);
        passwordText.setOnFocusChangeListener(focusChangeListener);
        passwordConfirmText.setOnFocusChangeListener(focusChangeListener);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        background = (ScrollView) findViewById(R.id.background_layout);
        scrollView.setVerticalScrollBarEnabled(false);
        background.setVerticalScrollBarEnabled(false);

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        background.setScrollY(((int) (scrollView.getScrollY() * 0.5)));
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
                finish();
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
        new MaterialDialog.Builder(this)
                .title(CREATING_ACCOUNT)
                .content(SIGNUP_MESSAGE)
                .progress(true, 0)
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void setLogInResult(final User user) {
        accountManager.removeLogInListener(this);
        if (user != null) {
            mainIntent.putExtra(ExtraFieldNames.USER, user);
            openMainActivity();
        } else {
            Log.e(TAG, "Registration null");
        }
    }

    private void openMainActivity() {
        startActivity(mainIntent);
        finish();
    }

}