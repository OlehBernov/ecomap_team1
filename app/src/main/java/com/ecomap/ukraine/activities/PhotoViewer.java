package com.ecomap.ukraine.activities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecomap.ukraine.R;

public class PhotoViewer extends Fragment {

    private static Bitmap bitmap;
    private static String description;
    private static FragmentManager fragmentManager;

    public static PhotoViewer newInstance (Bitmap bitmap, String description,
                                           FragmentManager fragmentManager) {
        PhotoViewer.bitmap = bitmap;
        PhotoViewer.description = description;
        PhotoViewer.fragmentManager = fragmentManager;
        return new PhotoViewer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_photo_viewer, container, false);
        ImageView photo = (ImageView) rootView.findViewById(R.id.imageView4);
        photo.setImageBitmap(fillByWidth(bitmap));
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        TextView descriptionView = (TextView) rootView.findViewById(R.id.photo_description);
        descriptionView.setText(description);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    closeFragment();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    private Bitmap fillByWidth(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        float scale = ((float) Math.round((float) displayMetrics.widthPixels * density)) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private void closeFragment() {
        fragmentManager.beginTransaction().remove(PhotoViewer.this).commit();
    }

}
