package com.ecomap.ukraine.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;


public class BitmapResizer {

    private static final int DEGREE_90 = 90;

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

    public Bitmap changePhotoOrientation(String photoPath, int bounds) {
        BitmapResizer bitmapResizer =  new BitmapResizer(context);
        Bitmap photoBitmap = bitmapResizer.scalePhoto(photoPath, bounds);
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(photoPath);
        } catch (IOException e) {
            Log.e("IOException", "change photo orientation");
            return photoBitmap;
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            Matrix matrix = new Matrix();
            matrix.postRotate(DEGREE_90);
            return Bitmap.createBitmap(photoBitmap, 0, 0, photoBitmap.getWidth(),
                    photoBitmap.getHeight(), matrix, true);
        } else {
            return photoBitmap;
        }
    }

    private int dpToPx(final int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
