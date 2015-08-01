package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.LogInListener;
import com.ecomap.ukraine.models.User;
import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;

/**
 * Created by Andriy on 31.07.2015.
 */
public class LoginScreen extends Activity implements LogInListener {

    private Intent intent;

    private DataManager dataManager;

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

        dataManager = DataManager.getInstance(getApplicationContext());
        dataManager.registerLogInListener(this);
        intent = new Intent(this, MainActivity.class);

        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openMainActvity();
                                    }
                                }
        );

        final Intent signUpIntent = new Intent(this, SignupActivity.class);
        View signUp = findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onDestroy();
                                                startActivity(signUpIntent);
                                            }
                                        });

                                    }
                                }
        );
    }

    private void openMainActvity() {
        onDestroy();
        startActivity(intent);
    }

    public void LogIn(View view) {
        EditText password = (EditText) findViewById(R.id.Password);
        EditText login = (EditText) findViewById(R.id.Login);
        dataManager.logInUser(password.getText().toString(),
                              login.getText().toString());
    }

    @Override
    public void setLogInResult(User user) {
        if (user != null) {
            intent.putExtra("User", user);
            openMainActvity();
        }
    }

}
