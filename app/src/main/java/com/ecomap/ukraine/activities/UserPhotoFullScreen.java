package com.ecomap.ukraine.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ecomap.ukraine.R;

public class UserPhotoFullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo_full_screen);
        ImageView userPhoto = (ImageView) findViewById(R.id.user_photo);
        byte[] photoByteArray = getIntent().getByteArrayExtra("photo");
        Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.length);
        BitmapResizer bitmapResizer = new BitmapResizer(getApplicationContext());
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        photoBitmap = bitmapResizer.resizeBitmap(photoBitmap, screenWidth);
        userPhoto.setImageBitmap(photoBitmap);
    }

}