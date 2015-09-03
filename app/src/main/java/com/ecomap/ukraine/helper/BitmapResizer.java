package com.ecomap.ukraine.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Performs photo resizing.
 */
public final class BitmapResizer {

    /**
     * Rotation degree for horizontal photo.
     */
    private static final int DEGREE_90 = 90;

    private BitmapResizer(){}

    /**
     * Changes bitmap size according to bounds.
     *
     * @param bitmap photo bitmap.
     * @param bounds bounds of result photo.
     * @param context context of the application.
     * @return bitmap of photo with changed size.
     */
    public static Bitmap resizeBitmap(final Bitmap bitmap, final int bounds, final Context context) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(bounds, context);
        float yScale = (float) bounding / height;

        Matrix matrix = new Matrix();
        matrix.postScale(yScale, yScale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * Changes photo size according to bounds.
     *
     * @param photoPath path to photo.
     * @param bounds bounds of result photo.
     * @return bitmap of photo with changed size.
     */
    public static Bitmap scalePhoto(final String photoPath, final int bounds) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / bounds, photoH / bounds);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    /**
     * Calls changing of photo size. Perform photo rotation if photo height less than width.
     *
     * @param photoPath path to photo.
     * @param bounds bounds of photo.
     * @return vertical photo.
     */
    public static Bitmap changePhotoOrientation(String photoPath, int bounds) {
        Bitmap photoBitmap = BitmapResizer.scalePhoto(photoPath, bounds);
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

    /**
     * Converts dp to pixels.
     *
     * @param dp value of dp.
     * @param context application context.
     * @return dp value in pixels.
     */
    private static int dpToPx(final int dp, final Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
