package com.ecomap.ukraine.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


public class BitmapResizer {

    private Context context;

    public BitmapResizer(final Context context) {
        this.context = context;
    }

    public Bitmap resizeBitmap(final Bitmap bitmap, final int bounds) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(bounds);
        float yScale = ((float) bounding) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(yScale, yScale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private int dpToPx(final int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public Bitmap scalePhoto(final String photoPath, final int bounds) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/ bounds, photoH/ bounds);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

}
