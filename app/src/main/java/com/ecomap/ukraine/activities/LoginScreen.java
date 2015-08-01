package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;

/**
 * Created by Andriy on 31.07.2015.
 */
public class LoginScreen extends Activity {
    private Intent intent;

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
        intent = new Intent(this, MainActivity.class);
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {
                                       @Override
                                       public void run() {
                                           onDestroy();
                                           startActivity(intent);
                                       }
                                       });

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
}
