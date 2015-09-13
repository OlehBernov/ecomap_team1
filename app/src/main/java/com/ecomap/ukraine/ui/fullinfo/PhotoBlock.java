package com.ecomap.ukraine.ui.fullinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.ui.fragments.ProblemPhotoSlidePager;
import com.ecomap.ukraine.util.BasicContentLayout;
import com.ecomap.ukraine.util.BitmapResizer;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;


public class PhotoBlock extends LinearLayout  {

    private static final String TRANSFORMATION = "transformation";
    private static final String POSITION = "position";

    private Context context;

    public PhotoBlock(Context context) {
        this(context, null);
    }

    public PhotoBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    public void setPhotos(final List<Photo> photos) {
        BasicContentLayout photoPreview = (BasicContentLayout) findViewById(R.id.basic_content_layout_photo);
        int position = 0;
        for (final Photo photo : photos) {
            ImageView photoView = new ImageView(context);
            photoPreview.addHorizontalBlock(photoView);
            photoView.setId(position++);
            loadPhotoToView(photo, photoView);
            photoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhotoSlidePager(v.getId(), photos);
                }
            });
        }
    }

    /**
     * Opens full photos preview.
     *
     * @param position position of selected photo on small photo preview panel.
     * @param photos list of all photos, which related to the problem.
     */
    private void openPhotoSlidePager(final int position, final List<Photo> photos) {
        ProblemPhotoSlidePager.setContent(photos);
        Intent intent = new Intent(context, ProblemPhotoSlidePager.class);
        intent.putExtra(POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Performs photo loading to photo preview panel.
     *
     * @param photo photo which related to the problem.
     * @param photoView view which will contain the photo.
     */
    private void loadPhotoToView(final Photo photo, final ImageView photoView) {
        BitmapTransformation transformation = new  BitmapTransformation(context) {
            @Override
            public Bitmap transform(BitmapPool pool, Bitmap source,
                                    int outWidth, int outHeight) {
                int photoSize = (int) context.getResources().getDimension(R.dimen.small_photo_size);
                Bitmap resizedBitmap = BitmapResizer.resizeBitmap(source, photoSize, context);
                if (resizedBitmap != source) {
                    source.recycle();
                }
                return resizedBitmap;
            }

            @Override
            public String getId() {
                return TRANSFORMATION;
            }

        };

        Glide.with(context)
                .load(photo.getLink())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(transformation)
                .into(photoView);
    }

    /**
     * Initiates the view.
     */
    private void init(Context context) {
        inflate(context, R.layout.problem_photo, this);
    }

}
