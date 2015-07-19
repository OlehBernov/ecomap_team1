package com.ecomap.ukraine.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ecomap.ukraine.R;

public class Splashscreen extends Activity {

    private static int splashInterval = 2000;
    private AnimationDrawable animationDrawable;
    private ImageView fourSquare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(Splashscreen.this, MainActivity.class);
                startActivity(i);
                fourSquare = (ImageView) findViewById(R.id.fourSquare);
                animationDrawable = (AnimationDrawable) fourSquare.getDrawable();
                animationDrawable.start();
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub
            }
        }, splashInterval);
    };
}
