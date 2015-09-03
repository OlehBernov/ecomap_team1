package com.ecomap.ukraine.problemposting;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.helper.BitmapResizer;
import com.ecomap.ukraine.helper.ExtraFieldNames;

/**
 * Contains
 */
public class UserPhotoFullScreen extends AppCompatActivity {

    /**
     * Sets user photo on full screen mode.
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo_full_screen);
        ImageView userPhoto = (ImageView) findViewById(R.id.user_photo);
        String photoPath = getIntent().getStringExtra(ExtraFieldNames.PHOTO);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        Bitmap photoBitmap = BitmapResizer.changePhotoOrientation(photoPath, screenWidth);
        userPhoto.setImageBitmap(photoBitmap);
    }

}
