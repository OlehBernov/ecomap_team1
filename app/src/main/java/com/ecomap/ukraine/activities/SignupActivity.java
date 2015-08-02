package com.ecomap.ukraine.activities;

import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecomap.ukraine.R;

import butterknife.ButterKnife;
        import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                hideKeyboard(v);
            }
        }
    };

    @InjectView(R.id.input_name) EditText nameText;
    @InjectView(R.id.input_surname) EditText surnameText;
    @InjectView(R.id.input_email) EditText emailText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.input_password_confirmation) EditText passwordConfirmText;
    @InjectView(R.id.btn_signup) Button signUpButton;
    @InjectView(R.id.link_login_p2) TextView loginLink;

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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        signUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Base_V11_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String surname = surnameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signUpButton.setEnabled(true);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                =(InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}