package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.account.manager.LogInListener;
import com.ecomap.ukraine.models.User;
import com.facebook.FacebookSdk;

/**
 * Created by Andriy on 31.07.2015.
 */
public class LoginScreen extends Activity implements LogInListener {

    private Intent intent;

    private AccountManager accountManager;

    /**
     * Initialize activity
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.login_activity);

        accountManager = AccountManager.getInstance(getApplicationContext());
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
        }
    }

}
